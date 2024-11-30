package software_design.model;

public class ConsolidatedOrder {
    private MenuItem item;
        private int quantity;
        private String option;
        private String remark;
        private String status;

        public ConsolidatedOrder(MenuItem item, int quantity, String option, String remark, String status) {
            this.item = item;
            this.quantity = quantity;
            this.option = option;
            this.remark = remark;
            this.status = status;
        }

        public MenuItem getItem() {
            return item;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getOption() {
            return option;
        }

        public String getRemark() {
            return remark;
        }

        public String getStatus() {
            return status;
        }
}

