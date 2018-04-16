package com.myHelper.sch_activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.myHelper.R;
import com.myHelper.R.string;
import com.myHelper.borderText.BorderTextView;
import com.myHelper.calendar.LunarCalendar;
import com.myHelper.constant.CalendarConstant;
import com.myHelper.dao.ScheduleDAO;
import com.myHelper.myAlarmManager.AlarmHelper;
import com.myHelper.service.SaveService;
import com.myHelper.service.SaveThread;
import com.myHelper.vo.ScheduleDateTag;
import com.myHelper.vo.ScheduleVO;


public class ScheduleView extends Activity {

	private LunarCalendar lc = null;
	private ScheduleDAO dao = null;
	private BorderTextView scheduleType = null;
	private BorderTextView dateText = null;
	private BorderTextView scheduleTop = null;
	private EditText scheduleText = null;
	private BorderTextView scheduleSave = null;  //���水ťͼƬ
	private static int hour = -1;
	private static int minute = -1;
	private static ArrayList<String> scheduleDate = null;
	public static ArrayList<String> sche_message = new ArrayList<String>();
	private ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>();
	private String scheduleYear = "";
	private String scheduleMonth = "";
	private String scheduleDay = "";
	private String week = "";
	String []weeks={"������", "����һ", "���ڶ�", "������", "������", "������", "������"};
	//��ʱ����ʱ�������
	private String tempMonth;
	private String tempDay;

	private String[] sch_type = CalendarConstant.sch_type;
	private String[] remind = CalendarConstant.remind;
	private int sch_typeID = 0;   //�ճ�����
	private int remindID = 0;     //��������
	int tempFlag=0;
	private static String schText = "";
    int schTypeID = 0;
    private Thread timeThread;
	public ScheduleView() {
		lc = new LunarCalendar();
		dao = new ScheduleDAO(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);

		
		scheduleTop = (BorderTextView) findViewById(R.id.scheduleTop);
		scheduleType = (BorderTextView) findViewById(R.id.scheduleType);
		scheduleSave = (BorderTextView) findViewById(R.id.save);
		scheduleType.setBackgroundColor(Color.WHITE);
		scheduleType.setText(sch_type[0]+"\t\t\t\t"+remind[remindID]);
		dateText = (BorderTextView) findViewById(R.id.scheduleDate);
		dateText.setBackgroundColor(Color.WHITE);
		scheduleText = (EditText) findViewById(R.id.scheduleText);
		scheduleText.setBackgroundColor(Color.WHITE);
		if(schText != null){
			//��ѡ���ճ�����֮ǰ�Ѿ��������ճ̵���Ϣ��������ת��ѡ���ճ�����֮ǰӦ�����ճ���Ϣ���浽schText�У�������ʱ�ٴο���ȡ�á�
			scheduleText.setText(schText);
			//һ���������֮���Ӧ�ý��˾�̬��������Ϊ�գ�
			schText = "";  
		}

		Date date = new Date();
		if(hour == -1 && minute == -1){
			hour = date.getHours();
			minute = date.getMinutes();
		}
		getScheduleDate();
		dateText.setText(getStringOfDate());

		//����ճ�����
		scheduleType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				schText = scheduleText.getText().toString();
				Intent intent = new Intent();
				intent.setClass(ScheduleView.this, ScheduleTypeView.class);
				intent.putExtra("sch_remind", new int[]{sch_typeID,remindID});
				startActivity(intent);
			}
		});

		//���ʱ��
		dateText.setOnClickListener(new OnClickListener() {
			
			@Override
			
			public void onClick(View v) {
				Date date1=new Date();
				hour = date1.getHours();
				minute = date1.getMinutes();
				
				new TimePickerDialog(ScheduleView.this, new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int min) {

						hour = hourOfDay;
						minute = min;
						dateText.setText(getStringOfDate());
					}
				}, hour, minute, true).show();
				new DatePickerDialog(ScheduleView.this,new OnDateSetListener() {
					
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						// TODO �Զ����ɵķ������
						scheduleYear=Integer.toString(year);
						scheduleMonth=Integer.toString(monthOfYear+1);
						scheduleDay=Integer.toString(dayOfMonth);
						getWeekNumber(new Date(year,monthOfYear,dayOfMonth-1));
						dateText.setText(getStringOfDate());
		
					}
					
				},Integer.parseInt(scheduleYear),Integer.parseInt(scheduleMonth)-1, Integer.parseInt(scheduleDay)).show();
					
					//tempFlag++;
				}
	
		});
		
		//�����ճ���Ϣ
		scheduleSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(scheduleText.getText().toString())){
					//�ж�������Ƿ�Ϊ��
					new AlertDialog.Builder(ScheduleView.this).setTitle("�����ճ�").setMessage("�ճ���Ϣ����Ϊ��").setPositiveButton("ȷ��", null).show();
				}else{
					//���ճ���Ϣ����
					String showDate = handleInfo(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay), hour, minute, week, remindID);
	                ScheduleVO schedulevo = new ScheduleVO();
	                schedulevo.setScheduleTypeID(sch_typeID);
	                schedulevo.setRemindID(remindID);
	                schedulevo.setScheduleDate(showDate);
	                schedulevo.setScheduleContent(scheduleText.getText().toString());
					int scheduleID = dao.save(schedulevo);
					//��scheduleID���浽������(��Ϊ��CalendarActivity�е��gridView�е�һ��Item���ܻ��Ӧ�������ճ�(scheduleID))
					String [] scheduleIDs = new String[]{String.valueOf(scheduleID)};
					Intent intent = new Intent();
					intent.setClass(ScheduleView.this, ScheduleInfoView.class);
					//intent.putExtra("scheduleID", String.valueOf(scheduleID));
					Toast.makeText(getApplicationContext(), sch_type[schedulevo.getScheduleTypeID()]+"����ɹ�",0).show();
                    intent.putExtra("scheduleID", scheduleIDs);					
                    startActivity(intent);

                    threadService(remindID, scheduleYear, scheduleMonth, scheduleDay, scheduleID, hour, minute);
//                    Intent intent2=new Intent();
//                    intent2.putExtra("remindID", remindID);
//                    intent2.putExtra("scheduleYear", scheduleYear);
//                    intent2.putExtra("scheduleMonth", scheduleMonth);
//                    intent2.putExtra("scheduleDay", scheduleDay);
//                    intent2.putExtra("scheduleID", scheduleID);
//                    intent2.putExtra("hour", hour);
//                    intent2.putExtra("minute", minute);
//                    intent2.setClass(ScheduleView.this, SaveService.class);
//                    startService(intent2);
                    
					
				}
			}
		});
		
	}

	/**
	 * �����ճ̱������
	 * @param remindID
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setScheduleDateTag(int remindID, String year, String month, String day,int scheduleID,int temphour,int min){}
	
	/**
	 * �ճ̱�����ڵĴ���
	 * @param cal
	 */
	public void handleDate(Calendar cal, int scheduleID){
		ScheduleDateTag dateTag = new ScheduleDateTag();
		dateTag.setYear(cal.get(Calendar.YEAR));
		dateTag.setMonth(cal.get(Calendar.MONTH)+1);
		dateTag.setDay(cal.get(Calendar.DATE));
		dateTag.setScheduleID(scheduleID);
		dateTagList.add(dateTag);
	}
	
	/**
	 * ͨ��ѡ�����Ѵ���������������ʾ���
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param week
	 * @param remindID
	 */
	public String handleInfo(int year, int month, int day, int hour, int minute, String week, int remindID){
		String remindType = remind[remindID];     //��������
		String show = "";
		if(remindID==0){
			//����һ��,��10����,��30����,��һСʱ
			show = year+"-"+month+"-"+day+"\t"+hour+":"+minute+"\t"+week+"\t\t"+remindType;
		}else if(remindID == 1){
			//ÿ��
			show = "ÿ��"+week+"\t"+hour+":"+minute;
		}else if(remindID == 2){
			//ÿ��
			show = "ÿ��"+day+"��"+"\t"+hour+":"+minute;
		}else if(remindID == 3){
			//ÿ��
			show = "ÿ��"+month+"-"+day+"\t"+hour+":"+minute;
		}
		return show;
	}
	
	/**
	 * ���item֮����ʾ��������Ϣ
	 * 
	 * @return
	 */
	public void getScheduleDate() {
		Intent intent = getIntent();
		// intent.getp
		if(intent.getStringArrayListExtra("scheduleDate") != null){
			//��CalendarActivity�д�����ֵ��������������Ϣ��
			scheduleDate = intent.getStringArrayListExtra("scheduleDate");
		}
		int [] schType_remind = intent.getIntArrayExtra("schType_remind");  //��ScheduleTypeView�д�����ֵ(�����ճ����ͺ����Ѵ�����Ϣ)
		
		if(schType_remind != null){
			sch_typeID = schType_remind[0];
			remindID = schType_remind[1];
			scheduleType.setText(sch_type[sch_typeID]+"\t\t\t\t"+remind[remindID]);
		}
		// �õ������պ�����
		if(scheduleDate!=null){
		scheduleYear = scheduleDate.get(0);
		scheduleMonth = scheduleDate.get(1);
		tempMonth = scheduleMonth;
		if (Integer.parseInt(scheduleMonth) < 10) {
			scheduleMonth = "0" + scheduleMonth;
		}
		scheduleDay = scheduleDate.get(2);
		tempDay = scheduleDay;
		if (Integer.parseInt(scheduleDay) < 10) {
			scheduleDay = "0" + scheduleDay;
		}
		week = scheduleDate.get(3);
		
	}
	}
	public String getStringOfDate(){
		String hour_c = String.valueOf(hour);
		String minute_c = String.valueOf(minute);
		if(hour < 10){
			hour_c = "0"+hour_c;
		}
		if(minute < 10){
			minute_c = "0"+minute_c;
		}
		String scheduleLunarDay = getLunarDay(Integer.parseInt(scheduleYear),
				Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
		String scheduleLunarMonth = lc.getLunarMonth(); // �õ��������·�
		StringBuffer scheduleDateStr = new StringBuffer();
		scheduleDateStr.append(scheduleYear).append("-").append(scheduleMonth)
				.append("-").append(scheduleDay).append(" ").append(hour_c).append(":").append(minute_c).append("\n").append(
						scheduleLunarMonth).append(scheduleLunarDay)
				.append(" ").append(week);
		// dateText.setText(scheduleDateStr);
		return scheduleDateStr.toString();
	}
	public void getWeekNumber(Date date){
		
		Calendar calendar=Calendar.getInstance();	
		calendar.setTime(date);
		int temp=calendar.get(Calendar.DAY_OF_WEEK)-1;
		week=String.valueOf(weeks[temp]);
		
	}
	/**
	 * �������ڵ������շ�����������
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public String getLunarDay(int year, int month, int day) {
		String lunarDay = lc.getLunarDate(year, month, day, true);
		// {������ȡ��������Ӧ����������ʱ������������ڶ�Ӧ����������Ϊ"��һ"���ͱ����ó����·�(��:���£����¡�������)},�����ڴ˾�Ҫ�жϵõ������������Ƿ�Ϊ�·ݣ�������·ݾ�����Ϊ"��һ"
		if (lunarDay.substring(1, 2).equals("��")) {
			lunarDay = "��һ";
		}
		return lunarDay;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ�
		//do something
			Intent intent = new Intent();			
			intent.setClass(ScheduleView.this, CalendarActivity.class);
			startActivity(intent);
		
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

		//do something
			
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {

		//���������û�з��ؽ����
		}
		return super.onKeyDown(keyCode, event);
		}
	public void threadService(final int remindID,final String year,final String month,final String day,final int scheduleID,final int hour,final int minute)
	{

        timeThread = new Thread(new Runnable() {

            @Override
            public void run() {

        		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        		String d = year+"-"+month+"-"+day;
        		int month_temp=Integer.parseInt(month);
        		String d1=year+"-"+Integer.toString(month_temp)+"-"+day+"-"+Integer.toString(hour)+"-"+Integer.toString(minute);        		
        		Calendar cal = Calendar.getInstance();
        		ScheduleVO sv=dao.getScheduleByID(scheduleID);        		
    			AlarmHelper alarmHelper=new AlarmHelper(ScheduleView.this);
    			//Intent intent=new Intent(); 
    			Calendar mcalendar=Calendar.getInstance();
    			mcalendar.setTimeInMillis(System.currentTimeMillis());
    			mcalendar.set(Calendar.YEAR,Integer.parseInt(year));
    			mcalendar.set(Calendar.MONTH,Integer.parseInt(month)-1);
    			mcalendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(day));
    			mcalendar.set(Calendar.HOUR_OF_DAY,hour);
    			mcalendar.set(Calendar.MINUTE,minute);
    			mcalendar.set(Calendar.SECOND,0);
    			mcalendar.set(Calendar.MILLISECOND,0);
    			long time=mcalendar.getTimeInMillis();
        		try {
        			cal.setTime(format.parse(d));
        		} catch (ParseException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		Date date=new Date();
        		Date now =new Date();
    			try {
					date=format1.parse(d1);
				} catch (ParseException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
    			Calendar c=Calendar.getInstance();
    			now=c.getTime();
        		//��װҪ��ǵ�����
        		if(remindID == 0 ){
        			//"����һ��","��10����","��30����","��һСʱ"��ֻ���ǵ�ǰ��һ�죩
        			ScheduleDateTag dateTag = new ScheduleDateTag();
        			dateTag.setYear(Integer.parseInt(year));
        			dateTag.setMonth(Integer.parseInt(month));
        			dateTag.setDay(Integer.parseInt(day));
        			dateTag.setScheduleID(scheduleID);
        			dateTagList.add(dateTag);
        			//Toast.makeText(getApplicationContext(), format1.format(now), 0).show();
        			
        			if(date.after(now)){
        				alarmHelper.openAlarm(scheduleID, sch_type[sv.getScheduleTypeID()], sv.getScheduleContent(), time);
        			}
        			}
        		
        	
        		else if(remindID == 1){
        			//ÿ���ظ�(�������ճ̵�����(���ڼ�)����������ÿ�ܵ���һ���Ҫ���)
        			for(int i =0; i <= (2049-Integer.parseInt(year))*12*4; i++){
        				if( i==0 ){
        					cal.add(Calendar.WEEK_OF_MONTH, 0);
        				}else{
        				    cal.add(Calendar.WEEK_OF_MONTH, 1);
        				}
        				handleDate(cal,scheduleID);
        			}
        			if(date.after(now)){
        				alarmHelper.openAlarm(scheduleID, sch_type[sv.getScheduleTypeID()], sv.getScheduleContent(), time);
        			}
        			}else if(remindID == 2){
        			//ÿ���ظ�(�������ճ̵�����(���¼���)����������ÿ�µ���һ���Ҫ���)
        			for(int i =0; i <= (2049-Integer.parseInt(year))*12; i++){
        				if( i==0 ){
        					cal.add(Calendar.MONTH, 0);
        				}else{
        				    cal.add(Calendar.MONTH, 1);
        				}
        				handleDate(cal,scheduleID);
        			}
        			if(date.after(now)){
        			alarmHelper.openAlarm(scheduleID, sch_type[sv.getScheduleTypeID()], sv.getScheduleContent(), time);
        			}
        			}else if(remindID == 3){
        			//ÿ���ظ�(�������ճ̵�����(��һ�꼸�¼���)����������ÿ�����һ���Ҫ���)
        			for(int i =0; i <= 2049-Integer.parseInt(year); i++){
        				if( i==0 ){
        					cal.add(Calendar.YEAR, 0);
        				}else{
        				    cal.add(Calendar.YEAR, 1);
        				}
        				handleDate(cal,scheduleID);
        			}
        			if(date.after(now)){
        				alarmHelper.openAlarm(scheduleID, sch_type[sv.getScheduleTypeID()], sv.getScheduleContent(), time);
        			}
        		}
        		//��������ڴ������ݿ���
        		
        		dao.saveTagDate(dateTagList);
        		
        	

            }
        });
        timeThread.start();
	
	}
}
