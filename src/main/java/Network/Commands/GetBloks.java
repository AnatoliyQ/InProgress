package Network.Commands;

import java.io.Serializable;

public class GetBloks implements Serializable{
    private int from;
    public GetBloks(int from){
        this.from = from;
    }

    public int getFrom(){
        return from;
    }
}
