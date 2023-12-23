package com.deptManager.Dept.service.debtTransactionService;

import com.deptManager.Dept.entity.DebtTransaction;
import com.deptManager.Dept.exception.debtTransactionException.DebtTransactionException;
import com.deptManager.Dept.model.deptTransactionModel.DebtTransactionRequestModel;
import com.deptManager.Dept.model.deptTransactionModel.DebtTransactionResponseModel;
import com.deptManager.Dept.model.deptTransactionModel.DebtTransactionalTotal;

import java.util.List;

public interface DebTransactionService {
    DebtTransactionResponseModel addTransaction(Long pageId, DebtTransactionRequestModel body) throws DebtTransactionException;

    List<DebtTransaction> getAllDebtTransactions(Long pageId) throws DebtTransactionException;

    DebtTransaction getDebtTransactionById(Long id) throws DebtTransactionException;

    DebtTransactionResponseModel editDebtTransaction(Long id, DebtTransactionRequestModel body) throws DebtTransactionException;

    DebtTransactionResponseModel deleteDebtTransaction(Long id) throws DebtTransactionException;

    DebtTransactionalTotal getTotalDebtTransaction(Long pageId) throws DebtTransactionException;
}
