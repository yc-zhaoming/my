package com.myHelper.sch_activity;

import java.util.ArrayList;
import java.util.TreeSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources.Theme;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.myHelper.R;
import com.myHelper.borderText.BorderEditText;
import com.myHelper.borderText.BorderTextView;
import com.myHelper.constant.CalendarConstant;
import com.myHelper.dao.ScheduleDAO;
import com.myHelper.myAlarmManager.AlarmHelper;
import com.myHelper.vo.ScheduleVO;

public class ScheduleInfoView extends Activity {
	private ScrollView sv = null;
	private LinearLayout layout = null;
	private BorderTextView textTop = null;
	private BorderTextView info = null;
	private BorderTextView date = null;
	private BorderTextView type = null;
	private BorderEditText editInfo = null;
	private ScheduleDAO dao = null;
	private ScheduleVO scheduleVO = null;
	private String scheduleYear = "";
	private String scheduleMonth = "";
	private String scheduleDay = "";
	private String week = "";
	private static ArrayList<String> scheduleDate = null;
	private String scheduleInfo = "";    //日程信息被修改前的内容
	private String scheduleChangeInfo = "";  //日程信息被修改之后的内容
	private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	public String[] sch_type=CalendarConstant.sch_type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		dao = new ScheduleDAO(this);
		
        //final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 5, 0, 0);
		layout = new LinearLayout(this); // 实例化布局对象
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundResource(R.drawable.schedule_bk);
		layout.setLayoutParams(params);
		sv = new ScrollView(this);
		textTop = new BorderTextView(this, null);
		textTop.setTextColor(Color.BLACK); 
		textTop.setBackgroundResource(R.drawable.top);
		textTop.setText("日程详情");
		textTop.setHeight(40);
		textTop.setGravity(Gravity.CENTER);
		
			
		editInfo = new BorderEditText(ScheduleInfoView.this, null);
		editInfo.setTextColor(Color.BLACK); 
		editInfo.setBackgroundColor(Color.WHITE);
		editInfo.setHeight(200);
		editInfo.setGravity(Gravity.TOP);
		editInfo.setLayoutParams(params);
		editInfo.setPadding(10, 5, 10, 5);		
		layout.addView(textTop);
		sv.addView(layout);
		
		
		Intent intent = getIntent();
		//scheduleID = Integer.parseInt(intent.getStringExtra("scheduleID"));
		//一个日期可能对应多个标记日程(scheduleID)
		String[] scheduleIDs = intent.getStringArrayExtra("scheduleID");
		//显示日程详细信息
		if(scheduleIDs!=null){
		for(int i = 0; i< scheduleIDs.length; i++){
			handlerInfo(Integer.parseInt(scheduleIDs[i]));
		}
		}
		else{
			BorderTextView nothing = new BorderTextView(this, null);
			nothing.setTextColor(Color.BLACK); 
			nothing.setBackgroundColor(Color.WHITE);
			nothing.setLayoutParams(params);
			nothing.setGravity(Gravity.CENTER);
			nothing.setHeight(30);
			nothing.setPadding(10, 0, 10, 0);
			nothing.setText("今天没有日程记录");
			layout.addView(nothing);
		}
		setContentView(sv);
		scheduleDate = intent.getStringArrayListExtra("scheduleDate");
		if(scheduleDate!=null){
		scheduleYear=scheduleDate.get(0);
		scheduleMonth=scheduleDate.get(1);
		scheduleDay=scheduleDate.get(2);
		week=scheduleDate.get(3);
		}		
				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(1, menu.FIRST, menu.FIRST, "所有日程");
		menu.add(1, menu.FIRST+1, menu.FIRST+1,"添加日程");
		menu.add(1, menu.FIRST+2, menu.FIRST+2,"返回日历");
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case Menu.FIRST:
			Intent intent = new Intent();
			intent.setClass(ScheduleInfoView.this, ScheduleAll.class);
			startActivity(intent);
			break;
        case Menu.FIRST+1:
        	Intent intent1 = new Intent();
        	intent1.putStringArrayListExtra("scheduleDate", scheduleDate);
			intent1.setClass(ScheduleInfoView.this, ScheduleView.class);
			startActivity(intent1);
			break;
        case Menu.FIRST+2:
        	Intent intent2=new Intent();
        	intent2.setClass(ScheduleInfoView.this, CalendarActivity.class);
        	startActivity(intent2);
    	    break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	/**
	 * 显示日程所有信息
	 */
	public void handlerInfo( final int scheduleID){
		BorderTextView date = new BorderTextView(this, null);
		date.setTextColor(Color.BLACK); 
		date.setBackgroundColor(Color.WHITE);
		date.setLayoutParams(params);
		date.setGravity(Gravity.CENTER);
		date.setHeight(30);
		date.setPadding(10, 0, 10, 0);
		
		BorderTextView type = new BorderTextView(this, null);
		type.setTextColor(Color.RED); 
		type.setBackgroundColor(Color.WHITE);
		type.setLayoutParams(params);
		type.setGravity(Gravity.CENTER);
		type.setHeight(40);
		type.setPadding(10, 0, 10, 0);
		type.setTag(scheduleID);
		
		final BorderTextView info = new BorderTextView(this, null);
		info.setTextColor(Color.BLACK); 
		info.setBackgroundColor(Color.WHITE);
		//info.setHeight(50);
		info.setGravity(Gravity.CENTER_VERTICAL);
		info.setLayoutParams(params);
		info.setPadding(10, 5, 10, 5);
//		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//		info.setLayoutParams(params1);
		
		
		layout.addView(type);
		layout.addView(date);
		layout.addView(info);
		/*Intent intent = getIntent();
		int scheduleID = Integer.parseInt(intent.getStringExtra("scheduleID"));*/	
		scheduleVO = dao.getScheduleByID(scheduleID);		
		if(scheduleVO!=null){
		date.setText(scheduleVO.getScheduleDate());
		type.setText(CalendarConstant.sch_type[scheduleVO.getScheduleTypeID()]);
		info.setText(scheduleVO.getScheduleContent());
		
		}
		
		
		
		//按住日程类型textview就提示是否删除日程信息
		type.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// TODO 自动生成的方法存根
 final String scheduleID1 = String.valueOf(v.getTag());
				
				new AlertDialog.Builder(ScheduleInfoView.this).setTitle("删除日程").setMessage("确认删除?").setPositiveButton("确认", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						scheduleVO = dao.getScheduleByID(Integer.parseInt(scheduleID1));
						if(scheduleVO==null){
							Toast.makeText(ScheduleInfoView.this, "日程已经意外删除", 0).show();
						}
						else {								
							//Toast.makeText(ScheduleInfoView.this, Integer.toString(scheduleID), 0).show();
							//Toast.makeText(getApplicationContext(), sch_type[scheduleVO.getScheduleTypeID()], 0).show();
						}
						Toast.makeText(ScheduleInfoView.this, "已删除日程", 0).show();	
						AlarmHelper alarmHelper=new AlarmHelper(getApplicationContext());							
						alarmHelper.closeAlarm(scheduleVO.getScheduleID(), sch_type[scheduleVO.getScheduleTypeID()], scheduleVO.getScheduleContent());
						
						dao.delete(Integer.parseInt(scheduleID1));
						startActivity(new Intent(ScheduleInfoView.this,ScheduleAll.class));
						
					}
				}).setNegativeButton("取消", null).show();
				
			}
			

		});
		
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
		//do something
			Intent intent = new Intent();			
			intent.setClass(ScheduleInfoView.this, ScheduleAll.class);
			startActivity(intent);
		
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

		//do something
			
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {

		//这里操作是没有返回结果的
		}
		return super.onKeyDown(keyCode, event);
		}
}
