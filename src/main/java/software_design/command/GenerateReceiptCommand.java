package software_design.command;
import software_design.controller.AdminController;

public class GenerateReceiptCommand implements AdminPageCommand {
    private AdminController receiver;

    public GenerateReceiptCommand(AdminController receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.handleGenerateReceipt();
    }
}
