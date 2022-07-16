package ata.unit.three.project.expense.service;

import ata.unit.three.project.App;
<<<<<<< HEAD

import ata.unit.three.project.expense.dynamodb.ExpenseItemConverterModule;
import ata.unit.three.project.expense.dynamodb.ExpenseServiceRepository;
import ata.unit.three.project.expense.dynamodb.ExpenseServiceRepositoryModule;
=======
import ata.unit.three.project.expense.dynamodb.ExpenseServiceRepository;
>>>>>>> b0aae60 (Added dagger expenseService calls to endpoints. Tests pass. Stubbed code for Add/Remove ExpenseItem from List methods.)
import ata.unit.three.project.expense.service.model.ExpenseItemConverter;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {App.class, ExpenseServiceRepositoryModule.class, ExpenseItemConverterModule.class})
public interface ExpenseServiceComponent {
    ExpenseService expenseService();
    ExpenseServiceRepository provideExpenseServiceRepository();
    ExpenseItemConverter provideExpenseItemConverter();
}
