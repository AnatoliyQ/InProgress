package Client.Commands;

import Client.Commander;
import Network.Node;

import java.util.Arrays;

public class WalletCommand implements Command {
    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String[] getParams() {
        return new String[]{ "-help", "-params", "getaddress", "getbalance", "info" };
    }

    @Override
    public void run(String[] args) {
        if( !Arrays.asList(getParams()).contains(args[0]) ){
            Commander.CommanderPrint("ERROR ! unknown parameters ");
            Commander.CommanderPrint(Arrays.toString(getParams()));
            return;
        }

        if(args[0].equals("getaddress")){
           String address =  Node.getInstance().myWallet.getOwnAddress();
           Commander.CommanderPrint(address);
        } else if(args[0].equals("getbalance")){
            float balance = Node.getInstance().myWallet.getBalance();
            Commander.CommanderPrint(Float.toString(balance));
        }
    }
}
