package Client.Commands;

import Client.Commander;

public class HelpCommand implements Command{
    @Override
    public String getHelp() {
        return "Displays help for all known commands. \n";
    }

    @Override
    public String[] getParams() {
        return new String[0];
    }

    @Override
    public void run(String[] args) {
        for( String key : Commander.getInstance().cmds.keySet() ){
            Commander.CommanderPrint(Commander.getInstance().cmds.get(key).getHelp());
        }
    }
}
