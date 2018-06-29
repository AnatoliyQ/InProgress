import Client.Commander;
import Client.Commands.Command;
import Network.Node;

public class Runner {
    public static void main(String[] args) {
        Node currentNode = Node.getInstance();
        Commander commander = Commander.getInstance();
        commander.listen();
    }
}
