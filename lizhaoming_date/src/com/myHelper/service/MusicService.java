package com.myHelper.service;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.widget.Toast;

import com.myHelper.R;
import com.myHelper.course.MainActivity;
import com.myHelper.myAlarmManager.AlarmAlert;
import com.myHelper.sch_activity.ScheduleAll;
import com.myHelper.sch_activity.ScheduleInfoView;

@SuppressLint("NewApi")
public class MusicService extends Service{
	public int scheduleID=0;
	private MediaPlayer mp;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}
	public void onCreate(){

		mp=new MediaPlayer();
		try {
			mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
			mp.prepare();
		} catch (IllegalArgumentException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		super.onCreate();
	}
	@Override
	public void onStart(Intent intent,int startId){
		mp.start();
		scheduleID=intent.getIntExtra("ID", 0);
		//Toast.makeText(getApplicationContext(), scheduleID+"aaa",0).show();
		Intent intent2=new Intent(this,AlarmAlert.class);
		intent2.putExtra("id", intent.getIntExtra("ID",0));
		intent2.putExtra("title", intent.getStringExtra("TITLE"));
		Toast.makeText(this, intent.getStringExtra("TITLE"), 0).show();
		intent2.putExtra("content", intent.getStringExtra("CONTENT"));
		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent2);
	}
	@Override
	public void onDestroy(){
		mp.stop();
		mp.release();
		String[] scheduleIDs=new String[]{Integer.toString(scheduleID)};
		//Toast.makeText(getApplicationContext(), scheduleID+"a",0).show();
		Intent intent1=new Intent();
		intent1.putExtra("scheduleID", scheduleIDs);
		intent1.setClass(this, ScheduleInfoView.class);
		intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent1);
		super.onDestroy();
	}

}
