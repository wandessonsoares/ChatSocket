import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class ThreadCliente extends Thread{
	Socket socket;
	DataInputStream in;
	DataOutputStream out;
	String comando;

	public ThreadCliente(Socket socket, DataInputStream in, DataOutputStream out){
		this.socket = socket;
		this.in = in;
		this.out = out;
	}
	
	public void run(){
		while(true){
			try {
				comando = in.readUTF();
				String resposta = interpretador(comando);
				out.writeUTF(resposta);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String interpretador(String comando){
		if(comando.equals("list")){
			StringBuffer usuariosOnline = new StringBuffer();
			for (User u : Servidor.users) {
				usuariosOnline.append("\n-" + u.getNome());
			}
			return "Usuarios online: " + usuariosOnline;
		}
		if(comando.contains("all")){
			String mensagem = comando.substring(9);
			broadcast(mensagem);
			return "";
		}
		else{
			return "comando não reconhecido";
		}
	}
	
	public void broadcast(String msg){
		for (User u : Servidor.users) {
			try {
				DataOutputStream o = new DataOutputStream(u.getSocket().getOutputStream());
				o.writeUTF(u.getSocket().getInetAddress() + "/~" + u.getNome() + msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
