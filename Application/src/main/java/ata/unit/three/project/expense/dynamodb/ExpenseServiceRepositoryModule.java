package ata.unit.three.project.expense.dynamodb;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ExpenseServiceRepositoryModule {

    @Singleton
    @Provides
    public ExpenseServiceRepository provideExpenseServiceRepository() {
        return new ExpenseServiceRepository();
    }
}
