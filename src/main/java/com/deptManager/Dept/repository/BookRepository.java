package com.deptManager.Dept.repository;

import com.deptManager.Dept.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByOwnerName(String ownerName);

    Optional<Book> findBookById(Long id);

//    @Query("SELECT b FROM Book b WHERE b.user_id = :id")
//SELECT b
//    FROM Book b
//    JOIN b.user u
//    WHERE u.id = :userId
    @Query("SELECT b FROM Book b JOIN b.user u WHERE u.id = :id")
    List<Book> findBooksByUserId(Long id);

    @Query("SELECT COUNT(b) FROM Book b")
    Optional<Integer> findTotalNumberOfBooks(Long userId);
}
