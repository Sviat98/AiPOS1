package commands;

import java.util.HashMap;
import java.util.Map;

public class CommandDirector {
    private Map<CommandName,Command> commandMap;

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

    }

    public Command getCommand(String name){
        CommandName commandName = CommandName.valueOf(name);
        return commandMap.get(commandName);
    }
}
