package com.deptManager.Dept.service.deptItemService;

import com.deptManager.Dept.entity.DeptItem;
import com.deptManager.Dept.entity.Page;
import com.deptManager.Dept.exception.deptItemException.DeptItemException;
import com.deptManager.Dept.exception.pageException.PageException;
import com.deptManager.Dept.model.deptItemModel.DeptItemRequestModel;
import com.deptManager.Dept.model.deptItemModel.DeptItemResponseModel;
import com.deptManager.Dept.model.pageModel.PageResponseModel;
import com.deptManager.Dept.repository.DeptItemRepository;
import com.deptManager.Dept.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeptItemServiceImpl implements DeptItemService{
    private final DeptItemRepository deptItemRepository;
    private final PageRepository pageRepository;
    @Override
    public DeptItemResponseModel addDeptItem(Long pageId, DeptItemRequestModel body) throws DeptItemException {
        try {
            Optional<Page> page = pageRepository.findPageById(pageId);
            if (page.isPresent()){
                if (body.getProductName() == null || body.getProductName().isBlank()){
                    throw new DeptItemException("Product name is required");
                }
                if (body.getQuantity() == null){
                    throw new DeptItemException("Quantity is required");
                }
                if (body.getQuantity() <= 0){
                    throw new DeptItemException("Quantity must be greater than 0");
                }

                if (body.getPrice() == null){
                    throw new DeptItemException("Price is required");
                }
                if (body.getPrice() <= 0){
                    throw new DeptItemException("Price must be greater than 0");
                }
                page.get().setCurrentTotalDept(page.get().getCurrentTotalDept() + body.getPrice() * body.getQuantity());
                page.get().setRemainingTotalDept(page.get().getRemainingTotalDept() + body.getPrice() * body.getQuantity());

                DeptItem deptItem = DeptItem.builder()
                        .productName(body.getProductName())
                        .price(body.getPrice())
                        .quantity(body.getQuantity())
                        .date(new Date())
                        .page(page.get())
                        .build();
                deptItemRepository.save(deptItem);
                pageRepository.save(page.get());
                DeptItemResponseModel deptItemResponseModel = DeptItemResponseModel.builder()
                        .status(HttpStatus.CREATED)
                        .message("Dept item added successfully")
                        .build();
                return deptItemResponseModel;
            }else {
                throw new DeptItemException("page with id " + pageId + " does not exist");
            }
        }catch (Exception e){
            throw new DeptItemException(e.getMessage());
        }
    }

    @Override
    public List<DeptItem> getAllDeptItems(Long pageId) throws DeptItemException {
        try {
            Optional<Page> page = pageRepository.findPageById(pageId);
            if (page.isPresent()){
                Optional<List<DeptItem>> deptItems = deptItemRepository.findDeptItemsByPageId(pageId);

                if (deptItems.isPresent()){
                    return deptItems.get();
//                    if(deptItems.get().size() == 0){
//                        throw new DeptItemException("No dept item found", HttpStatus.NO_CONTENT);
//                    }else {
//                    }
                }else {
                    throw new DeptItemException("No dept item found", HttpStatus.NO_CONTENT);
                }
            }else {
                throw new DeptItemException("Page with id " + pageId + " does not exist");
            }
        }catch (Exception e){
            throw new DeptItemException(e.getMessage());
        }

    }

    @Override
    public DeptItem getDeptItemById(Long id) throws PageException {
        try {
            Optional<DeptItem> deptItem = deptItemRepository.findDeptItemById(id);
            if (deptItem.isPresent()){
                return deptItem.get();
            }else {
                throw new PageException("Dept item with id " + id + " does not exist", HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            throw new PageException(e.getMessage());
        }
    }

    @Override
    public DeptItemResponseModel editDeptItem(Long id, DeptItemRequestModel body) throws DeptItemException {
        try {
            Optional<DeptItem> dept = deptItemRepository.findDeptItemById(id);
            if(body.getProductName() == null &&  body.getQuantity() ==null &&  body.getPrice() == null){
                throw new DeptItemException("You must provide at least one parameter", HttpStatus.BAD_REQUEST);
            }
            if (dept.isPresent()){
                if (body.getProductName() !=null){
                    dept.get().setProductName(body.getProductName());
                }
                if (body.getQuantity() != null){
                    dept.get().setQuantity(body.getQuantity());
                }
                if (body.getPrice() != null){
                    dept.get().setPrice(body.getPrice());
                }
                deptItemRepository.save(dept.get());
                DeptItemResponseModel deptItemResponseModel = DeptItemResponseModel.builder()
                        .status(HttpStatus.OK)
                        .message("Dept edit successfully")
                        .build();
                return deptItemResponseModel;
            }else {
                throw new DeptItemException("Dept item with id + " + id + " does not exist");
            }
        }catch (Exception e){
            throw new DeptItemException(e.getMessage());
        }
    }

    @Override
    public DeptItemResponseModel deleteDeptItem(Long id) throws DeptItemException {
        try {
            Optional<DeptItem> deptItem = deptItemRepository.findDeptItemById(id);
            if (deptItem.isPresent()){
                deptItemRepository.deleteById(id);
                DeptItemResponseModel deptItemResponse = DeptItemResponseModel.builder()
                        .status(HttpStatus.OK)
                        .message("Dept item is deleted successfully")
                        .build();
                return deptItemResponse;
            }else {
                throw new PageException("Dept item with id " + id + " does not exist");
            }
        }catch (Exception e){
            throw new DeptItemException(e.getMessage());
        }
    }
}