package Client;

import Client.Commands.Command;

import java.util.HashMap;
import java.util.Scanner;

public class Commander {
    private static Commander instance;
    private Scanner scanner;
    public HashMap<String,Command> cmds;

    public static Commander getInstance(){
        if(instance != null) {
            instance = new Commander();
        }

        return instance;
    }
    public void setup(){
        scanner = new Scanner(System.in);
    }

    public void listen(){
        while (true){

        }
    }

    public static void CommanderPrint(String msg){
        System.out.println("- " + msg);
    }

}
