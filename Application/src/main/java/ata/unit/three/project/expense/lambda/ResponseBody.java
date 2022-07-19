package ata.unit.three.project.expense.lambda;

public class ResponseBody {
        private String expenseListId;
        private String expenseItemId;
        private String email;
        private String title;

        public ResponseBody(String expenseListId, String expenseItemId, String email, String title) {
            this.expenseListId = expenseListId;
            this.expenseItemId = expenseItemId;
            this.email = email;
            this.title = title;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
}

