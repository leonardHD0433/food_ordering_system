package software_design.command;
import software_design.controller.AdminController;

public class ProcessOrderCommand implements AdminPageCommand {
    private AdminController receiver;

    public ProcessOrderCommand(AdminController receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.handleProcessOrder();
    }
}