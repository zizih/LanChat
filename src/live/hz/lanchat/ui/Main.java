package live.hz.lanchat.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import live.hz.lanchat.R;

public class Main extends Activity implements OnClickListener{

    private int SETTING = 001;
    private int HELP = 002;
    private int EXIT = 003;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

       /* try{
            Process process = Runtime.getRuntime().exec("ping " + "10.50.9.27" + " -w 100 -n 1");
            BufferedReader bf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while(true){
                String a = bf.readLine();
                System.out.println("test:  "+a);
            }
            //process.destroy();
        }catch(IOException e){}*/
        System.out.println("Main Thread: "+Runtime.getRuntime());
        new UdpClient(Main.this).start();


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
        menu.add(0, SETTING, 0, "settings");
        menu.add(0, HELP, 0, "help");
        menu.add(0, EXIT, 0, "exit");
        return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

    class UdpClient extends Thread{

        Activity ctx;

        UdpClient(Activity ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            super.run();    //To change body of overridden methods use File | Settings | File Templates.
            send();
        }

        public void fun(){
            int i=0;
            System.out.println(i++ +"UDP Thread: "+Thread.currentThread().getName());

            int rLeng = 1024;
            String multicastIp = "224.0.0.1";
            int mPort = 8899;

            try{
                InetAddress mCastAddr = InetAddress.getByName(multicastIp);

                System.out.println("mCastAddr.isMulticastAddress()"+mCastAddr.isMulticastAddress());
                if(!mCastAddr.isMulticastAddress()){
                    throw new Exception("please use multicast ip");
                }

                MulticastSocket rSocket = new MulticastSocket(mPort);
                rSocket.joinGroup(mCastAddr);

                DatagramPacket rPacket = new DatagramPacket(new byte[rLeng],rLeng);

                while(true){

                    rSocket.receive(rPacket);

                    System.out.println((new String(rPacket.getData())).trim());

                    rSocket.close();

                }


            }catch(UnknownHostException excep){
                System.out.println("error1");
            }
            catch(Exception e){
                System.out.println(e);
                System.out.println("error2");
            }


        }
        //end of fun


        public void send(){

            int TIMEOUT = 1000;
            byte[] data = "client string test".getBytes();
            int port = 8899;
            String servera = "192.168.0.101";


            try{
                // 1. 构造UDP DatagramSocket对象
                DatagramSocket client = new DatagramSocket();

                // 2。指定timeout时间，防止进入无限等待状态
                client.setSoTimeout(TIMEOUT);

                // 3. 构造收发的报文对象
                InetAddress ineta = InetAddress.getByName(servera);
                DatagramPacket sPacket = new DatagramPacket(data,data.length,ineta,port);

                byte[] buf = new byte[100];
                DatagramPacket rPacket = new DatagramPacket(buf,buf.length);

                // 4.指定尝试的次数
                int MAXTRIES = 5;
                int tries = 0;
                boolean hasResponse = false;

                do {
                    try{
                        client.send(sPacket);

                        try{
                            client.receive(rPacket);

                            System.out.println(tries + " Client Receive: "+new String(buf,0,rPacket.getLength()));

                            if(!rPacket.getAddress().equals(servera)){
                                throw new IOException("Received packet from an unknown source");
                            };

                            hasResponse = true;

                        }catch(IOException ioException){
                            System.out.println("Timed out, " + (MAXTRIES - tries) + "");
                            tries += 1;
                        }
                    }catch(IOException io){
                        System.out.println("Fail to Send.");
                    }

                }while((!hasResponse)&&(tries<MAXTRIES));


                if(hasResponse){
                    System.out.println("Receive: "+rPacket.getData());
                }else{
                    System.out.println("No Response, Give up");
                }
                client.close();

            }catch(SocketException e){

            }catch(UnknownHostException unknownHostException){

            }



        }


    }

}
