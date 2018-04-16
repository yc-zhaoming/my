package com.myHelper.myAlarmManager;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.widget.Toast;

import com.myHelper.R;
import com.myHelper.sch_activity.ScheduleInfoView;
import com.myHelper.service.MusicService;
import com.myHelper.vo.ScheduleVO;

public class AlarmAlert extends Activity {

	ScheduleVO scheduleVO;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		//Toast.makeText(getApplicationContext(), "闹钟响了", 0).show();;
		new AlertDialog.Builder(AlarmAlert.this)
				.setIcon(R.drawable.clock)
				.setTitle(intent.getStringExtra("title"))
				.setMessage(intent.getStringExtra("content"))
				.setPositiveButton("我知道了",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {						
								stopService(new Intent(AlarmAlert.this,MusicService.class));
							}
						}).show();
	}
}