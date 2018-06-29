package Client.Commands;

import Client.Commander;
import Network.Node;

import java.util.Arrays;

public class MiningCommand implements Command {
    @Override
    public String getHelp() {
        return "cmd: mining \n" +
                "- usage: mining param \n"+
                "- param: 'setgenerate', 'stopmining', 'help' \n";
    }

    @Override
    public String[] getParams() {
        return new String[]{ "help", "setgenerate", "stopmining"};
    }

    @Override
    public void run(String[] args) {
        if( !Arrays.asList(getParams()).contains(args[0]) ){
            Commander.CommanderPrint("ERROR ! unknown parameters ");
            Commander.CommanderPrint(Arrays.toString(getParams()));
            return;
        }

        switch (args[0]) {
            case "setgenerate":
                Node.getInstance().startMining();
                break;
            case "stopmining":
                Node.getInstance().stopMining();
                break;
            case "help":
                Commander.CommanderPrint(getHelp());
                break;
        }



    }
}
