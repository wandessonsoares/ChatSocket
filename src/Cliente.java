import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Cliente {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		try {
			Socket socket = new Socket("localhost", 2130);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(socket.getInputStream());
			out.writeUTF("João");
			
			ThreadLeitura tl = new ThreadLeitura(socket, in);
			tl.start();
			
			ThreadEscrita te = new ThreadEscrita(socket, out);
			te.start();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
