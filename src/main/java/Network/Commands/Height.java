package Network.Commands;

import java.io.Serializable;

public class Height implements Serializable {
    private int height ;
    public Height(int height){
        this.height = height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public int getHeight(){
        return height;
    }
}
