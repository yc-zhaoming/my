/**
 * 
 */
package com.myHelper.course;

import com.myHelper.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;


public class course_set_activity extends Activity {
	
	Button btn_course_set_confirm;
	DatePicker course_first_week;
	int year = 0;
	int month = 0;
	int day = 0;

	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_set);
		
		btn_course_set_confirm = (Button)findViewById(R.id.btn_course_set_confirm);
		btn_course_set_confirm.setOnClickListener(btn_confirm_listener);
		
		course_first_week = (DatePicker) findViewById(R.id.course_first_week);
		day = course_first_week.getDayOfMonth();
		month = course_first_week.getMonth();
		year = course_first_week.getYear();
		Log.e("y1", " " +year);
	}
	
	//按钮监听函数
	private Button.OnClickListener btn_confirm_listener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			
			day = course_first_week.getDayOfMonth();
			month = course_first_week.getMonth();
			year = course_first_week.getYear();
			MainActivity.first_week_message.setFirstDate(day, "first_day");
			MainActivity.first_week_message.setFirstDate(month, "first_month");
			MainActivity.first_week_message.setFirstDate(year, "first_year");
			//Log.e("y", " " +year);
			// TODO Auto-generated method stub
			Bundle bundle = new Bundle();
			bundle.putInt("year", year);
			bundle.putInt("month", month);
			bundle.putInt("day", day);
			
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			Log.e("y2", " " +year);
			finish();
		}
	};
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
		//do something
			Intent intent = new Intent();			
			setResult(RESULT_OK, intent);
			finish();
		
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

		//do something
			
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {

		//这里操作是没有返回结果的
		}
		return super.onKeyDown(keyCode, event);
		}
}
