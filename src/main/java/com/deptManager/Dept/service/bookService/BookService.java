package com.deptManager.Dept.service.bookService;

import com.deptManager.Dept.entity.Book;
import com.deptManager.Dept.exception.bookException.BookException;
import com.deptManager.Dept.model.bookmodel.*;

import java.util.List;
import java.util.Optional;

public interface BookService {
    BookCreationResponseModel createBook(Long userId, BookCreationRequestModel book) throws BookException;

    List<Book> getAllBooks(Long userId) throws BookException;

    Book getBookById(Long id) throws BookException;

    BookResponse deleteBook(Long id) throws BookException;

    BookResponse editBook(Long id, BookRequest body) throws BookException;

    TotalBooksResponse getTheTotalNumberOfBooks(Long userId) throws BookException;
}
