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
	private String scheduleInfo = "";    //�ճ���Ϣ���޸�ǰ������
	private String scheduleChangeInfo = "";  //�ճ���Ϣ���޸�֮�������
	private final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	public String[] sch_type=CalendarConstant.sch_type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		dao = new ScheduleDAO(this);
		
        //final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 5, 0, 0);
		layout = new LinearLayout(this); // ʵ�������ֶ���
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundResource(R.drawable.schedule_bk);
		layout.setLayoutParams(params);
		sv = new ScrollView(this);
		textTop = new BorderTextView(this, null);
		textTop.setTextColor(Color.BLACK); 
		textTop.setBackgroundResource(R.drawable.top);
		textTop.setText("�ճ�����");
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
		//һ�����ڿ��ܶ�Ӧ�������ճ�(scheduleID)
		String[] scheduleIDs = intent.getStringArrayExtra("scheduleID");
		//��ʾ�ճ���ϸ��Ϣ
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
			nothing.setText("����û���ճ̼�¼");
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

		menu.add(1, menu.FIRST, menu.FIRST, "�����ճ�");
		menu.add(1, menu.FIRST+1, menu.FIRST+1,"����ճ�");
		menu.add(1, menu.FIRST+2, menu.FIRST+2,"��������");
		
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
	 * ��ʾ�ճ�������Ϣ
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
		
		
		
		//��ס�ճ�����textview����ʾ�Ƿ�ɾ���ճ���Ϣ
		type.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// TODO �Զ����ɵķ������
 final String scheduleID1 = String.valueOf(v.getTag());
				
				new AlertDialog.Builder(ScheduleInfoView.this).setTitle("ɾ���ճ�").setMessage("ȷ��ɾ��?").setPositiveButton("ȷ��", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						scheduleVO = dao.getScheduleByID(Integer.parseInt(scheduleID1));
						if(scheduleVO==null){
							Toast.makeText(ScheduleInfoView.this, "�ճ��Ѿ�����ɾ��", 0).show();
						}
						else {								
							//Toast.makeText(ScheduleInfoView.this, Integer.toString(scheduleID), 0).show();
							//Toast.makeText(getApplicationContext(), sch_type[scheduleVO.getScheduleTypeID()], 0).show();
						}
						Toast.makeText(ScheduleInfoView.this, "��ɾ���ճ�", 0).show();	
						AlarmHelper alarmHelper=new AlarmHelper(getApplicationContext());							
						alarmHelper.closeAlarm(scheduleVO.getScheduleID(), sch_type[scheduleVO.getScheduleTypeID()], scheduleVO.getScheduleContent());
						
						dao.delete(Integer.parseInt(scheduleID1));
						startActivity(new Intent(ScheduleInfoView.this,ScheduleAll.class));
						
					}
				}).setNegativeButton("ȡ��", null).show();
				
			}
			

		});
		
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ�
		//do something
			Intent intent = new Intent();			
			intent.setClass(ScheduleInfoView.this, ScheduleAll.class);
			startActivity(intent);
		
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

		//do something
			
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {

		//���������û�з��ؽ����
		}
		return super.onKeyDown(keyCode, event);
		}
}
