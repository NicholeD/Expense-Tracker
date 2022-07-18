package ata.unit.three.project.expense.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.kenzie.ata.ExcludeFromJacocoGeneratedReport;

import java.util.Comparator;
import java.util.Objects;

@ExcludeFromJacocoGeneratedReport
@DynamoDBTable(tableName = "Expense")
public class ExpenseItem {

    private String id;
    private String email;
    private String expenseDate;
    private String title;
    private Double amount;

    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return this.id;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "EmailIndex", attributeName = "Email")
    public String getEmail() {
        return this.email;
    }

    @DynamoDBAttribute(attributeName = "ExpenseDate")
    public String getExpenseDate() {
        return this.expenseDate;
    }

    @DynamoDBAttribute(attributeName = "Title")
    public String getTitle() {
        return this.title;
    }

    @DynamoDBAttribute(attributeName = "Amount")
    public Double getAmount() {
        return this.amount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o==null || getClass() != o.getClass()) return false;
//
//        ExpenseItem that = (ExpenseItem) o;
//        return id.equals(this.id) && email.equals(that.email);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, email);
//    }


//    @Override
//    public int compare(ExpenseItem o1, ExpenseItem o2) {
//        return o1.getExpenseDate().compareTo(o2.getExpenseDate());
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpenseItem)) return false;
        ExpenseItem that = (ExpenseItem) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getExpenseDate(), that.getExpenseDate()) && Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getAmount(), that.getAmount());
    }

//    @Override
//    public Comparator<ExpenseItem> reversed() {
//        return Comparator.super.reversed();
//    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getExpenseDate(), getTitle(), getAmount());
    }
}
