import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


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
		else if(comando.contains("send -all")){
			String mensagem = comando.substring(9);
			
			String emitente = null;
			for (User u : Servidor.users) {
				if(u.getSocket() == socket){
					emitente = u.getNome();
				}
			}
			
			broadcast(mensagem, emitente);
			return "";
		}
		else if(comando.contains("send -user")){
			String temp = comando.substring(11);
			String user = temp.substring(0, temp.indexOf(" "));
			String msg = temp.substring(temp.indexOf(" "));
			
			User remetente = null;
			for (User u : Servidor.users) {
				if (u.getNome().equals(user)) {
					remetente = u;
				}
			}
			
			String emitente = null;
			for (User u : Servidor.users) {
				if (u.getSocket() == socket) {
					emitente = u.getNome();
				}
			}
			
			sendTo(remetente, emitente, msg);
			return("");
		}
		else{
			return "comando não reconhecido";
		}
	}
	
	public void broadcast(String msg, String emitente){
		for (User u : Servidor.users) {
			try {
				String horaFormatada = retornaHora();
				String dataFormatada = retornaData();
				
				DataOutputStream o = new DataOutputStream(u.getSocket().getOutputStream());
				o.writeUTF(socket.getInetAddress() + ":" + socket.getPort() + "/~" + emitente + ":" + msg + " " + horaFormatada + " " + dataFormatada);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendTo(User remetente, String emitente, String msg){
		String dataFormatada = retornaData();
		String horaFormatada = retornaHora();
		
		try {
			DataOutputStream o = new DataOutputStream(remetente.getSocket().getOutputStream());
			o.writeUTF(socket.getInetAddress() + ":" + socket.getPort() + "/~" + emitente + ":" + msg + " " + horaFormatada + " " + dataFormatada);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String retornaHora(){
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat hora = new SimpleDateFormat("HH");
		SimpleDateFormat minuto = new SimpleDateFormat("mm");
		Date date = new Date();
		calendar.setTime(date);
		return hora.format(calendar.getTime()) + "h" + minuto.format(calendar.getTime());
	}
	
	public String retornaData(){
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		calendar.setTime(date);
		return df.format(calendar.getTime());
	}
}
