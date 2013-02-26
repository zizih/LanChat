package live.hz.lanchat.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class SocketHelper {

	private static Socket socket;
	private static List<Socket> list;
	private static DataInputStream reader = null;
	private static DataOutputStream out = null;

	public SocketHelper() {

	}

	public static boolean getSocketConnect(String addr,int port) {
		try {
			socket = new Socket(addr,port);
			list.add(socket);
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean sendRecord(File soundFile) {
		try {
			reader = new DataInputStream(new BufferedInputStream(
					new FileInputStream(soundFile)));
			out = new DataOutputStream(socket.getOutputStream());
			byte[] buf = new byte[512];
			int read = 0;
			while((read=reader.read(buf))!=-1){
				out.write(buf,0,read);
			}
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
