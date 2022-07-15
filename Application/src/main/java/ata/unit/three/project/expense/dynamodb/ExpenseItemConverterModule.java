package ata.unit.three.project.expense.dynamodb;

import ata.unit.three.project.expense.service.model.ExpenseItemConverter;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ExpenseItemConverterModule {

    @Singleton
    @Provides
    public ExpenseItemConverter provideExpenseItemConverter() {
        return new ExpenseItemConverter();
    }
}
