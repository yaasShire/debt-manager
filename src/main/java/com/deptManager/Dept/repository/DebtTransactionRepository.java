package com.deptManager.Dept.repository;

import com.deptManager.Dept.entity.DebtTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DebtTransactionRepository extends JpaRepository<DebtTransaction, Long> {

    @Query("SELECT t from DebtTransaction t JOIN t.page p WHERE p.id = :pageId")
    Optional<List<DebtTransaction>> findDebTransactionsByPageId(Long pageId);

    Optional<DebtTransaction> findDebtTransactionById(Long id);

    @Query("SELECT SUM(t.ammount) FROM DebtTransaction t WHERE t.page.id= :pageId")
    Optional<Double> findTotalDebtTransactionsByPageId(Long pageId);

}
