package com.myHelper.myAlarmManager;

import java.io.IOException;

import com.myHelper.service.MusicService;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{
	MediaPlayer mediaPlayer=new MediaPlayer();
	String content="";
	int id=0;
	int scheduleID=0;
	String title="";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根

		id=intent.getIntExtra("_id", 0);
		title=intent.getStringExtra("title");
		content=intent.getStringExtra("content");
		//Toast.makeText(context, title, 0).show();
		Intent intent2=new Intent();
		intent2.putExtra("ID", id);
		intent2.putExtra("TITLE", title);
		intent2.putExtra("CONTENT", content);
		intent2.setClass(context, MusicService.class);
		//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(intent2);
		
	
	}

}
