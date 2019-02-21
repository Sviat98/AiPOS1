package commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandDirector {
    private Map<CommandName,Command> commandMap;
    private java.util.List<CommandName> commandNameList;

    public CommandDirector() {
        commandMap = new HashMap<>();
        commandMap.put(CommandName.USER,new User());
        commandMap.put(CommandName.PASS,new Pass());
        commandMap.put(CommandName.NOOP,new Noop());
        commandMap.put(CommandName.LIST,new List());
        commandMap.put(CommandName.QUIT,new Quit());
        commandMap.put(CommandName.DELE,new Dele());
        commandMap.put(CommandName.RSET,new Rset());
        commandMap.put(CommandName.STAT,new Stat());
        commandMap.put(CommandName.RETR, new Retr());
        commandMap.put(CommandName.TOP, new Top());
        commandMap.put(CommandName.UIDL, new Uidl());
        commandMap.put(CommandName.MAILBOX,new Mailbox());

        commandNameList = new ArrayList<>();
        commandNameList.add(CommandName.USER);
        commandNameList.add(CommandName.PASS);
        commandNameList.add(CommandName.QUIT);
        commandNameList.add(CommandName.MAILBOX);

    }

    public boolean isNotInComboBox (CommandName commandName){
        return commandNameList.contains(commandName);
    }

    public Command getCommand(String name){
        CommandName commandName = CommandName.valueOf(name);
        return commandMap.get(commandName);
    }
}
