package com.deptManager.Dept.controller.debtTransaction;

import com.deptManager.Dept.entity.DebtTransaction;
import com.deptManager.Dept.exception.debtTransactionException.DebtTransactionException;
import com.deptManager.Dept.model.deptTransactionModel.DebtTransactionRequestModel;
import com.deptManager.Dept.model.deptTransactionModel.DebtTransactionResponseModel;
import com.deptManager.Dept.model.deptTransactionModel.DebtTransactionalTotal;
import com.deptManager.Dept.service.debtTransactionService.DebTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class DebtTransactionController {
    private final DebTransactionService debTransactionService;

    @PostMapping("/add/{pageId}")
    public DebtTransactionResponseModel addDeptTransaction(@RequestBody DebtTransactionRequestModel body, @PathVariable Long pageId) throws DebtTransactionException {
        return debTransactionService.addTransaction(pageId, body);
    }

    @GetMapping("/getAllTransactions/{pageId}")
    public List<DebtTransaction> getAllDebtTransactions(@PathVariable Long pageId) throws DebtTransactionException {
        return debTransactionService.getAllDebtTransactions(pageId);
    }

    @GetMapping("/get/{id}")
    public DebtTransaction getDebtTransactionById(@PathVariable Long id) throws DebtTransactionException {
        return debTransactionService.getDebtTransactionById(id);
    }

    @PutMapping("/edit/{id}")
    public DebtTransactionResponseModel editDebtTransaction(@RequestBody DebtTransactionRequestModel body, @PathVariable Long id) throws DebtTransactionException {
        return debTransactionService.editDebtTransaction(id, body);
    }

    @DeleteMapping("/delete/{id}")
    public DebtTransactionResponseModel deleteDebtTransaction(@PathVariable Long id) throws DebtTransactionException {
        return debTransactionService.deleteDebtTransaction(id);
    }

    @GetMapping("/total/{pageId}")
    public DebtTransactionalTotal getTotalDebtTransactional(@PathVariable Long pageId) throws DebtTransactionException {
        return debTransactionService.getTotalDebtTransaction(pageId);
    }

}
