package Client.Commands;

public interface  Command {

    public String getHelp();
    public String[] getParams();
    public void run(String[] args);
}
