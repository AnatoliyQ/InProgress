package Client;

import Client.Commands.Command;
import Client.Commands.HelpCommand;
import Client.Commands.WalletCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Commander {
    private static Commander instance;
    private Scanner scanner;
    public HashMap<String,Command> cmds;

    public static Commander getInstance(){
        if(instance == null) {
            instance = new Commander();
        }

        return instance;
    }

    private Commander(){
        setup();
        //instance = this;
    }

    private void setup(){
        cmds = new HashMap<>();
        cmds.put("help", new HelpCommand());
        cmds.put("wallet", new WalletCommand());
        scanner = new Scanner(System.in);
    }

    private void call(String[] rawArgs){

        String function = rawArgs[0];
        String[] args = Arrays.copyOfRange(rawArgs, 1,rawArgs.length);

        Command command = cmds.get(function);
        if(command == null){
            CommanderPrint("command function: '" + function +"' not found. Type help for a list of functions");
        }else{
            command.run(args);
        }

    }

    public void listen(){
        CommanderPrint("Welcome to console interface. Waiting inputs commands. Set help for help");
        while (true){


            String input = (String) scanner.nextLine();
            if(input.equals("quit") || input.equals("exit")){
                break;
            }

            String[] rawArgs = input.split("\\s+");

            if(!rawArgs[0].equals("")){
                /* Check if debug mode is false, if so then run command in try catch to aviod crashing the application if an error is thrown */
                    try{
                        call(rawArgs);
                    }catch(ArrayIndexOutOfBoundsException e){
                        CommanderPrint("command couldn't execute, perhaps not enough arguments? try: "+ rawArgs[0] + " -help");
                    }catch(Exception e){
                        CommanderPrint("command failed to execute.");
                    }

            }


        }
    }

    public static void CommanderPrint(String msg){
        System.out.println("- " + msg);
    }

    public static void main(String[] args) {
        Commander commander = Commander.getInstance();
        commander.listen();
    }

}
