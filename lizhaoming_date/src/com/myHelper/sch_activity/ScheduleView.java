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
	private BorderTextView scheduleSave = null;  //保存按钮图片
	private static int hour = -1;
	private static int minute = -1;
	private static ArrayList<String> scheduleDate = null;
	public static ArrayList<String> sche_message = new ArrayList<String>();
	private ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>();
	private String scheduleYear = "";
	private String scheduleMonth = "";
	private String scheduleDay = "";
	private String week = "";
	String []weeks={"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	//临时日期时间变量，
	private String tempMonth;
	private String tempDay;

	private String[] sch_type = CalendarConstant.sch_type;
	private String[] remind = CalendarConstant.remind;
	private int sch_typeID = 0;   //日程类型
	private int remindID = 0;     //提醒类型
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
			//在选择日程类型之前已经输入了日程的信息，则在跳转到选择日程类型之前应当将日程信息保存到schText中，当返回时再次可以取得。
			scheduleText.setText(schText);
			//一旦设置完成之后就应该将此静态变量设置为空，
			schText = "";  
		}

		Date date = new Date();
		if(hour == -1 && minute == -1){
			hour = date.getHours();
			minute = date.getMinutes();
		}
		getScheduleDate();
		dateText.setText(getStringOfDate());

		//获得日程类型
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

		//获得时间
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
						// TODO 自动生成的方法存根
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
		
		//保存日程信息
		scheduleSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(scheduleText.getText().toString())){
					//判断输入框是否为空
					new AlertDialog.Builder(ScheduleView.this).setTitle("输入日程").setMessage("日程信息不能为空").setPositiveButton("确认", null).show();
				}else{
					//将日程信息保存
					String showDate = handleInfo(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay), hour, minute, week, remindID);
	                ScheduleVO schedulevo = new ScheduleVO();
	                schedulevo.setScheduleTypeID(sch_typeID);
	                schedulevo.setRemindID(remindID);
	                schedulevo.setScheduleDate(showDate);
	                schedulevo.setScheduleContent(scheduleText.getText().toString());
					int scheduleID = dao.save(schedulevo);
					//将scheduleID保存到数据中(因为在CalendarActivity中点击gridView中的一个Item可能会对应多个标记日程(scheduleID))
					String [] scheduleIDs = new String[]{String.valueOf(scheduleID)};
					Intent intent = new Intent();
					intent.setClass(ScheduleView.this, ScheduleInfoView.class);
					//intent.putExtra("scheduleID", String.valueOf(scheduleID));
					Toast.makeText(getApplicationContext(), sch_type[schedulevo.getScheduleTypeID()]+"保存成功",0).show();
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
	 * 设置日程标记日期
	 * @param remindID
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setScheduleDateTag(int remindID, String year, String month, String day,int scheduleID,int temphour,int min){}
	
	/**
	 * 日程标记日期的处理
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
	 * 通过选择提醒次数来处理最后的显示结果
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param week
	 * @param remindID
	 */
	public String handleInfo(int year, int month, int day, int hour, int minute, String week, int remindID){
		String remindType = remind[remindID];     //提醒类型
		String show = "";
		if(remindID==0){
			//提醒一次,隔10分钟,隔30分钟,隔一小时
			show = year+"-"+month+"-"+day+"\t"+hour+":"+minute+"\t"+week+"\t\t"+remindType;
		}else if(remindID == 1){
			//每周
			show = "每周"+week+"\t"+hour+":"+minute;
		}else if(remindID == 2){
			//每月
			show = "每月"+day+"号"+"\t"+hour+":"+minute;
		}else if(remindID == 3){
			//每年
			show = "每年"+month+"-"+day+"\t"+hour+":"+minute;
		}
		return show;
	}
	
	/**
	 * 点击item之后，显示的日期信息
	 * 
	 * @return
	 */
	public void getScheduleDate() {
		Intent intent = getIntent();
		// intent.getp
		if(intent.getStringArrayListExtra("scheduleDate") != null){
			//从CalendarActivity中传来的值（包含年与日信息）
			scheduleDate = intent.getStringArrayListExtra("scheduleDate");
		}
		int [] schType_remind = intent.getIntArrayExtra("schType_remind");  //从ScheduleTypeView中传来的值(包含日程类型和提醒次数信息)
		
		if(schType_remind != null){
			sch_typeID = schType_remind[0];
			remindID = schType_remind[1];
			scheduleType.setText(sch_type[sch_typeID]+"\t\t\t\t"+remind[remindID]);
		}
		// 得到年月日和星期
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
		String scheduleLunarMonth = lc.getLunarMonth(); // 得到阴历的月份
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
	 * 根据日期的年月日返回阴历日期
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public String getLunarDay(int year, int month, int day) {
		String lunarDay = lc.getLunarDate(year, month, day, true);
		// {由于在取得阳历对应的阴历日期时，如果阳历日期对应的阴历日期为"初一"，就被设置成了月份(如:四月，五月。。。等)},所以在此就要判断得到的阴历日期是否为月份，如果是月份就设置为"初一"
		if (lunarDay.substring(1, 2).equals("月")) {
			lunarDay = "初一";
		}
		return lunarDay;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
		//do something
			Intent intent = new Intent();			
			intent.setClass(ScheduleView.this, CalendarActivity.class);
			startActivity(intent);
		
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

		//do something
			
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {

		//这里操作是没有返回结果的
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
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
    			Calendar c=Calendar.getInstance();
    			now=c.getTime();
        		//封装要标记的日期
        		if(remindID == 0 ){
        			//"提醒一次","隔10分钟","隔30分钟","隔一小时"（只需标记当前这一天）
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
        			//每周重复(从设置日程的这天(星期几)，接下来的每周的这一天多要标记)
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
        			//每月重复(从设置日程的这天(几月几号)，接下来的每月的这一天多要标记)
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
        			//每年重复(从设置日程的这天(哪一年几月几号)，接下来的每年的这一天多要标记)
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
        		//将标记日期存入数据库中
        		
        		dao.saveTagDate(dateTagList);
        		
        	

            }
        });
        timeThread.start();
	
	}
}
