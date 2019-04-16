package commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CommandDirectorTest {

    Map<CommandName, Command> commandMap;
    String name;
    CommandName commandName;
    private java.util.List<CommandName> commandNameList;

    @Before
    public void setUp() throws Exception {
        name = "PASS";
        commandMap = new HashMap<>();
        commandMap.put(CommandName.PASS, new Pass());
        commandName = CommandName.valueOf(name);
        commandNameList = new ArrayList<>();
        commandNameList.add(CommandName.USER);
        commandNameList.add(CommandName.PASS);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isNotInComboBox() {
        assertEquals(true, commandNameList.contains(commandName));

    }

    @Test
    public void getCommand() {
        String expected = "commands.Pass@28c97a5";
      //  assertEquals("commands.Pass@28c97a5", commandMap.get(commandName));
    }
}
