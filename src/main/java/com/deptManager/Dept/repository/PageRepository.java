package com.deptManager.Dept.repository;

import com.deptManager.Dept.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long> {
    @Query("SELECT p FROM Page p JOIN p.book b WHERE b.id = :bookId")
    Optional<List<Page>> findPagesByBookId(Long bookId);

    Optional<Page> findPageById(Long id);

    //SELECT p FROM Page p WHERE CAST(p.endDate AS date) - CURRENT_DATE <= 10
    @Query("SELECT p FROM Page p WHERE timestampdiff(DAY, CURRENT_DATE, CAST(p.endDate AS date)) <= 10 AND timestampdiff(DAY, CURRENT_DATE, CAST(p.endDate AS date)) > 0 AND p.book.user.id=:userId")
    Optional<List<Page>> findOpenPagesWithEndDateWithinNexTenDays(Long userId);

    //SELECT p FROM Page p WHERE p.startDate <= CURRENT_DATE - INTERVAL '1 MONTH' AND p.startDate >= CURRENT_DATE - INTERVAL '2 MONTH'
    @Query("SELECT p FROM Page p WHERE timestampdiff(DAY, CURRENT_DATE, CAST(p.endDate AS date)) <= 0 AND p.book.user.id=:userId")
    Optional<List<Page>> findOpenPagesWithDueDate(Long userId);

    @Query("SELECT p FROM Page p WHERE timestampdiff(DAY, CURRENT_DATE, CAST(p.endDate AS date)) <= 0")
    Optional<List<Page>> findAllOpenPagesWithDueDate();
}