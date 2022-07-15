package ata.unit.three.project.expense.service;

import ata.unit.three.project.App;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {App.class})
public interface ExpenseServiceComponent {
    ExpenseService provideExpenseService();
}
