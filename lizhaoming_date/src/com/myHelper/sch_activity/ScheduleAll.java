package com.myHelper.sch_activity;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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

import com.myHelper.R;
import com.myHelper.borderText.BorderTextView;
import com.myHelper.constant.CalendarConstant;
import com.myHelper.dao.ScheduleDAO;
import com.myHelper.vo.ScheduleVO;


public class ScheduleAll extends Activity {

	private ScrollView sv = null;
	private LinearLayout layout = null;
	private BorderTextView textTop = null;
	private ScheduleDAO dao = null;
	private ScheduleVO scheduleVO = null;
	private ArrayList<ScheduleVO> schList = new ArrayList<ScheduleVO>();
	private String scheduleInfo = "";
	private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	
	private int scheduleID = -1;
	private String scheduleYear = "";
	private String scheduleMonth = "";
	private String scheduleDay = "";
	private String week = "";
	String []weeks={"������", "����һ", "���ڶ�", "������", "������", "������", "������"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		dao = new ScheduleDAO(this);
		sv = new ScrollView(this);
		
		params.setMargins(0, 5, 0, 0);
		layout = new LinearLayout(this); // ʵ�������ֶ���
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundResource(R.drawable.schedule_bk);
		layout.setLayoutParams(params);
		
		textTop = new BorderTextView(this, null);
		textTop.setTextColor(Color.BLACK); 
		textTop.setBackgroundResource(R.drawable.top);
		textTop.setText("�����ճ�");
		textTop.setHeight(40);
		textTop.setGravity(Gravity.CENTER);
		
		layout.addView(textTop);
		sv.addView(layout);
		
		setContentView(sv);
		
		getScheduleAll();
	}
	
	/**
	 * �õ����е��ճ���Ϣ
	 */
	public void getScheduleAll(){
		schList = dao.getAllSchedule();
		if(schList != null){
			for (ScheduleVO vo : schList) {
				String content = vo.getScheduleContent();
				int startLine = content.indexOf("\n");
				if(startLine > 0){
					content = content.substring(0, startLine)+"...";
				}else if(content.length() > 30){
					content = content.substring(0, 30)+"...";
				}
				scheduleInfo = CalendarConstant.sch_type[vo.getScheduleTypeID()]+"\n"+vo.getScheduleDate()+"\n"+content;
				scheduleID = vo.getScheduleID();
				createInfotext(scheduleInfo, scheduleID);
			}
		}else{
			scheduleInfo = "û���ճ�";
			createInfotext(scheduleInfo,-1);
		}
	}
	
	/**
	 * �������ճ���Ϣ��textview
	 */
	public void createInfotext(String scheduleInfo, final int scheduleID){
		final BorderTextView info = new BorderTextView(this, null);
		info.setText(scheduleInfo);
		info.setTextColor(Color.BLACK); 
		info.setBackgroundColor(Color.WHITE);
		info.setLayoutParams(params);
		info.setGravity(Gravity.CENTER_VERTICAL);
		info.setBackgroundResource(R.drawable.mylistviewbackground);
		info.setPadding(10, 5, 10, 5);
		info.setTag(scheduleID);
		layout.addView(info);
		
		//���ÿһ��textview����ת��shceduleInfoView����ʾ��ϸ��Ϣ
		info.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getIntentDate();
				ArrayList<String> scheduleDate = new ArrayList<String>();
				scheduleDate.add(scheduleYear);
				scheduleDate.add(scheduleMonth);
				scheduleDate.add(scheduleDay);
				scheduleDate.add(week);
				String schID = String.valueOf(v.getTag());
				String scheduleIDs[] = new String[]{schID};
				if (scheduleID!=-1) {
					Intent intent = new Intent();
					intent.setClass(ScheduleAll.this, ScheduleInfoView.class);
					intent.putExtra("scheduleID", scheduleIDs);
					intent.putStringArrayListExtra("scheduleDate", scheduleDate);
					startActivity(intent);
				}
			}
		});
		
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(1, menu.FIRST, menu.FIRST, "��������");
		menu.add(1, menu.FIRST+1, menu.FIRST+1, "����ճ�");
		menu.add(1, menu.FIRST+2, menu.FIRST+2, "ɾ�����м�¼");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case Menu.FIRST:
			Intent intent = new Intent();
			intent.setClass(ScheduleAll.this, CalendarActivity.class);
			startActivity(intent);
			break;
		case Menu.FIRST+1:
			getIntentDate();
			ArrayList<String> scheduleDate = new ArrayList<String>();
			scheduleDate.add(scheduleYear);
			scheduleDate.add(scheduleMonth);
			scheduleDate.add(scheduleDay);
			scheduleDate.add(week);
			Intent intent1 = new Intent();
			intent1.putStringArrayListExtra("scheduleDate", scheduleDate);
			intent1.setClass(ScheduleAll.this, ScheduleView.class);
			startActivity(intent1);
			break;
		case Menu.FIRST+2:
			final ScheduleDAO dao=new ScheduleDAO(this);
		new AlertDialog.Builder(ScheduleAll.this).setTitle("ɾ���ճ�").setMessage("ȷ��ɾ��?").setPositiveButton("ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dao.deldeteAll();
				Intent intent=new Intent();
				intent.setClass(ScheduleAll.this, CalendarActivity.class);
				startActivity(intent);
			
			}

		
		}).setNegativeButton("ȡ��", null).show();
		}
		return super.onOptionsItemSelected(item);
	}
	public void getIntentDate(){
		Calendar now=Calendar.getInstance();
		scheduleYear=Integer.toString(now.get(Calendar.YEAR));
		scheduleMonth=Integer.toString(now.get(Calendar.MONTH)+1);
		scheduleDay=Integer.toString(now.get(Calendar.DAY_OF_MONTH));
		Calendar calendar=Calendar.getInstance();
		int temp=calendar.get(Calendar.DAY_OF_WEEK)-1;
		week=weeks[temp];

		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ�
		//do something
			Intent intent = new Intent();			
			intent.setClass(ScheduleAll.this, CalendarActivity.class);
			startActivity(intent);
		
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

		//do something
			
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {

		//���������û�з��ؽ����
		}
		return super.onKeyDown(keyCode, event);
		}
}

