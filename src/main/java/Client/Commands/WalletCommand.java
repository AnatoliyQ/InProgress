package Client.Commands;

import Client.Commander;
import Network.Node;
import Util.Address;
import Util.AddressFormatException;
import Util.StringUtil;

import java.util.Arrays;

public class WalletCommand implements Command {
    @Override
    public String getHelp() {
        return "cmd: wallet \n" +
                "- usage: wallet param \n"+
                "- param: 'getaddress', 'getbalance', 'sendtoaddress', 'info', 'help' \n";

    }

    @Override
    public String[] getParams() {
        return new String[]{ "help", "getaddress", "getbalance", "sendtoaddress", "info" };
    }

    @Override
    public void run(String[] args) {
        if( !Arrays.asList(getParams()).contains(args[0]) ){
            Commander.CommanderPrint("ERROR ! unknown parameters ");
            Commander.CommanderPrint(Arrays.toString(getParams()));
            return;
        }

        switch (args[0]) {
            case "getaddress":
                String address = Node.getInstance().myWallet.getOwnAddress();
                Commander.CommanderPrint(address);
                break;
            case "getbalance":
                float balance = Node.getInstance().myWallet.getBalance();
                Commander.CommanderPrint(Float.toString(balance));
                break;
            case "info":
                Commander.CommanderPrint("Not implemented yet");
                break;
            case "help":
                Commander.CommanderPrint(getHelp());
                break;
            case "sendtoaddress":
                String recipient = args[1];
                String value = args[2];
                try {
                    Node.getInstance().myWallet.sendFunds(Address.getKeyFromAddress(recipient), Float.parseFloat(value) );
                } catch (AddressFormatException e) {
                    Commander.CommanderPrint("Error! Incorrect address.");
                }
                break;
        }
    }
}
