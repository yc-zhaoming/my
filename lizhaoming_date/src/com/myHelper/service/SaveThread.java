package com.myHelper.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.widget.Toast;

import com.myHelper.constant.CalendarConstant;
import com.myHelper.dao.ScheduleDAO;
import com.myHelper.myAlarmManager.AlarmHelper;
import com.myHelper.vo.ScheduleDateTag;
import com.myHelper.vo.ScheduleVO;

public class SaveThread extends Thread{
	Context context;
	ScheduleDAO dao;
	private String year;
	private String month;
	private String day;
	private int scheduleID;
	private int remindID;
	private int minute;
	private int hour;
	private String[] sch_type = CalendarConstant.sch_type;
	private String[] remind = CalendarConstant.remind;
	private ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>();
	public SaveThread(Context context,int remindID,String year,String month,String day,int scheduleID,int hour,int minute){
		this.year=year;
		this.month=month;
		this.day=day;
		this.remindID=remindID;
		this.scheduleID=scheduleID;
		this.hour=hour;
		this.minute=minute;
		this.context=context;
		Toast.makeText(this.context,"�̴߳���", 0);
	}
	@Override
	public void run() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
		String d = year+"-"+month+"-"+day;
		Calendar cal = Calendar.getInstance();
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
			
//			ScheduleDAO dao=new ScheduleDAO(context);
//			ScheduleVO sv=dao.getScheduleByID(scheduleID);
//			AlarmHelper alarmHelper=new AlarmHelper(context);
//			//Intent intent=new Intent(); 
//			Calendar mcalendar=Calendar.getInstance();
//			mcalendar.setTimeInMillis(System.currentTimeMillis());
//			//mcalendar.set(Calendar.YEAR,Integer.parseInt(year));
//			//mcalendar.set(Calendar.MONTH,Integer.parseInt(month));
//			//mcalendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(day));
//			mcalendar.set(Calendar.HOUR_OF_DAY,hour);
//			mcalendar.set(Calendar.MINUTE,minute);
//			mcalendar.set(Calendar.SECOND,0);
//			mcalendar.set(Calendar.MILLISECOND,0);
//			long time=mcalendar.getTimeInMillis();
//			alarmHelper.openAlarm(0, sch_type[sv.getScheduleTypeID()], sv.getScheduleContent(), time);
		}
		
		else if(remindID == 1){
			//ÿ���ظ�(�����õ��ճ̵Ŀ�ʼ��֮��ÿһ���Ҫ���)
			for(int i =0; i <= (2049-Integer.parseInt(year))*12*4*7; i++){
				if( i==0 ){
					cal.add(Calendar.DATE, 0);
				}else{
				    cal.add(Calendar.DATE, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 2){
			//ÿ���ظ�(�������ճ̵�����(���ڼ�)����������ÿ�ܵ���һ���Ҫ���)
			for(int i =0; i <= (2049-Integer.parseInt(year))*12*4; i++){
				if( i==0 ){
					cal.add(Calendar.WEEK_OF_MONTH, 0);
				}else{
				    cal.add(Calendar.WEEK_OF_MONTH, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 3){
			//ÿ���ظ�(�������ճ̵�����(���¼���)����������ÿ�µ���һ���Ҫ���)
			for(int i =0; i <= (2049-Integer.parseInt(year))*12; i++){
				if( i==0 ){
					cal.add(Calendar.MONTH, 0);
				}else{
				    cal.add(Calendar.MONTH, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 4){
			//ÿ���ظ�(�������ճ̵�����(��һ�꼸�¼���)����������ÿ�����һ���Ҫ���)
			for(int i =0; i <= 2049-Integer.parseInt(year); i++){
				if( i==0 ){
					cal.add(Calendar.YEAR, 0);
				}else{
				    cal.add(Calendar.YEAR, 1);
				}
				handleDate(cal,scheduleID);
			}
		}
		//��������ڴ������ݿ���
		dao.saveTagDate(dateTagList);
	}
	
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
	
}
