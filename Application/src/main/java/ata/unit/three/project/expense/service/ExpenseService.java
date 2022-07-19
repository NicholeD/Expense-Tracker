package ata.unit.three.project.expense.service;

import ata.unit.three.project.expense.dynamodb.ExpenseItem;
import ata.unit.three.project.expense.dynamodb.ExpenseItemList;
import ata.unit.three.project.expense.dynamodb.ExpenseServiceRepository;
import ata.unit.three.project.expense.lambda.models.Expense;
import ata.unit.three.project.expense.service.exceptions.InvalidDataException;
import ata.unit.three.project.expense.service.exceptions.InvalidExpenseException;
import ata.unit.three.project.expense.service.exceptions.ItemNotFoundException;
import ata.unit.three.project.expense.service.model.ExpenseItemConverter;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;

@Singleton
public class ExpenseService {

    private ExpenseServiceRepository expenseServiceRepository;
    private ExpenseItemConverter expenseItemConverter;

    @Inject
    public ExpenseService(ExpenseServiceRepository expenseServiceRepository,
                          ExpenseItemConverter expenseItemConverter) {
        this.expenseServiceRepository = expenseServiceRepository;
        this.expenseItemConverter = expenseItemConverter;
    }

    public ExpenseItem getExpenseById(String expenseId) {
        if (StringUtils.isEmpty(expenseId) || isInvalidUuid(expenseId)) {
            throw new InvalidDataException("Expense id is not present");
        }
        return expenseServiceRepository.getExpenseById(expenseId);
    }

    public List<ExpenseItem> getExpensesByEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new InvalidDataException("Email is not present");
        }
        return expenseServiceRepository.getExpensesByEmail(email);
    }

    public ExpenseItemList getExpenseItemListById(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new InvalidDataException("Id cannot be empty");
        }

        return expenseServiceRepository.getExpenseListById(id);
    }

    public String createExpense(Expense expense) {
        if (expense == null) {
            throw new InvalidDataException("Expense is null");
        }
        ExpenseItem expenseItem = expenseItemConverter.convert(expense);
        expenseServiceRepository.createExpense(expenseItem);
        return expenseItem.getId();
    }

    public void updateExpense(String expenseId, Expense updateExpense) {
        if (StringUtils.isEmpty(expenseId) || isInvalidUuid(expenseId)) {
            throw new InvalidDataException("Expense id is not present");
        }
        ExpenseItem item = expenseServiceRepository.getExpenseById(expenseId);
        if (item == null) {
            throw new ItemNotFoundException("Expense does not exist");
        }
        expenseServiceRepository.updateExpense(expenseId,
                updateExpense.getTitle(),
                updateExpense.getAmount());
    }

    public void deleteExpense(String expenseId) {
        if (StringUtils.isEmpty(expenseId) || isInvalidUuid(expenseId)) {
            throw new InvalidDataException("Expense id is not present");
        }
        expenseServiceRepository.deleteExpense(expenseId);
    }

    public String createExpenseList(String email, String title) {
        if (StringUtils.isEmpty(email) || email == null) {
            throw new InvalidDataException("Email is invalid");
        }
        if ( StringUtils.isEmpty(title)|| title == null) {
            throw new InvalidDataException("Title is invalid");
        }
        String expenseListId = randomUUID().toString();
        expenseServiceRepository.createExpenseList(expenseListId, email, title);
        return expenseListId;
    }

//    It shouldn't be possible to add/remove an expense item where The email of the expense item does not match the expense list
//    It should not be possible to add an expense item that's already in the list
//    It should not be possible to remove an expense item that does not exist in the list

    public void addExpenseItemToList(String id, String expenseId) {
        ExpenseItem expenseItem = expenseServiceRepository.getExpenseById(expenseId);
        ExpenseItemList expenseItemList = expenseServiceRepository.getExpenseListById(id);

        //Checking if parameters are valid
        if  (StringUtils.isEmpty(expenseId) || isInvalidUuid(expenseId)) {
            throw new InvalidDataException("Expense id is not present");
        } else if (StringUtils.isEmpty(id)) {
            throw new InvalidDataException("Id is not present");
        }

//        //Checking that expenseItem isn't null
//        if (expenseItem == null) {
//            throw new ItemNotFoundException("Expense does not exist");
//        }
//
//        //Checking itemList isn't null
//        if (itemList == null) {
//            throw new InvalidDataException("Expense List does not exist");
//        }

        //Checking if expense is already on this list
        if (expenseItemList.getExpenseItems().contains(expenseItem)) {
            throw new InvalidDataException("ExpenseItem already exists on this list");
        }

        //Checking if email to expense and expenseList match
        if (!expenseItem.getEmail().equals(expenseItemList.getEmail())) {
            throw new InvalidDataException("Email does not match");
        }

        expenseServiceRepository.addExpenseItemToList(id, expenseItem);
    }

    public void removeExpenseItemFromList(String id, String expenseId) {
        // Your Code Here
        ExpenseItem expenseItem = expenseServiceRepository.getExpenseById(expenseId);
        ExpenseItemList itemList = expenseServiceRepository.getExpenseListById(id);

        //Checking if parameters are valid
        if  (StringUtils.isEmpty(expenseId) || isInvalidUuid(expenseId)) {
            throw new InvalidDataException("Expense id is not present");
        } else if (StringUtils.isEmpty(id)) {
            throw new InvalidDataException("Id is not present");
        }

        //Checking that expenseItem isn't null
        if (expenseItem == null) {
            throw new ItemNotFoundException("Expense does not exist");
        }

        //Checking itemList isn't null
        if (itemList == null) {
            throw new InvalidDataException("Expense List does not exist");
        }

        //Checking if email to expense and expenseList match
        if (!Objects.equals(expenseItem.getEmail(), itemList.getEmail())) {
            throw new InvalidDataException("Email does not match");
        }
        expenseServiceRepository.removeExpenseItemToList(id, expenseItem);
    }

    public List<ExpenseItemList> getExpenseListByEmail(String email) {
        if (StringUtils.isEmpty(email) || email == null) {
            throw new InvalidDataException("Email is not present");
        }
        return expenseServiceRepository.getExpenseListsByEmail(email);
    }

    private boolean isInvalidUuid(String uuid) {
        try {
            fromString(uuid);
        } catch (IllegalArgumentException exception) {
            return true;
        }
        return false;
    }
}
