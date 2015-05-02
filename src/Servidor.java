import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Servidor {
	public static ArrayList<User> users = new ArrayList<User>();
	
	public static void main(String[] args) {		
		try {
			ServerSocket server = new ServerSocket(2130);
			
			while(true){
				Socket socket = server.accept();
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				String nome = in.readUTF();
				User u = new User(socket, nome);
				users.add(u);
				
				ThreadCliente tc = new ThreadCliente(socket, in, out);
				tc.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<User> getUsers() {
		return users;
	}
}
