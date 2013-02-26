package live.hz.lanchat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import live.hz.lanchat.R;

public class Client extends Activity {

	private Button send;
	private TextView view;
	private EditText edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client);

		send = (Button) findViewById(R.id.send);
		view = (TextView) findViewById(R.id.view);
		edit = (EditText) findViewById(R.id.edit);

		send.setOnClickListener(new MyListener());

	}

	class MyListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("on click.." + v.getId());
			new Thread() {
				public void run() {
					try {
						Socket socket = new Socket("172.26.14.209", 8889);
						OutputStream output = socket.getOutputStream();
						output.write("client to server".getBytes());
						
						// InputStream in = socket.getInputStream();
						// byte[] buffer = new byte[in.available()];
						// in.read(buffer);
						// String msg = new String(buffer) + "client"
						// + edit.getText().toString();
						

					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("UnknownHostException");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("IOException");
					}
				}
			}.start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}
