package ata.unit.three.project.expense.lambda;

public class ResponseBody {
        private String expenseListId;
        private String expenseItemId;

        public ResponseBody(String expenseListId, String expenseItemId) {
            this.expenseListId = expenseListId;
            this.expenseItemId = expenseItemId;
        }

        public String getExpenseListId() {
            return expenseListId;
        }

        public void setExpenseListId(String expenseListId) {
            this.expenseListId = expenseListId;
        }

        public String getExpenseItemId() {
            return expenseItemId;
        }

        public void setExpenseItemId(String expenseItemId) {
            this.expenseItemId = expenseItemId;
        }
}

