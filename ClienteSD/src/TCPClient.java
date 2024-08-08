import java.net.*;
import java.io.*;


public class TCPClient {
	private DataInputStream in;
	private DataOutputStream out;
	
    public void connectToServer (String hostName, int serverPort) {
        // arguments supply message and hostname of destination
        Socket s = null;
        try{
            s = new Socket(hostName, serverPort);
            this.in = new DataInputStream( s.getInputStream());
            this.out = new DataOutputStream( s.getOutputStream());
            ReceiveMessage receiveMessage = new ReceiveMessage(this.in);
            receiveMessage.start();
        }catch (UnknownHostException e){System.out.println("Sock:"+e.getMessage());
        }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
        }catch (IOException e){System.out.println("IO:"+e.getMessage());}
      
    }

	public DataInputStream getIn() {
		return in;
	}

	public DataOutputStream getOut() {
		return out;
	}
}