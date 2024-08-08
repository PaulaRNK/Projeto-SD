import java.io.DataInputStream;
import java.io.IOException;

public class ReceiveMessage extends Thread {
    DataInputStream in;
    
    public ReceiveMessage(DataInputStream in){
        this.in = in;
    }

    public void run() {        
    	while (true) {
            try {
                TCPClientGUI.setResponse(in.readUTF());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}