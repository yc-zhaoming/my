package com.myHelper.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.myHelper.constant.CalendarConstant;
import com.myHelper.dao.ScheduleDAO;
import com.myHelper.myAlarmManager.AlarmHelper;
import com.myHelper.sch_activity.ScheduleView;
import com.myHelper.vo.ScheduleDateTag;
import com.myHelper.vo.ScheduleVO;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.widget.Toast;

public class SaveService extends Service {
	ScheduleDAO dao;
	int scheduleID=0;
	int remindID=0;
	String year="";
	String month="";
	String day="";
	int hour=0;
	int minute=0;
	Thread timeThread;
	private String[] sch_type = CalendarConstant.sch_type;
	private String[] remind = CalendarConstant.remind;
	private ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>();
	private ArrayList<String> sche_message=null;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO �Զ����ɵķ������
		return null;
	}
	@Override
	public void onCreate(){
		dao=new ScheduleDAO(this);
		//Toast.makeText(getApplicationContext(), "bb",Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onStart(Intent intent,int startId){
	
		remindID=intent.getIntExtra("remindID",0);
		year=intent.getStringExtra("scheduleYear");
		month=intent.getStringExtra("scheduleMonth");
		day=intent.getStringExtra("scheduleDay");
		scheduleID=intent.getIntExtra("scheduleID", 0);
		hour=intent.getIntExtra("hour", 0);
		minute=intent.getIntExtra("minute", 0);	
		Toast.makeText(getApplicationContext(), remindID+year+hour, 0).show();
		myThread();
	}
	public void handleDate(Calendar cal, int scheduleID){
		ScheduleDateTag dateTag = new ScheduleDateTag();
		dateTag.setYear(cal.get(Calendar.YEAR));
		dateTag.setMonth(cal.get(Calendar.MONTH)+1);
		dateTag.setDay(cal.get(Calendar.DATE));
		dateTag.setScheduleID(scheduleID);
		dateTagList.add(dateTag);
	}
	@Override
	public void onDestroy(){
		
	}

public void myThread(){
    timeThread = new Thread(new Runnable() {

        @Override
        public void run() {

    		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
    		String d = year+"-"+month+"-"+day;
    		Calendar cal = Calendar.getInstance();
    		ScheduleVO sv=dao.getScheduleByID(scheduleID);
			AlarmHelper alarmHelper=new AlarmHelper(getApplicationContext());
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
    		//��װҪ��ǵ�����
    		if(remindID == 0 ){
    			//"����һ��","��10����","��30����","��һСʱ"��ֻ���ǵ�ǰ��һ�죩
    			ScheduleDateTag dateTag = new ScheduleDateTag();
    			dateTag.setYear(Integer.parseInt(year));
    			dateTag.setMonth(Integer.parseInt(month));
    			dateTag.setDay(Integer.parseInt(day));
    			dateTag.setScheduleID(scheduleID);
    			dateTagList.add(dateTag);
    			

    			alarmHelper.openAlarm(scheduleID, sch_type[sv.getScheduleTypeID()], sv.getScheduleContent(), time);
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
    			Toast.makeText(getApplicationContext(), scheduleID, 0).show();
    			alarmHelper.openAlarm(scheduleID, sch_type[sv.getScheduleTypeID()], sv.getScheduleContent(), time);
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
    			//alarmHelper.openAlarm(scheduleID, sch_type[sv.getScheduleTypeID()], sv.getScheduleContent(), time);
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
    		
    			alarmHelper.openAlarm(scheduleID, sch_type[sv.getScheduleTypeID()], sv.getScheduleContent(), time);
    		}
    		//��������ڴ������ݿ���
    		dao.saveTagDate(dateTagList);
    		
    	

        }
    });
    timeThread.start();
}



}
