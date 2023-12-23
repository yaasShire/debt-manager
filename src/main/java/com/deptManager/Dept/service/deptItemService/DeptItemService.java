package com.deptManager.Dept.service.deptItemService;

import com.deptManager.Dept.entity.Book;
import com.deptManager.Dept.entity.DeptItem;
import com.deptManager.Dept.exception.deptItemException.DeptItemException;
import com.deptManager.Dept.exception.pageException.PageException;
import com.deptManager.Dept.model.deptItemModel.DeptItemRequestModel;
import com.deptManager.Dept.model.deptItemModel.DeptItemResponseModel;

import java.util.List;

public interface DeptItemService {
    DeptItemResponseModel addDeptItem(Long pageId, DeptItemRequestModel body) throws DeptItemException;

    List<DeptItem> getAllDeptItems(Long idpageId) throws DeptItemException;


    DeptItem getDeptItemById(Long id) throws PageException;

    DeptItemResponseModel editDeptItem(Long id, DeptItemRequestModel body) throws DeptItemException;

    DeptItemResponseModel deleteDeptItem(Long id) throws DeptItemException;
}
