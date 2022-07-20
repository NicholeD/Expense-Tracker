package ata.unit.three.project.expense.service;

import ata.unit.three.project.expense.dynamodb.ExpenseItem;
import ata.unit.three.project.expense.dynamodb.ExpenseItemList;
import ata.unit.three.project.expense.dynamodb.ExpenseServiceRepository;
import ata.unit.three.project.expense.lambda.models.Expense;
import ata.unit.three.project.expense.service.exceptions.InvalidDataException;
import ata.unit.three.project.expense.service.exceptions.ItemNotFoundException;
import ata.unit.three.project.expense.service.model.ExpenseItemConverter;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import net.andreinc.mockneat.MockNeat;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {

    private final MockNeat mockNeat = MockNeat.threadLocal();

    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseById
     *  ------------------------------------------------------------------------ **/

    @Test
    void get_expense_by_id() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        expenseItem.setId(id);
        expenseItem.setEmail(mockNeat.emails().val());
        expenseItem.setExpenseDate(Instant.now().toString());
        expenseItem.setTitle(mockNeat.strings().val());

        //WHEN
        when(expenseServiceRepository.getExpenseById(id)).thenReturn(expenseItem);

        //THEN
        ExpenseItem returnedExpenseItem = expenseService.getExpenseById(id);
        Assertions.assertEquals(returnedExpenseItem.getId(), expenseItem.getId());
        Assertions.assertEquals(returnedExpenseItem.getEmail(), expenseItem.getEmail());
        Assertions.assertEquals(returnedExpenseItem.getTitle(), expenseItem.getTitle());
        Assertions.assertEquals(returnedExpenseItem.getExpenseDate(), expenseItem.getExpenseDate());
    }


    @Test
    void get_expense_with_empty_id() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = "";
        expenseItem.setId(id);

        //WHEN -THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.getExpenseById(id), "Expense id cannot be blank");
    }

    @Test
    void get_expense_with_invalid_id() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = "asf4d596";
        expenseItem.setId(id);

        //WHEN -THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.getExpenseById(id), "Expense id is invalid");
    }

    /** ------------------------------------------------------------------------
     *  expenseService.getExpensesByEmail
     *  ------------------------------------------------------------------------ **/

    @Test
    void get_expenses_by_email() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        String email = mockNeat.emails().val();
        expenseItem.setId(id);
        expenseItem.setEmail(email);
        expenseItem.setExpenseDate(Instant.now().toString());
        expenseItem.setTitle(mockNeat.strings().val());

        List<ExpenseItem> expenseItemList = Collections.singletonList(expenseItem);

        //WHEN
        when(expenseServiceRepository.getExpensesByEmail(email)).thenReturn(expenseItemList);

        //THEN
        List<ExpenseItem> returnedExpenseList = expenseService.getExpensesByEmail(email);
        assertEquals(returnedExpenseList.size(), 1);
        assertEquals(returnedExpenseList.get(0).getId(), id);
        assertEquals(returnedExpenseList.get(0).getEmail(), email);
    }

    @Test
    void get_expense_by_email_with_blank_email() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        //WHEN-THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.getExpensesByEmail(""), "Email cannot be blank.");
    }

    /** ------------------------------------------------------------------------
     *  expenseService.updateExpense
     *  ------------------------------------------------------------------------ **/

    @Test
    void update_expense(){
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        Double amount = mockNeat.doubles().val();
        String email = mockNeat.emails().val();
        String title = mockNeat.strings().val();
        Expense expense = new Expense(email, title, amount);
        expenseItem.setId(id);
        expenseItem.setExpenseDate(Instant.now().toString());

        when(expenseServiceRepository.getExpenseById(id)).thenReturn(expenseItem);

        //WHEN
        expenseService.updateExpense(id, expense);

        //THEN
        assertEquals(amount, expense.getAmount());
    }
    @Test
    void update_expense_with_null_item_throws_ItemNotFoundException() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        Expense expense = mock(Expense.class);
        String id = UUID.randomUUID().toString();
        System.out.println(id);

        when(expenseServiceRepository.getExpenseById(id)).thenReturn(null);

        //WHEN-THEN
        assertThrows(ItemNotFoundException.class,
                () -> expenseService.updateExpense(id, expense), "Expense does not exist.");

    }

    @Test
    void update_expense_with_empty_id() {
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        //WHEN-THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.updateExpense("", mock(Expense.class)), "Expense id cannot be blank.");

    }

    @Test
    void update_expense_with_invalid_id() {
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        Expense expense = mock(Expense.class);
        String id = "456dsfg";

        //WHEN-THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.updateExpense(id, expense), "Invalid UUID.");
    }

    /** ------------------------------------------------------------------------
     *  expenseService.deleteExpense
     *  ------------------------------------------------------------------------ **/

    // Write additional tests here
    @Test
    void delete_expense_with_empty_id() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        //WHEN-THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.deleteExpense(""), "Expense id cannot be blank.");
    }

    @Test
    void delete_expense_with_invalid_id() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        //WHEN-THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.deleteExpense("arf684"), "Expense id cannot be blank.");
    }


    @Test
    void delete_expense() {

        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        Double amount = mockNeat.doubles().val();
        String email = mockNeat.emails().val();
        String title = mockNeat.strings().val();
        Expense expense = new Expense(email, title, amount);
        expenseItem.setId(id);

        when(expenseItemConverter.convert(expense)).thenReturn(expenseItem);

        //WHEN
        String newlyCreated = expenseService.createExpense(expense);
        expenseService.deleteExpense(newlyCreated);

        //THEN
        assertThrows(ItemNotFoundException.class,
                () -> expenseService.updateExpense(expenseItem.getId(), expense));

    }

    /** ------------------------------------------------------------------------
     *  expenseService.addExpenseItemToList
     *  ------------------------------------------------------------------------ **/


    @Test
    void add_expense_item_to_list() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        expenseItem.setId(id);
        expenseItem.setEmail(mockNeat.emails().val());
        expenseItem.setTitle(mockNeat.strings().val());
        expenseItem.setExpenseDate(Instant.now().toString());

        ExpenseItemList expenseItemList = new ExpenseItemList();
        String listID = UUID.randomUUID().toString();
        expenseItemList.setId(listID);
        expenseItemList.setEmail(expenseItem.getEmail());
        expenseItemList.setExpenseItems(new ArrayList<>());

        when(expenseServiceRepository.getExpenseListById(any())).thenReturn(expenseItemList);
        when(expenseServiceRepository.getExpenseById(any())).thenReturn(expenseItem);
        when(expenseServiceRepository.getExpensesByEmail(any())).thenReturn(expenseItemList.getExpenseItems());

        //WHEN
        expenseService.addExpenseItemToList(expenseItemList.getId(), expenseItem.getId());
        List<ExpenseItem> expenses = expenseService.getExpensesByEmail(expenseItem.getEmail());

        //THEN
        assertTrue(expenses.contains(expenseItem));

    }
    @Test
    void add_existing_expense_item_to_list() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        expenseItem.setId(id);
        expenseItem.setEmail(mockNeat.emails().val());
        List<ExpenseItem> expenseitems = new ArrayList<>();
        expenseitems.add(expenseItem);

        ExpenseItemList expenseItemList = new ExpenseItemList();
        String listID = UUID.randomUUID().toString();
        expenseItemList.setId(listID);
        expenseItemList.setEmail(expenseItem.getEmail());
        expenseItemList.setExpenseItems(expenseitems);

        when(expenseServiceRepository.getExpenseListById(any())).thenReturn(expenseItemList);
        when(expenseServiceRepository.getExpenseById(any())).thenReturn(expenseItem);

        //WHEN
        //THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.addExpenseItemToList(listID, expenseItem.getId()));
    }

    @Test
    void add_expense_item_to_list_with_different_email() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        expenseItem.setId(id);
        expenseItem.setEmail(mockNeat.emails().val());
        List<ExpenseItem> expenseitems = new ArrayList<>();

        ExpenseItemList expenseItemList = new ExpenseItemList();
        String listID = UUID.randomUUID().toString();
        expenseItemList.setId(listID);
        expenseItemList.setEmail(mockNeat.emails().val());
        expenseItemList.setExpenseItems(expenseitems);

        when(expenseServiceRepository.getExpenseListById(any())).thenReturn(expenseItemList);
        when(expenseServiceRepository.getExpenseById(any())).thenReturn(expenseItem);

        //WHEN
        //THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.addExpenseItemToList(expenseItemList.getId(), expenseItem.getId()));
    }

    @Test
    void add_null_expense_item_to_list() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        expenseItem.setId(id);
        expenseItem.setEmail(mockNeat.emails().val());


        ExpenseItemList expenseItemList = new ExpenseItemList();
        String listID = UUID.randomUUID().toString();
        expenseItemList.setId(listID);
        expenseItemList.setEmail(expenseItem.getEmail());

        when(expenseServiceRepository.getExpenseListById(any())).thenReturn(expenseItemList);
        when(expenseServiceRepository.getExpenseById(any())).thenReturn(null);

        //WHEN - THEN
        assertThrows(ItemNotFoundException.class,
                () -> expenseService.addExpenseItemToList(listID, expenseItem.getId()));
    }

    @Test
    void add_expense_item_to_null_list() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        expenseItem.setId(id);
        expenseItem.setEmail(mockNeat.emails().val());


        ExpenseItemList expenseItemList = new ExpenseItemList();
        String listID = UUID.randomUUID().toString();
        expenseItemList.setId(listID);
        expenseItemList.setEmail(expenseItem.getEmail());

        when(expenseServiceRepository.getExpenseListById(any())).thenReturn(null);
        when(expenseServiceRepository.getExpenseById(any())).thenReturn(expenseItem);

        //WHEN - THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.addExpenseItemToList(listID, expenseItem.getId()));
    }

    @Test
    void add_expense_item_to_list_with_empty_expense_id() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        String listID = UUID.randomUUID().toString();

        //WHEN
        //THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.addExpenseItemToList(listID, ""));
    }

    @Test
    void add_expense_item_to_list_with_invalid_expense_id() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        String listID = UUID.randomUUID().toString();

        //WHEN
        //THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.addExpenseItemToList(listID, "asdf"));
    }

    @Test
    void add_expense_item_to_list_with_empty_id() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        String id = UUID.randomUUID().toString();

        //WHEN
        //THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.addExpenseItemToList("", id));
    }

    /** ------------------------------------------------------------------------
     *  expenseService.removeExpenseItemFromList
     *  ------------------------------------------------------------------------ **/

    @Test
    void remove_expense_item_from_list() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        expenseItem.setId(id);
        expenseItem.setEmail(mockNeat.emails().val());
        List<ExpenseItem> expenseitems = new ArrayList<>();
        expenseitems.add(expenseItem);

        ExpenseItemList expenseItemList = new ExpenseItemList();
        String listID = UUID.randomUUID().toString();
        expenseItemList.setId(listID);
        expenseItemList.setEmail(expenseItem.getEmail());
        expenseItemList.setExpenseItems(expenseitems);

        when(expenseServiceRepository.getExpenseById(any())).thenReturn(expenseItem);
        when(expenseServiceRepository.getExpenseListById(any())).thenReturn(expenseItemList);

        //WHEN
        expenseService.removeExpenseItemFromList(listID, expenseItem.getId());
        List<ExpenseItem> expenses = expenseService.getExpensesByEmail(expenseItem.getEmail());

        //THEN
        assertFalse(expenses.contains(expenseItem));
    }

    @Test
    void remove_expense_item_from_list_with_empty_expense_id() {
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        String listID = UUID.randomUUID().toString();

        //WHEN - THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.removeExpenseItemFromList(listID, ""));
    }

    @Test
    void remove_expense_item_from_list_with_empty_id() {
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        String id = UUID.randomUUID().toString();

        //WHEN - THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.removeExpenseItemFromList("", id));
    }

    @Test
    void remove_expense_item_from_list_with_invalid_id() {
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        String id = UUID.randomUUID().toString();

        //WHEN - THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.removeExpenseItemFromList(id, "45126"));
    }

    @Test
    void remove_null_expense_item_from_list() {
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        String id = UUID.randomUUID().toString();
        String listID = UUID.randomUUID().toString();

        when(expenseServiceRepository.getExpenseListById(any())).thenReturn(null);

        //WHEN - THEN
        assertThrows(ItemNotFoundException.class,
                () -> expenseService.removeExpenseItemFromList(listID, id));
    }

    @Test
    void remove_expense_item_from_null_list() {
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        expenseItem.setId(id);

        String listID = UUID.randomUUID().toString();

        when(expenseServiceRepository.getExpenseById(any())).thenReturn(expenseItem);
        when(expenseServiceRepository.getExpenseListById(any())).thenReturn(null);

        //WHEN - THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.removeExpenseItemFromList(listID, expenseItem.getId()));
    }

    @Test
    void remove_expense_item_from_list_with_different_email() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        expenseItem.setId(id);
        expenseItem.setEmail(mockNeat.emails().val());
        List<ExpenseItem> expenseitems = new ArrayList<>();

        ExpenseItemList expenseItemList = new ExpenseItemList();
        String listID = UUID.randomUUID().toString();
        expenseItemList.setId(listID);
        expenseItemList.setEmail(mockNeat.emails().val());
        expenseItemList.setExpenseItems(expenseitems);

        when(expenseServiceRepository.getExpenseListById(any())).thenReturn(expenseItemList);
        when(expenseServiceRepository.getExpenseById(any())).thenReturn(expenseItem);

        //WHEN
        //THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.removeExpenseItemFromList(expenseItemList.getId(), expenseItem.getId()));
    }


    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseListByEmail
     *  ------------------------------------------------------------------------ **/

    @Test
    void get_expense_list_by_email() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        String email = mockNeat.emails().val();
        expenseItem.setId(id);
        expenseItem.setEmail(email);
        expenseItem.setExpenseDate(Instant.now().toString());
        expenseItem.setTitle(mockNeat.strings().val());

        //WHEN
        ExpenseItemList expenseItemList = new ExpenseItemList();
        String expenseListId = mockNeat.strings().val();
        expenseItemList.setEmail(email);
        expenseItemList.setTitle(mockNeat.strings().val());
        expenseItemList.setId(expenseListId);
        expenseItemList.setExpenseItems(Collections.singletonList(expenseItem));
        List<ExpenseItemList> list = Collections.singletonList(expenseItemList);

        when(expenseServiceRepository.getExpenseListsByEmail(anyString())).thenReturn(list);

        //THEN
        List<ExpenseItemList> returnedExpenseList = expenseService.getExpenseListByEmail(email);
        assertEquals(returnedExpenseList.size(), 1);
        assertEquals(returnedExpenseList.get(0).getId(), expenseListId);
        assertEquals(returnedExpenseList.get(0).getEmail(), email);
    }

    @Test
    void get_expense_list_by_email_with_empty_email() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        //WHEN - THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.getExpenseListByEmail(""));

    }


    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseItemListById
     *  ------------------------------------------------------------------------ **/

    @Test
    void get_expense_item_list_by_id() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItemList expenseItemList = new ExpenseItemList();
        String id = UUID.randomUUID().toString();
        expenseItemList.setId(id);
        expenseItemList.setEmail(mockNeat.emails().val());
        expenseItemList.setTitle(mockNeat.strings().val());

        when(expenseServiceRepository.getExpenseListById(any())).thenReturn(expenseItemList);

        //WHEN
        ExpenseItemList expenseItemList2 = expenseService.getExpenseItemListById(expenseItemList.getId());

        //THEN
        assertEquals(expenseItemList, expenseItemList2);
    }

    @Test
    void get_expense_item_list_by_id_with_empty_id() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        //WHEN - THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.getExpenseItemListById(""));
    }

    /** ------------------------------------------------------------------------
     *  expenseService.createExpense
     *  ------------------------------------------------------------------------ **/

    @Test
    void create_expense() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItem expenseItem = new ExpenseItem();
        String id = UUID.randomUUID().toString();
        Double amount = mockNeat.doubles().val();
        String email = mockNeat.emails().val();
        String title = mockNeat.strings().val();
        Expense expense = new Expense(email, title, amount);
        expenseItem.setId(id);
        expenseItem.setExpenseDate(Instant.now().toString());
        expenseItem.setEmail(email);
        expenseItem.setTitle(title);
        expenseItem.setAmount(amount);

        when(expenseItemConverter.convert(any())).thenReturn(expenseItem);
        when(expenseServiceRepository.getExpenseById(any())).thenReturn(expenseItem);

        //WHEN
        expenseService.createExpense(expense);

        //THEN
        assertEquals(expenseItem, expenseService.getExpenseById(id));
    }

    @Test
    void create_expense_with_null_expense() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        //WHEN - THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.createExpense(null));
    }


    @Test
    void create_expense_list() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        ExpenseItemList expenseItemlist = new ExpenseItemList();
        String id = UUID.randomUUID().toString();
        String email = mockNeat.emails().val();
        String title = mockNeat.strings().val();
        expenseItemlist.setId(id);
        expenseItemlist.setEmail(email);
        expenseItemlist.setTitle(title);
        List<ExpenseItemList> expenseItemsList = new ArrayList<ExpenseItemList>();
        expenseItemsList.add(expenseItemlist);

        when(expenseServiceRepository.getExpenseListById(any())).thenReturn(expenseItemlist);

        //WHEN
        String expenseListID = expenseService.createExpenseList(email, title);

        ExpenseItemList expenseItemList2 = expenseService.getExpenseItemListById(id);

        //THEN
        assertEquals(expenseItemlist, expenseItemList2);
    }

    @Test
    void create_expense_list_with_invalid_email() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        String email = "";
        String title = mockNeat.strings().val();

        //WHEN
        //THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.createExpenseList(email, title));
    }



    @Test
    void create_expense_list_with_invalid_title() {
        //GIVEN
        ExpenseServiceRepository expenseServiceRepository = mock(ExpenseServiceRepository.class);
        ExpenseItemConverter expenseItemConverter = mock(ExpenseItemConverter.class);
        ExpenseService expenseService = new ExpenseService(expenseServiceRepository, expenseItemConverter);

        String email = mockNeat.emails().val();;
        String title = null;

        //WHEN
        //THEN
        assertThrows(InvalidDataException.class,
                () -> expenseService.createExpenseList(email, title));
    }

}