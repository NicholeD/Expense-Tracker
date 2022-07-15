package ata.unit.three.project.expense.service;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component
public interface ExpenseServiceComponent {
    ExpenseService provideExpenseService();
}
