package com.myHelper.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.myHelper.R;
import com.myHelper.myAlarmManager.AlarmAlert;
import com.myHelper.sch_activity.CalendarActivity;
import com.myHelper.service.MusicService;

public class MainActivity extends Activity {
	
	//菜单选项
	public static final int HELP = Menu.FIRST;
	public static final int EXIT = Menu.FIRST + 1;
	public static final int SCORE = Menu.FIRST + 2; 
	public static first_week_message first_week_message;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        first_week_message=new first_week_message(this);
        setTitle("我的生活助手");
        find_and_modify_button();
        
    }

	private void find_and_modify_button() {
		// TODO Auto-generated method stub
		Button btn_course = (Button) findViewById(R.id.btn_course);
		btn_course.setOnClickListener(course_listener);
		Button btn_diary = (Button) findViewById(R.id.btn_diary);
		btn_diary.setOnClickListener(diary_listener);
		Button mydate= (Button) findViewById(R.id.mydate);
		mydate.setOnClickListener(mydate_listener);
	}
	private Button.OnClickListener course_listener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, course_activity.class);
			startActivity(intent);
		}
	};
	private Button.OnClickListener diary_listener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, diary_login_activity.class);
			startActivity(intent);
		}
	};
	private Button.OnClickListener mydate_listener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, CalendarActivity.class);
			startActivity(intent);
		}
	};

	@Override
	/*
	 * menu.findItem(EXIT_ID);找到特定的MenuItem
	 * MenuItem.setIcon.可以设置menu按钮的背景
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, HELP, 0, "帮助").setIcon(R.drawable.helps);
		menu.add(0, EXIT, 0, "退出").setIcon(R.drawable.exit);
		menu.add(0, SCORE, 0, "评分").setIcon(R.drawable.score);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case HELP: 
			 Intent help_intent = new Intent();
			 help_intent.setClass(MainActivity.this,help_activity.class);
			 startActivity(help_intent);
		break;
		case SCORE:
			 Intent score_intent = new Intent();
			 score_intent.setClass(MainActivity.this, score_activity.class);
			 startActivity(score_intent);
		break;
		case EXIT: 
			System.exit(0);
		 break;

		}
		return super.onOptionsItemSelected(item);
	}

}