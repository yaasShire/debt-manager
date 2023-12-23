package com.deptManager.Dept.controller.page;

import com.deptManager.Dept.entity.Page;
import com.deptManager.Dept.exception.pageException.PageException;
import com.deptManager.Dept.model.pageModel.PageEditRequestModel;
import com.deptManager.Dept.model.pageModel.PageRequestModel;
import com.deptManager.Dept.model.pageModel.PageResponseModel;
import com.deptManager.Dept.service.pageService.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/page")
@RequiredArgsConstructor
public class PageController {
    private final PageService pageService;

    @PostMapping("/add/{bookId}")
    public PageResponseModel addPage(@RequestBody PageRequestModel body, @PathVariable Long bookId) throws PageException {
        System.out.println("page controller");
        return pageService.createPage(bookId, body);
    }
    @GetMapping("/getAllPages/{bookId}")
    public List<Page> getAllPagesByBookId(@PathVariable Long bookId) throws PageException {
        return pageService.getAllPagesByBookId(bookId);
    }
    @GetMapping("/get/{pageId}")
    public Page getPageById(@PathVariable Long pageId) throws PageException {
        return pageService.findPageById(pageId);
    }
    @PutMapping("/edit/{pageId}")
    public PageResponseModel editPage(@RequestBody PageEditRequestModel body, @PathVariable Long pageId) throws PageException {
        return pageService.editPage(pageId, body);
    }

    @DeleteMapping("/delete/{pageId}")
    public PageResponseModel deletePage(@PathVariable Long pageId) throws PageException {
        return pageService.deletePage(pageId);
    }

    @GetMapping("/getPageEndDateWithInTenDays/{userId}")
    public List<Page> findOpenPagesWithEndDateWithinNexTenDays(@PathVariable Long userId) throws PageException {
        return pageService.findOpenPagesWithEndDateWithinNexTenDays(userId);
    }

    @GetMapping("/findOpenPagesWithDueDate/{userId}")
    public List<Page> findOpenPagesWithDueDate(@PathVariable Long userId) throws PageException {
        return pageService.findOpenPagesWithDueDate(userId);
    }
}