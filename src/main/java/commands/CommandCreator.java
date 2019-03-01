package commands;

public class CommandCreator {
    public static String createCommand(CommandName name,String parameters){
        StringBuilder fullCommand = new StringBuilder();
        fullCommand.append(name.toString());
        if(!parameters.isEmpty()){
            fullCommand.append(" ");
            fullCommand.append(parameters);
            fullCommand.append("\n");
        }

        return fullCommand.toString();
    }

    public static String createCommand(CommandName name){
        return name.toString()+"\n";
    }
}
