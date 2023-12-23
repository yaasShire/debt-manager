package com.deptManager.Dept.service.debtTransactionService;

import com.deptManager.Dept.entity.DebtTransaction;
import com.deptManager.Dept.entity.Page;
import com.deptManager.Dept.exception.debtTransactionException.DebtTransactionException;
import com.deptManager.Dept.exception.deptItemException.DeptItemException;
import com.deptManager.Dept.model.deptTransactionModel.DebtTransactionRequestModel;
import com.deptManager.Dept.model.deptTransactionModel.DebtTransactionResponseModel;
import com.deptManager.Dept.model.deptTransactionModel.DebtTransactionalTotal;
import com.deptManager.Dept.repository.DebtTransactionRepository;
import com.deptManager.Dept.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DebtTransactionServiceImpl implements DebTransactionService {
    private final DebtTransactionRepository debtTransactionRepository;
    private final PageRepository pageRepository;
    @Override
    public DebtTransactionResponseModel addTransaction(Long pageId, DebtTransactionRequestModel body) throws DebtTransactionException {
        try {
            Optional<Page> page = pageRepository.findPageById(pageId);
            if (page.isPresent()){
                if (body.getAmmount() == null){
                    throw new DebtTransactionException("Ammount is required", HttpStatus.BAD_REQUEST);
                }
                if (body.getAmmount() <= 0){
                    throw new DebtTransactionException("Ammount must be greater than 0", HttpStatus.BAD_REQUEST);
                }
                if(body.getDate() == null){
                    throw new DebtTransactionException("Date is required", HttpStatus.BAD_REQUEST);
                }
                if(page.get().getRemainingTotalDept() == 0.0){
                    throw new DebtTransactionException("debt balance is " + page.get().getRemainingTotalDept());
                }
                if (body.getAmmount() > page.get().getRemainingTotalDept()){
                    throw new DebtTransactionException("Ammount cannot be greater than " + page.get().getRemainingTotalDept());
                }
                page.get().setRemainingTotalDept(page.get().getRemainingTotalDept() - body.getAmmount());
                DebtTransaction debtTransaction = DebtTransaction.builder()
                        .ammount(body.getAmmount())
                        .date(body.getDate())
                        .remaining(page.get().getRemainingTotalDept())
                        .page(page.get())
                        .build();
                debtTransactionRepository.save(debtTransaction);
                pageRepository.save(page.get());
                DebtTransactionResponseModel responseModel = DebtTransactionResponseModel.builder()
                        .status(HttpStatus.CREATED)
                        .message("Debt transaction added")
                        .build();
                return responseModel;
            }else {
                throw new DeptItemException("page with id " + pageId + " does not exist");
            }
        }catch (Exception e){
            throw new DebtTransactionException(e.getMessage());
        }
    }

    @Override
    public List<DebtTransaction> getAllDebtTransactions(Long pageId) throws DebtTransactionException {
        try {
            Optional<Page> page = pageRepository.findPageById(pageId);
            if (page.isPresent()){
                Optional<List<DebtTransaction>> debtTransactions = debtTransactionRepository.findDebTransactionsByPageId(pageId);
                if (debtTransactions.isPresent()){
                return debtTransactions.get();
                }else {
                    throw new DebtTransactionException("No transactions", HttpStatus.NO_CONTENT);
                }
            }else {
                throw new DebtTransactionException("Page with id " + pageId + " does not exist", HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            throw new DebtTransactionException(e.getMessage());
        }
    }

    @Override
    public DebtTransaction getDebtTransactionById(Long id) throws DebtTransactionException {
        try {
            Optional<DebtTransaction> debtTransaction = debtTransactionRepository.findDebtTransactionById(id);
            if (debtTransaction.isPresent()){
                return debtTransaction.get();
            }else {
                throw new DebtTransactionException("Debt transaction with id " + id + " does not exist", HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            throw new DebtTransactionException(e.getMessage());
        }
    }

    @Override
    public DebtTransactionResponseModel editDebtTransaction(Long id, DebtTransactionRequestModel body) throws DebtTransactionException {
        try {
            Optional<DebtTransaction> debtTransaction = debtTransactionRepository.findDebtTransactionById(id);
            if (debtTransaction.isPresent()){
                if (body.getAmmount() != null || body.getAmmount() <= 0){
                    debtTransaction.get().setAmmount(body.getAmmount());
                }
                if(body.getDate() != null){
                    debtTransaction.get().setDate(body.getDate());
                }
                Optional<Page> page = pageRepository.findPageById(debtTransaction.get().getPage().getId());
                if (page.isPresent()){
                    if(page.get().getRemainingTotalDept() == 0.0){
                        throw new DebtTransactionException("debt balance is " + page.get().getRemainingTotalDept());
                    }
                    if (body.getAmmount() > page.get().getRemainingTotalDept()){
                        throw new DebtTransactionException("Ammount cannot be greater than " + page.get().getRemainingTotalDept(), HttpStatus.BAD_REQUEST);
                    }

                    page.get().setRemainingTotalDept(page.get().getRemainingTotalDept() - body.getAmmount());
                    debtTransactionRepository.save(debtTransaction.get());
                    pageRepository.save(page.get());

                }else {
                    throw new DebtTransactionException("Page with id " + debtTransaction.get().getPage().getId() + " does not exist", HttpStatus.NOT_FOUND);
                }
            }else {
                throw new DebtTransactionException("Dept transaction with id " + id + " does not exist");
            }
        }catch (Exception e){
            throw new DebtTransactionException(e.getMessage());
        }
        return null;
    }

    @Override
    public DebtTransactionResponseModel deleteDebtTransaction(Long id) throws DebtTransactionException {
        try {
            Optional<DebtTransaction> debtTransaction = debtTransactionRepository.findDebtTransactionById(id);
            if (debtTransaction.isPresent()){
                Optional<Page> page = pageRepository.findPageById(debtTransaction.get().getPage().getId());
                if (page.isPresent()){
                    page.get().setRemainingTotalDept(page.get().getRemainingTotalDept() + debtTransaction.get().getAmmount());
                    pageRepository.save(page.get());
                    debtTransactionRepository.deleteById(id);
                    DebtTransactionResponseModel debtTransactionResponseModel = DebtTransactionResponseModel.builder()
                            .status(HttpStatus.OK)
                            .message("Debt transaction is deleted successfully")
                            .build();
                    return debtTransactionResponseModel;
                }else {
                    throw new DebtTransactionException("Page with id " + debtTransaction.get().getPage().getId() + debtTransaction.get().getPage().getId() + " does not exist", HttpStatus.NOT_FOUND);
                }
            }else {
                throw new DebtTransactionException("Dept transaction with id " + id +" does not exist");
            }
        }catch (Exception e){
            throw new DebtTransactionException(e.getMessage());
        }
    }

    @Override
    public DebtTransactionalTotal getTotalDebtTransaction(Long pageId) throws DebtTransactionException {
        try {
            Optional<Page> page = pageRepository.findPageById(pageId);
            if (page.isPresent()){
                Optional<Double> total = debtTransactionRepository.findTotalDebtTransactionsByPageId(pageId);
                if (total.isPresent()){
                    DebtTransactionalTotal totalDeptTransactions = DebtTransactionalTotal.builder()
                            .status(HttpStatus.OK)
                            .total(total.get())
                            .build();
                    return totalDeptTransactions;
                }else {
                    throw new DebtTransactionException("No total dept transactions", HttpStatus.NO_CONTENT);
                }
            }else {
                throw new DebtTransactionException("Page with id " + pageId + " does not exist");
            }

        }catch (Exception e){
            throw new DebtTransactionException(e.getMessage());

        }
    }
}
