package com.deptManager.Dept.service.pageService;

import com.deptManager.Dept.DeptApplication;
import com.deptManager.Dept.entity.AppUser;
import com.deptManager.Dept.entity.Book;
import com.deptManager.Dept.entity.Page;
import com.deptManager.Dept.exception.authenticationException.AuthenticationException;
import com.deptManager.Dept.exception.pageException.PageException;
import com.deptManager.Dept.model.authenticationModel.SendOTPRequestModel;
import com.deptManager.Dept.model.pageModel.AlertDebtSMSMessageModel;
import com.deptManager.Dept.model.pageModel.PageEditRequestModel;
import com.deptManager.Dept.model.pageModel.PageRequestModel;
import com.deptManager.Dept.model.pageModel.PageResponseModel;
import com.deptManager.Dept.repository.AppUserRepository;
import com.deptManager.Dept.repository.BookRepository;
import com.deptManager.Dept.repository.PageRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class PageServiceImpl implements PageService{
    private final PageRepository pageRepository;
    private final BookRepository bookRepository;
    private final AppUserRepository appUserRepository;
    List<Page> dueDatePages;
    @Override
    public PageResponseModel createPage(Long bookId, PageRequestModel body) throws PageException {
        log.info("page {}", body);
        if(body.getEndDate() == null){
            throw new PageException("end date is required", HttpStatus.BAD_REQUEST);
        }
        if(bookId == null){
            throw new PageException("Book id is required", HttpStatus.BAD_REQUEST);
        }
       try {
           Optional<Book> bookOptional = bookRepository.findBookById(bookId);
           if(!bookOptional.isPresent()){
               throw new PageException("Book with id "+ bookId + " does not exist", HttpStatus.NOT_FOUND);
           }else{
               Page page = Page.builder()
                       .startDate(new Date(System.currentTimeMillis()))
                       .endDate(body.getEndDate())
                       .currentTotalDept(body.getCurrentTotalDept())
                       .book(bookOptional.get())
                       .remainingTotalDept(body.getCurrentTotalDept())
                       .bId(bookOptional.get().getId())
                       .status(true)
                       .build();
               pageRepository.save(page);
               PageResponseModel pageResponseModel = PageResponseModel.builder()
                       .status(HttpStatus.CREATED)
                       .message("Page is created")
                       .build();
               return pageResponseModel;
           }
       }catch (Exception e){
           throw new PageException(e.getMessage());
       }
    }

    @Override
    public List<Page> getAllPagesByBookId(Long bookId) throws PageException {
        try {
            Optional<Book> bookOptional = bookRepository.findBookById(bookId);
            if (bookOptional.isPresent()){
                Optional<List<Page>> pages = pageRepository.findPagesByBookId(bookId);
                if (pages.isPresent()){
                    if (pages.get().size() ==0){
                        throw new PageException("No pages found", HttpStatus.NO_CONTENT);
                    }
                    return pages.get();
                }else {
                    throw new PageException("No pages found", HttpStatus.NO_CONTENT);
                }
            }else{
                throw new PageException("Book with id " + bookId + " does not exist");
            }

        }catch (Exception e){
            throw new PageException(e.getMessage());
        }
    }

    @Override
    public Page findPageById(Long pageId) throws PageException {
        try {
            Optional<Page> page = pageRepository.findPageById(pageId);
            if (page.isPresent()){
                return page.get();
            }else {
                throw new PageException("Page with id " + pageId + " does not exist", HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            throw new PageException(e.getMessage());
        }
    }

    @Override
    public PageResponseModel editPage(Long pageId, PageEditRequestModel body) throws PageException {
        try {
            if (body.getStatus() == null && body.getEndDate() == null && body.getCurrentTotalDept() == null){
                throw new PageException("You must provide at least one parameter", HttpStatus.BAD_REQUEST);
            }
            Optional<Page> page = pageRepository.findPageById(pageId);
            if (page.isPresent()){
                if(body.getEndDate() != null){
                    page.get().setEndDate(body.getEndDate());
                }
                if(body.getCurrentTotalDept() != null){
                    page.get().setCurrentTotalDept(body.getCurrentTotalDept());
                }
                if(body.getStatus() !=null){
                    page.get().setStatus(body.getStatus());
                }
                pageRepository.save(page.get());
                PageResponseModel pageResponseModel = PageResponseModel.builder()
                        .status(HttpStatus.OK)
                        .message("Page edited successfully")
                        .build();
                return pageResponseModel;
            }else {
                throw new PageException("Page with id " + pageId + " does not exist");
            }
        }catch (Exception e){
            throw new PageException(e.getMessage());
        }
    }

    @Override
    public PageResponseModel deletePage(Long pageId) throws PageException {
        try {
            Optional<Page> page = pageRepository.findPageById(pageId);
            if (page.isPresent()){
                pageRepository.deleteById(pageId);
                PageResponseModel pageResponseModel = PageResponseModel.builder()
                        .status(HttpStatus.OK)
                        .message("page is deleted successfully")
                        .build();
                return pageResponseModel;
            }else {
                throw new PageException("Page with id " + pageId + " does not exist");
            }
        }catch (Exception e){
            throw new PageException(e.getMessage());
        }

    }

    @Override
    public List<Page> findOpenPagesWithEndDateWithinNexTenDays(Long userId) throws PageException {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserById(userId);
            log.info("user page {}", appUser.get());
            if (appUser.isPresent()){
                Optional<List<Page>> pagesWithInTenDaysEndDate = pageRepository.findOpenPagesWithEndDateWithinNexTenDays(userId);
                if (pagesWithInTenDaysEndDate.isPresent()){
                    return pagesWithInTenDaysEndDate.get();
                }else {
                    throw new PageException("No pages with in five days end date found", HttpStatus.NO_CONTENT);
                }
            }else {
                throw new PageException("User with id " + userId + " does not exist", HttpStatus.NO_CONTENT);
            }

        }catch (Exception e){
            throw new PageException(e.getMessage());
        }
    }

    @Override
    public List<Page> findOpenPagesWithDueDate(Long userId) throws PageException {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserById(userId);
            if (appUser.isPresent()){
                Optional<List<Page>> openPagesWithDueDate = pageRepository.findOpenPagesWithDueDate(userId);
                if (openPagesWithDueDate.isPresent()){
                    dueDatePages = openPagesWithDueDate.get();
                    triggerSendOneAlertSMSNotification();
                    return openPagesWithDueDate.get();
                }else {
                    throw new PageException("No pages with in five days end date found", HttpStatus.NO_CONTENT);
                }
            }else {
                throw new PageException("User with id " + userId + " does not exist", HttpStatus.NO_CONTENT);
            }

        }catch (Exception e){
            throw new PageException(e.getMessage());
        }
    }


    // run every saturday once in a week '0 0 ? * * SAT'
    public void sendOneAlertSMSNotificationToCustomersWithDueDate() throws PageException {
        try {
                Optional<List<Page>> allOpenPagesWithDueDate = pageRepository.findAllOpenPagesWithDueDate();
                if (allOpenPagesWithDueDate.isPresent()){
                    for (Page page : allOpenPagesWithDueDate.get()){
                    System.out.println("page with due date" + page.getBook().getOwnerName());
                        sendSMSMessage(page);
                    }
                    triggerSendOneAlertSMSNotification();
                }else {
                    throw new PageException("No pages with in five days end date found", HttpStatus.NO_CONTENT);
                }

        }catch (Exception e){
            throw new PageException(e.getMessage());
        }

    }

    public void sendSMSMessage(Page page) throws URISyntaxException, IOException, InterruptedException, PageException {
//        generatedOTP.setGeneratedOTP(generateOTP());
//        generatedTime= LocalDateTime.now();
        AlertDebtSMSMessageModel alertSMS = AlertDebtSMSMessageModel.builder()
                .mobile(page.getBook().getOwnerNumber())
                .message("waqtiga deyn bixinta waa ladhaafay deynta iska bixi adigoo mahadsan")
                .build();
        Gson gson = new Gson();
        String otpJsonRequest =  gson.toJson(alertSMS);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://smsapi.hormuud.com/api/SendSMS"))
                .setHeader("Content-Type", "application/json")
                .header("Authorization", "Bearer "+ DeptApplication.getAccessToken())
                .POST(HttpRequest.BodyPublishers.ofString(otpJsonRequest))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Sent OTP response {}", response.body());
        System.out.println("otp result = "+ response.body());
        if (response.statusCode() != 200){
            throw new PageException("OTP Could not be sent");
        }
    }

//0 0 ? * * SAT
    @Scheduled(cron = "0 18 * * 1 *") // or any other scheduling configuration
    public void triggerSendOneAlertSMSNotification() {
        try {
            sendOneAlertSMSNotificationToCustomersWithDueDate();
        } catch (PageException e) {
            throw new RuntimeException(e);
        }
    }

}