package com.deptManager.Dept.repository;

import com.deptManager.Dept.entity.DeptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeptItemRepository extends JpaRepository<DeptItem, Long> {
    @Query("SELECT i FROM DeptItem i JOIN i.page p WHERE p.id = :pageId")
    Optional<List<DeptItem>> findDeptItemsByPageId(Long pageId);

    Optional<DeptItem> findDeptItemById(Long id);
}
