package com.myHelper.sch_activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.myHelper.R;
import com.myHelper.borderText.BorderText;
import com.myHelper.course.MainActivity;
import com.myHelper.dao.ScheduleDAO;


public class CalendarActivity extends Activity implements OnGestureListener {

	public ViewFlipper flipper = null;
	private GestureDetector gestureDetector = null;
	private CalendarView calV = null;
	private GridView gridView = null;
	private BorderText topText = null;
	private Drawable draw = null;
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	boolean flag=true;
	private ScheduleDAO dao = null;
	

	public CalendarActivity() {

		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //当期日期
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
    	
    	dao = new ScheduleDAO(this);
    	
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
		gestureDetector = new GestureDetector(this);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.removeAllViews();
        calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
        
        addGridView();
        gridView.setAdapter(calV);
        //flipper.addView(gridView);
        flipper.addView(gridView,0);
        
		topText = (BorderText) findViewById(R.id.toptext);
		addTextToTopTextView(topText);
		
	
	}
	

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float X=e2.getX()-e1.getX();
		float Y=e2.getY()-e1.getY();
		final int FLING_Min_DISTANCE =50;
		int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记

		
		if(X>FLING_Min_DISTANCE&&Math.abs(velocityX)>Math.abs(velocityY)){
			
			if(year_c+jumpYear==1901&&(month_c+(jumpMonth%12))%12==1){
				Toast.makeText(getApplicationContext(), "不支持以下年份", 0).show();
			}
			else{
			addGridView();   //添加一个gridView
			jumpMonth--;     //shang一个月			
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        //flipper.addView(gridView);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView, gvFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
			this.flipper.showPrevious();
			flipper.removeViewAt(0);
			return true;
			}
		}else if(X<-FLING_Min_DISTANCE&&Math.abs(velocityX)>Math.abs(velocityY)){
			
			
			if(year_c+jumpYear==2049&&(month_c+(jumpMonth%12))%12==0){
				Toast.makeText(getApplicationContext(), "不支持以下年份", 0).show();
			}
			else{
			addGridView();   //添加一个gridView
			jumpMonth++;     //下一个月
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        //flipper.addView(gridView);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView, gvFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
			this.flipper.showNext();
			flipper.removeViewAt(0);
			return true;
			}
		}else if(Y>FLING_Min_DISTANCE&&Math.abs(velocityX)<Math.abs(velocityY)){
			
			if(year_c+jumpYear==1901){
				Toast.makeText(getApplicationContext(), "不支持以上年份", 0).show();
				
			}
			else{
			addGridView();   //添加一个gridView
			jumpYear--;     //上一年	
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        //flipper.addView(gridView);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView, gvFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_buttom_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_buttom_out));
			this.flipper.showPrevious();
			flipper.removeViewAt(0);
			//Toast.makeText(this, "下", 0).show();
			return true;}
	
		}else if(Y<-FLING_Min_DISTANCE&&Math.abs(velocityX)<Math.abs(velocityY)){
			
			if(year_c+jumpYear==2049){
				Toast.makeText(getApplicationContext(), "不支持以下年份", 0).show();
			}
			else{
			addGridView();   //添加一个gridView
			jumpYear++;     //下年		
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        //flipper.addView(gridView);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView, gvFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_top_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_top_out));
			this.flipper.showPrevious();
			flipper.removeViewAt(0);
			//Toast.makeText(this, "上", 0).show();
			return true;
			}
		}
		return false;
	}
	
	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, menu.FIRST, menu.FIRST, "今天");
		menu.add(0, menu.FIRST+1, menu.FIRST+1, "跳转");
		menu.add(0, menu.FIRST+2, menu.FIRST+2, "日程");
		menu.add(0, menu.FIRST+3, menu.FIRST+3, "日期转换");
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 选择菜单
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
        case Menu.FIRST:
        	//跳转到今天
        	
        	//startService(new Intent(CalendarActivity.this,MusicService.class));        	
        	int xMonth = jumpMonth;
        	int xYear = jumpYear;
        	int gvFlag =0;
        	jumpMonth = 0;
        	jumpYear = 0;
        	addGridView();   //添加一个gridView
        	year_c = Integer.parseInt(currentDate.split("-")[0]);
        	month_c = Integer.parseInt(currentDate.split("-")[1]);
        	day_c = Integer.parseInt(currentDate.split("-")[2]);
        	calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView,gvFlag);
	        if(xMonth == 0 && xYear == 0){
	        	//nothing to do
	        }else if((xYear == 0 && xMonth >0) || xYear >0){
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
				this.flipper.showNext();
	        }else{
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
				this.flipper.showPrevious();
	        }
			flipper.removeViewAt(0);
        	break;
        case Menu.FIRST+1:
        	
        	new DatePickerDialog(this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					//1901-1-1 ----> 2049-12-31
					if(year < 1901 || year > 2049){
						//不在查询范围内
						new AlertDialog.Builder(CalendarActivity.this).setTitle("错误日期").setMessage("跳转日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
					}else{
						int gvFlag = 0;
						addGridView();   //添加一个gridView
			        	calV = new CalendarView(CalendarActivity.this, CalendarActivity.this.getResources(),year,monthOfYear+1,dayOfMonth);
				        gridView.setAdapter(calV);
				        addTextToTopTextView(topText);
				        gvFlag++;
				        flipper.addView(gridView,gvFlag);
				        if(year == year_c && monthOfYear+1 == month_c){
				        	//nothing to do
				        }
				        if((year == year_c && monthOfYear+1 > month_c) || year > year_c ){
				        	CalendarActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_left_in));
				        	CalendarActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_left_out));
				        	CalendarActivity.this.flipper.showNext();
				        }else{
				        	CalendarActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_right_in));
				        	CalendarActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_right_out));
				        	CalendarActivity.this.flipper.showPrevious();
				        }
				        flipper.removeViewAt(0);
				        //跳转之后将跳转之后的日期设置为当期日期
				        year_c = year;
						month_c = monthOfYear+1;
						day_c = dayOfMonth;
						jumpMonth = 0;
						jumpYear = 0;
					}
				}
			},year_c, month_c-1, day_c).show();
        	break;
        case Menu.FIRST+2:
        	Intent intent = new Intent();
			intent.setClass(CalendarActivity.this, ScheduleAll.class);
			startActivity(intent);
        	break;
        case Menu.FIRST+3:
        	Intent intent1 = new Intent();
        	intent1.setClass(CalendarActivity.this, CalendarConvert.class);
        	intent1.putExtra("date", new int[]{year_c,month_c,day_c});
        	startActivity(intent1);
        	break;
        }
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//添加头部的年份 闰哪月等信息
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		draw = getResources().getDrawable(R.drawable.mylistviewbackground);
		view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowYear()).append("年").append(
				calV.getShowMonth()).append("月").append("\t");
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("闰").append(calV.getLeapMonth()).append("月")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("年").append("(").append(
				calV.getCyclical()).append("年)");
		view.setText(textDate);
		view.setTextColor(Color.RED);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	//添加gridview
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		//取得屏幕的宽度和高度
		WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth(); 
        int Height = display.getHeight();
        
		gridView = new GridView(this);
		gridView.setNumColumns(7);
		gridView.setColumnWidth(46);
		
		if(Width == 480 && Height == 800){
			gridView.setColumnWidth(69);
		}
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除gridView边框
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
        gridView.setBackgroundResource(R.drawable.gridview_bk);
		gridView.setOnTouchListener(new OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetector.onTouchEvent(event);
			}
		});

		
		gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView中的每一个item的点击事件
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {


		  //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
		  int startPosition = calV.getStartPositon();
		  int endPosition = calV.getEndPosition();
		  int gvFlag = 0;
		  if(position<startPosition){
			  if(year_c+jumpYear==1901&&(month_c+(jumpMonth%12))%12==1){
					Toast.makeText(getApplicationContext(), "不支持以下年份", 0).show();
				}
				else{
				addGridView();   //添加一个gridView
				jumpMonth--;     //shang一个月			
				calV = new CalendarView(CalendarActivity.this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		        gridView.setAdapter(calV);
		        //flipper.addView(gridView);
		        addTextToTopTextView(topText);
		        gvFlag++;
		        flipper.addView(gridView, gvFlag);
				flipper.setInAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_right_out));
				flipper.showPrevious();
				flipper.removeViewAt(0);
				}
		  }
		  if(position>endPosition){

				if(year_c+jumpYear==2049&&(month_c+(jumpMonth%12))%12==0){
					Toast.makeText(getApplicationContext(), "不支持以下年份", 0).show();
				}
				else{
				addGridView();   //添加一个gridView
				jumpMonth++;     //下一个月
				calV = new CalendarView(CalendarActivity.this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		        gridView.setAdapter(calV);
		        //flipper.addView(gridView);
		        addTextToTopTextView(topText);
		        gvFlag++;
		        flipper.addView(gridView, gvFlag);
				flipper.setInAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_left_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_left_out));
				flipper.showNext();
				flipper.removeViewAt(0);
				
				}
			
		  }
		  if(startPosition <= position  && position <= endPosition){
			  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
			  //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
            String scheduleYear = calV.getShowYear();
            String scheduleMonth = calV.getShowMonth();
            String week = "";
            switch(position%7){
            case 0:
          	  week = "星期日";
          	  break;
            case 1:
          	  week = "星期一";
          	  break;
            case 2:
          	  week = "星期二";
          	  break;
            case 3:
          	  week = "星期三";
          	  break;
            case 4:
          	  week = "星期四";
          	  break;
            case 5:
          	  week = "星期五";
          	  break;
            case 6:
          	  week = "星期六";
          	  break;
            }
            ArrayList<String> scheduleDate = new ArrayList<String>();
            scheduleDate.add(scheduleYear);
            scheduleDate.add(scheduleMonth);
            scheduleDate.add(scheduleDay);
            scheduleDate.add(week);
            //通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信息
            String[] scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
            if(scheduleIDs != null && scheduleIDs.length > 0){
          	  
          	  //跳转到显示这一天的所有日程信息界面
				  Intent intent = new Intent();
				  intent.setClass(CalendarActivity.this, ScheduleInfoView.class);
                intent.putExtra("scheduleID", scheduleIDs);
                intent.putStringArrayListExtra("scheduleDate", scheduleDate);
				  startActivity(intent);  
				  
            }else{
            //直接跳转到需要添加日程的界面
          	  
                //得到这一天是星期几
          
				 
              
                //scheduleDate.add(scheduleLunarDay);
                
                
                Intent intent = new Intent();
                intent.putStringArrayListExtra("scheduleDate", scheduleDate);
                intent.setClass(CalendarActivity.this, ScheduleView.class);
                startActivity(intent);
            }
		  }
	
		
		  }
		});
//		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				  //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
//				  int startPosition = calV.getStartPositon();
//				  int endPosition = calV.getEndPosition();
//				  if(startPosition <= position  && position <= endPosition){
//					  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
//					  //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
//	                  String scheduleYear = calV.getShowYear();
//	                  String scheduleMonth = calV.getShowMonth();
//	                  String week = "";
//	                  switch(position%7){
//	                  case 0:
//	                	  week = "星期日";
//	                	  break;
//	                  case 1:
//	                	  week = "星期一";
//	                	  break;
//	                  case 2:
//	                	  week = "星期二";
//	                	  break;
//	                  case 3:
//	                	  week = "星期三";
//	                	  break;
//	                  case 4:
//	                	  week = "星期四";
//	                	  break;
//	                  case 5:
//	                	  week = "星期五";
//	                	  break;
//	                  case 6:
//	                	  week = "星期六";
//	                	  break;
//	                  }
//	                  ArrayList<String> scheduleDate = new ArrayList<String>();
//	                  scheduleDate.add(scheduleYear);
//	                  scheduleDate.add(scheduleMonth);
//	                  scheduleDate.add(scheduleDay);
//	                  scheduleDate.add(week);
//	                  //通过日期查询这一天是否被标记，如果标记了日程就查询出这天的所有日程信息
//	                  String[] scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
//	                  if(scheduleIDs != null && scheduleIDs.length > 0){
//	                	  
//	                	  //跳转到显示这一天的所有日程信息界面
//		  				  Intent intent = new Intent();
//		  				  intent.setClass(CalendarActivity.this, ScheduleInfoView.class);
//		                  intent.putExtra("scheduleID", scheduleIDs);
//		                  intent.putStringArrayListExtra("scheduleDate", scheduleDate);
//		  				  startActivity(intent);  
//		  				  
//	                  }else{
//	                  //直接跳转到需要添加日程的界面
//	                	  
//		                  //得到这一天是星期几
//		            
//						 
//		                
//		                  //scheduleDate.add(scheduleLunarDay);
//		                  
//		                  
//		                  Intent intent = new Intent();
//		                  intent.putStringArrayListExtra("scheduleDate", scheduleDate);
//		                  intent.setClass(CalendarActivity.this, ScheduleView.class);
//		                  startActivity(intent);
//	                  }
//				  }
//			
//				return true;
//			}
//		});
		gridView.setLayoutParams(params);


	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
		//do something
			flag=false;
			Intent intent = new Intent();			
			intent.setClass(CalendarActivity.this, MainActivity.class);
			startActivity(intent);		
			
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

		//do something
			
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {

		//这里操作是没有返回结果的
		}
		return super.onKeyDown(keyCode, event);
		}
	
}