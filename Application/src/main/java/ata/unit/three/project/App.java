package ata.unit.three.project;

import ata.unit.three.project.expense.dynamodb.ExpenseItem;
import ata.unit.three.project.expense.dynamodb.ExpenseServiceRepository;
import ata.unit.three.project.expense.service.ExpenseService;
import ata.unit.three.project.expense.service.model.ExpenseItemConverter;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class App {

    @Singleton
    @Provides
    public ExpenseServiceRepository provideExpenseServiceRepository() {
        return new ExpenseServiceRepository();
    }

    @Singleton
    @Provides
    public ExpenseItemConverter provideExpenseItemConverter() {
        return new ExpenseItemConverter();
    }
}
