package com.deptManager.Dept.controller.book;

import com.deptManager.Dept.entity.Book;
import com.deptManager.Dept.exception.bookException.BookException;
import com.deptManager.Dept.model.bookmodel.*;
import com.deptManager.Dept.service.bookService.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/add/{userId}")
    public BookCreationResponseModel createNewBook(@RequestBody BookCreationRequestModel book, @PathVariable Long userId) throws BookException {
        return bookService.createBook(userId, book);
    }

    @GetMapping("/getAllBooks/{userId}")
    public List<Book> getAllBooks(@PathVariable Long userId) throws BookException {
        return bookService.getAllBooks(userId);
    }

    @GetMapping("/get/{id}")
    public Book getBookById(@PathVariable Long id) throws BookException {
        return bookService.getBookById(id);
    }

    @DeleteMapping("/delete/{id}")
    public BookResponse removeBook(@PathVariable Long id) throws BookException {
        return bookService.deleteBook(id);
    }

    @PutMapping("/edit/{id}")
    public BookResponse editBook(@RequestBody BookRequest body, @PathVariable Long id) throws BookException {
        return bookService.editBook(id, body);
    }

    @GetMapping("/totalBooks/{userId}")
    public TotalBooksResponse getTheTotalNumberOfBooks(@PathVariable Long userId) throws BookException {
        return bookService.getTheTotalNumberOfBooks(userId);
    }
}
