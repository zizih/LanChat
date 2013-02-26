package live.hz.lanchat;

import java.io.File;
import java.io.IOException;

import android.graphics.Color;
import live.hz.lanchat.util.SocketHelper;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Record extends Activity implements OnClickListener {

	Button btn_record_start;
	Button btn_record_stop;
	Button btn_record_play;
	Button btn_record_send;

	MediaRecorder mRecorder;
	MediaPlayer mPlayer;
	File soundFile;
	File tmpFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record);

		btn_record_start = (Button) findViewById(R.id.btn_record_start);
		btn_record_stop = (Button) findViewById(R.id.btn_record_stop);
		btn_record_play = (Button) findViewById(R.id.btn_record_play);
		btn_record_send = (Button) findViewById(R.id.btn_record_send);

		btn_record_start.setOnClickListener(this);
		btn_record_stop.setOnClickListener(this);
		btn_record_play.setOnClickListener(this);
		btn_record_send.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		System.out.println("onclick==>>" + v.getId());
		switch (v.getId()) {
		case R.id.btn_record_start:
			onRecordStart();
			break;
		case R.id.btn_record_stop:
			onRecordStop();
			break;
		case R.id.btn_record_play:
			onRecordPlay();
			break;
		case R.id.btn_record_send:
			onRecordSend();
			break;

		default:
			break;
		}
	}

	private void onRecordStart() {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			Toast.makeText(Record.this, "please insert SD card",
					Toast.LENGTH_SHORT).show();
		}
		try {
			tmpFile = new File(Environment.getExternalStorageDirectory()
					.getCanonicalFile()
					+ File.separator
					+ "InterphoneRecordCache");
			if (!tmpFile.exists()) {
				tmpFile.mkdirs();
			}

			soundFile = File.createTempFile("record", ".amr", tmpFile);
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(soundFile.getAbsolutePath());
			mRecorder.prepare();
			mRecorder.start();

			btn_record_start.setEnabled(false);
			btn_record_stop.setEnabled(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void onRecordStop() {
		if (soundFile != null && soundFile.exists()) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
			btn_record_stop.setEnabled(false);
			btn_record_play.setEnabled(true);
		}
	}

	private void onRecordPlay() {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(soundFile.getAbsolutePath());
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					Toast.makeText(Record.this, "finished play",
							Toast.LENGTH_SHORT).show();
				}
			});
			btn_record_play.setEnabled(false);
			btn_record_send.setEnabled(true);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void onRecordSend() {
		SocketHelper.getSocketConnect("172.26.13.107", 8889);
		SocketHelper.sendRecord(soundFile);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (soundFile != null && soundFile.exists()) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}

}
