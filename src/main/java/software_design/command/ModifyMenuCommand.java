package software_design.command;
import software_design.controller.AdminController;

public class ModifyMenuCommand implements AdminPageCommand {
    private AdminController receiver;

    public ModifyMenuCommand(AdminController receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.handleModifyMenu();
    }
}



