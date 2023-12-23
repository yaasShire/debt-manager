package com.deptManager.Dept.service.pageService;

import com.deptManager.Dept.entity.Page;
import com.deptManager.Dept.exception.pageException.PageException;
import com.deptManager.Dept.model.pageModel.PageEditRequestModel;
import com.deptManager.Dept.model.pageModel.PageRequestModel;
import com.deptManager.Dept.model.pageModel.PageResponseModel;

import java.util.List;

public interface PageService {
    PageResponseModel createPage(Long bookId, PageRequestModel body) throws PageException;

    List<Page> getAllPagesByBookId(Long bookId) throws PageException;

    Page findPageById(Long pageId) throws PageException;

    PageResponseModel editPage(Long pageId, PageEditRequestModel body) throws PageException;

    PageResponseModel deletePage(Long pageId) throws PageException;

    List<Page> findOpenPagesWithEndDateWithinNexTenDays(Long userId) throws PageException;

    List<Page> findOpenPagesWithDueDate(Long userId) throws PageException;
}
