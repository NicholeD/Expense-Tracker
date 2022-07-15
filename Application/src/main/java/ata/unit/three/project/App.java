package ata.unit.three.project;

import ata.unit.three.project.expense.dynamodb.ExpenseItem;
import ata.unit.three.project.expense.dynamodb.ExpenseServiceRepository;
import ata.unit.three.project.expense.service.ExpenseService;
import ata.unit.three.project.expense.service.model.ExpenseItemConverter;
import dagger.Module;
import dagger.Provides;

@Module
public class App {
    @Provides
    public static ExpenseService expenseService(ExpenseServiceRepository expenseServiceRepository,
                                                ExpenseItemConverter expenseItemConverter) {
        return new ExpenseService(expenseServiceRepository, expenseItemConverter);
    }
}
