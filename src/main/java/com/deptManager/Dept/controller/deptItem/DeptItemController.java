package com.deptManager.Dept.controller.deptItem;

import com.deptManager.Dept.entity.Book;
import com.deptManager.Dept.entity.DeptItem;
import com.deptManager.Dept.exception.bookException.BookException;
import com.deptManager.Dept.exception.deptItemException.DeptItemException;
import com.deptManager.Dept.exception.pageException.PageException;
import com.deptManager.Dept.model.bookmodel.BookResponse;
import com.deptManager.Dept.model.deptItemModel.DeptItemRequestModel;
import com.deptManager.Dept.model.deptItemModel.DeptItemResponseModel;
import com.deptManager.Dept.repository.DeptItemRepository;
import com.deptManager.Dept.service.deptItemService.DeptItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deptItem")
@RequiredArgsConstructor
public class DeptItemController {
    private final DeptItemService deptItemService;

    @PostMapping("/add/{pageId}")
    public DeptItemResponseModel addDeptItem(@RequestBody DeptItemRequestModel body, @PathVariable Long pageId) throws DeptItemException {
        return deptItemService.addDeptItem(pageId, body);
    }

    @GetMapping("/getAllDeptItems/{pageId}")
    public List<DeptItem> getAllDeptItems(@PathVariable Long pageId) throws DeptItemException {
        return deptItemService.getAllDeptItems(pageId);
    }

    @GetMapping("/get/{id}")
    public DeptItem getDeptItemById(@PathVariable Long id) throws BookException, PageException {
        return deptItemService.getDeptItemById(id);
    }

    @PutMapping("/edit/{id}")
    public DeptItemResponseModel editDeptItem(@RequestBody DeptItemRequestModel body, @PathVariable Long id) throws DeptItemException {
        return deptItemService.editDeptItem(id, body);
    }

    @DeleteMapping("/delete/{id}")
    public DeptItemResponseModel deleteDeptItem(@PathVariable Long id) throws DeptItemException {
        return deptItemService.deleteDeptItem(id);
    }
}
