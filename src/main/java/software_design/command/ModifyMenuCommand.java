package software_design.command;
import software_design.controller.AdminController;

//Start extracting requests into concrete command classes that implement the command interface
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



