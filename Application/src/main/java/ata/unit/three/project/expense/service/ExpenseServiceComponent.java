package ata.unit.three.project.expense.service;

import ata.unit.three.project.App;

import ata.unit.three.project.expense.dynamodb.ExpenseItemConverterModule;
import ata.unit.three.project.expense.dynamodb.ExpenseServiceRepository;
import ata.unit.three.project.expense.dynamodb.ExpenseServiceRepositoryModule;
import ata.unit.three.project.expense.service.model.ExpenseItemConverter;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {App.class, ExpenseServiceRepositoryModule.class, ExpenseItemConverterModule.class})
public interface ExpenseServiceComponent {
    ExpenseService provideExpenseService();
}
