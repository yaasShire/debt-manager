package com.deptManager.Dept.service.bookService;

import com.deptManager.Dept.entity.AppUser;
import com.deptManager.Dept.entity.Book;
import com.deptManager.Dept.exception.bookException.BookException;
import com.deptManager.Dept.model.bookmodel.*;
import com.deptManager.Dept.repository.AppUserRepository;
import com.deptManager.Dept.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AppUserRepository appUserRepository;
    @Override
    public BookCreationResponseModel createBook(Long userId, BookCreationRequestModel book) throws BookException {
        log.info("create book {}", book);
        try {
            if(userId == null || userId<=0){
                throw new BookException("User id is required");
            }
            if(book.getOwnerName() == null || book.getOwnerName().isBlank()){
                throw new BookException("Owner name is required");
            }
            if(book.getOwnerNumber() == null || book.getOwnerNumber().isBlank()){
                throw new BookException("Owner number is required");
            }
            if(book.getBackUpNumber() == null || book.getBackUpNumber().isBlank()){
                throw new BookException("Owner backup number is required");
            }
            Optional<AppUser> user = appUserRepository.findAppUserById(userId);
            if(!user.isPresent()){
                throw new BookException("user with id " + userId+  " doest not exist", HttpStatus.NOT_FOUND);
            }
            Optional<Book> bookCheck = bookRepository.findByOwnerName(book.getOwnerName());
            if(bookCheck.isPresent()){
                throw new BookException("Book already exists");
            }

            Book newBook = Book.builder()
                    .ownerName(book.getOwnerName())
                    .ownerNumber(book.getOwnerNumber())
                    .backUpNumber(book.getBackUpNumber())
                    .createDate(getCurrentDate())
                    .user(user.get())
                    .build();
            bookRepository.save(newBook);
            BookCreationResponseModel response = BookCreationResponseModel.builder()
                    .status(HttpStatus.CREATED)
                    .message("Book created successfully")
                    .build();
            return response;

        } catch (BookException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Book> getAllBooks(Long userId) throws BookException {
        try {
        Optional<AppUser> user = appUserRepository.findAppUserById(userId);
        if(user.isPresent()){
        List<Book> books = bookRepository.findBooksByUserId(userId);
        if(books.size() == 0){
            throw new BookException("No books found", HttpStatus.NO_CONTENT);
        }
        return books;
        }else{
            throw new BookException("User with id " + userId + " does not exist");
        }
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }

    @Override
    public Book getBookById(Long id) throws BookException {
        log.info("book {}", id);
        Optional<Book> book = bookRepository.findBookById(id);
//        log.info("book {}", book.get());
//        System.out.println("book"+ book.get());
        if(book.isPresent()){
            return book.get();
        }else{
        throw new BookException("Book not found", HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public BookResponse deleteBook(Long id) throws BookException {
        Optional<Book> bookOption = bookRepository.findBookById(id);
        if(bookOption.isPresent()){
            bookRepository.deleteById(id);
            BookResponse response = BookResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Book is deleted successfully")
                    .build();
            return response;
        }else{
            throw new BookException("Book does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public BookResponse editBook(Long id, BookRequest body) throws BookException {
        Optional<Book> bookOption = bookRepository.findBookById(id);
        if (body.getOwnerName() == null && body.getOwnerNumber() == null && body.getBackUpNumber() == null){
            throw new BookException("You must provide at least one parameter");
        }
        if(bookOption.isPresent()){
            if(body.getOwnerName() != null && !body.getOwnerName().isBlank()){
            bookOption.get().setOwnerName(body.getOwnerName());
            }
            if(body.getOwnerNumber() != null && !body.getOwnerNumber().isBlank()){
                bookOption.get().setOwnerNumber(body.getOwnerNumber());
            }
            if(body.getBackUpNumber() != null && !body.getBackUpNumber().isBlank()){
                bookOption.get().setBackUpNumber(body.getBackUpNumber());
            }
            bookRepository.save(bookOption.get());
            BookResponse response = BookResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Book updated successfully")
                    .build();
            return response;
        }else {
            throw new BookException("Book does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public TotalBooksResponse getTheTotalNumberOfBooks(Long userId) throws BookException {
        try {
            Optional<AppUser> user = appUserRepository.findAppUserById(userId);
            if (user.isPresent()){
                Optional<Integer> booksTotal = bookRepository.findTotalNumberOfBooks(userId);
                if (booksTotal.isPresent()){
                    TotalBooksResponse totalBooks = TotalBooksResponse.builder()
                            .status(HttpStatus.OK)
                            .quantity(booksTotal.get())
                            .build();
                    return totalBooks;
                }else {
                    throw new BookException("Noo books found", HttpStatus.NO_CONTENT);
                }
            }else {
                throw new BookException("User with id " + " does not exist");
            }
        }catch (Exception e){
            throw new BookException(e.getMessage());
        }
    }

    public String getCurrentDate(){
        // Get the current date and time
        Date date = new Date();

        // Convert the date to a string
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

}
