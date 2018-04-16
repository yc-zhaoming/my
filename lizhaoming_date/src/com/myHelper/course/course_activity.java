/**
 * 
 */
package com.myHelper.course;

import java.sql.Date;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.myHelper.R;
import com.myHelper.borderText.BorderTextView;

public class course_activity extends Activity implements OnGestureListener{

	Button btupweek;
	Button btnextweek;
	ListView listView2;
	//菜单选项
	GestureDetector gd;
	public static final int NEW = Menu.FIRST;
	public static final int SET = Menu.FIRST + 1;
	public static final int DELETE = Menu.FIRST + 2;
	public static final int NOW = Menu.FIRST + 3;
	ListView listView; 
	//传�?确认的关键字
	private static final int REQUEST_SET = 0;
	private static final int REQUEST_NEW = 1;
	
	//默认的第�?���?��时间
	int first_year = 2010;
	int first_month = 9;
	int first_day = 1;	
	Date start_date = new Date(first_year,first_month,first_day);
	private int width;
	private int height;
	private int iwidth;
	private int iheight;
	 private LinearLayout centerlayout;
	//新建的课程信�?
	String course_name = "";
	String week_start  = "";
	String week_end  = "";
	String course_index1  = "";
	String course_place  = "";
	String week_index  = "";
	private ScrollView sv = null;
	//当前日期
	Calendar c = Calendar.getInstance();
	int now_year = c.get(Calendar.YEAR);
	int now_month = c.get(Calendar.MONTH);
	int now_day = c.get(Calendar.DAY_OF_MONTH);
	Date now_date = new Date(now_year,now_month,now_day);
	
	//现在是第几周
	int interval_weeks = get_interval_weeks(getFirstDate(), now_date);
	int now_week=get_now_week(getFirstDate(), now_date);
	int move_week=interval_weeks;
	int move_week_index=now_week;
	
	//数据库操
	private DbAdapter mDbHelper;
	private Cursor mCourseCursor;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("本周课程信息");
		gd=new GestureDetector(this);
		//setContentView(R.layout.course_activity);
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display dp = wm.getDefaultDisplay();
		width = dp.getWidth();
		height = dp.getHeight();
		iwidth=width/4;
		iheight=iwidth;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
		centerlayout = new LinearLayout(this);
		centerlayout.setLayoutParams(params);
		centerlayout.setOrientation(LinearLayout.VERTICAL);
		centerlayout.setBackgroundResource(R.drawable.mybackgroung);
		btupweek=new Button(this);
		btupweek.setHeight(40);
		btupweek.setWidth(480);
		btupweek.setText("上一周");
		btnextweek=new Button(this);
		btnextweek.setHeight(40);
		btnextweek.setWidth(480);
		btnextweek.setText("下一周");
		View view1=View.inflate(this, R.layout.course_button, null);
		mDbHelper = new DbAdapter(this);
		listView=new ListView(this);
		updateCourseView();
		final AlertDialog.Builder buider=new AlertDialog.Builder(this);	
		
		centerlayout.addView(btupweek);
		centerlayout.addView(listView);
		centerlayout.addView(btnextweek);
		
		sv = new ScrollView(this);
		sv.addView(centerlayout);
		
		setContentView(R.layout.courselistview);
		BorderTextView upweek=(BorderTextView)findViewById(R.id.upweek);
		BorderTextView nextweek=(BorderTextView)findViewById(R.id.nextweek);
		BorderTextView nextday=(BorderTextView)findViewById(R.id.nextday);
		BorderTextView upday=(BorderTextView)findViewById(R.id.upday);
		BorderTextView setting_course=(BorderTextView)findViewById(R.id.setting_course);
		listView=(ListView)findViewById(R.id.mylv); 
		updateCourseView();
		upweek.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(move_week!=1){
					move_week--;
					getMyCourse(move_week,move_week_index);
					}
					else {
						Toast.makeText(course_activity.this, "已经是第一周了！", 0).show();
					}
			}
		});
		upday.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(move_week_index!=1){
					move_week_index--;
					getMyCourse(move_week,move_week_index);
					}
					else {
						Toast.makeText(getApplicationContext(), "已经是星期一了！", 0).show();
					}
			}
		});
		nextday.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if(move_week_index!=7){
					move_week_index++;
					getMyCourse(move_week,move_week_index);
					}
					else {
						Toast.makeText(getApplicationContext(), "已经是星期天了！", 0).show();
					}
			}
		});
		nextweek.setOnClickListener(new View.OnClickListener() {
	
	@Override
		public void onClick(View v) {
		// TODO 自动生成的方法存根
		if(move_week!=20){
			move_week++;
			getMyCourse(move_week,move_week_index);
			}
			else {
				Toast.makeText(getApplicationContext(), "已经是第20周了！", 0).show();
			}
	}
});
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//				//	Toast.makeText(course_activity.this,"aaaa", Toast.LENGTH_LONG).show();
//					
//					//Toast.makeText(course_activity.this, "You click: ", Toast.LENGTH_SHORT).show();
//					mDbHelper.open();
//					mCourseCursor.moveToPosition(position);
//					buider.setMessage("是否刪除");
//					buider.setPositiveButton("是", new OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//						
//						mDbHelper.deleteCourse(mCourseCursor.getLong(mCourseCursor.getColumnIndex(DbAdapter.KEY_ROWID_COURSE)));
//						mDbHelper.closeclose();
//						updateCourseView();
//						}
//					});
//					buider.setNegativeButton("否",new OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							
//							mDbHelper.closeclose();
//						}
//					} );
//					buider.show();
//					
//				}});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 自动生成的方法存根
				mDbHelper.open();
				mCourseCursor.moveToPosition(position);
				buider.setMessage("是否刪除");
				buider.setPositiveButton("是", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					
					mDbHelper.deleteCourse(mCourseCursor.getLong(mCourseCursor.getColumnIndex(DbAdapter.KEY_ROWID_COURSE)));
					mDbHelper.closeclose();
					getMyCourse(move_week, move_week_index);
					}
				});
				buider.setNegativeButton("否",new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						mDbHelper.closeclose();
					}
				} );
				buider.show();
				return true;
			}
		});
		setting_course.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(course_activity.this).setTitle("选择")
				.setItems(new String[]{"新建","设置","回到今天"}, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						if(arg1==0){
							Intent new_course_intent = new Intent();
							 move_week=interval_weeks;
							 move_week_index=now_week;
							 new_course_intent.setClass(course_activity.this, course_new_activity.class);
							 startActivityForResult(new_course_intent, REQUEST_NEW);
							
						}
						if(arg1==1){
							Intent set_intent = new Intent();
							move_week=interval_weeks;
							 move_week_index=now_week;
							set_intent.setClass(course_activity.this, course_set_activity.class);
							startActivityForResult(set_intent, REQUEST_SET);
						}
						if(arg1==2){							
							updateCourseView();
							 move_week=interval_weeks;
							 move_week_index=now_week;
						}
					}
				})
				.show();
			}
		});
	}
	/**
	 * 更新listactivity的数�?
	 */
	private void updateCourseView() {
		// TODO Auto-generated method stub
		interval_weeks=get_interval_weeks(getFirstDate(), now_date);
		Log.e("done", "getcourse");
		mDbHelper.open();
		mCourseCursor = mDbHelper.getCourseOfWeek(interval_weeks,getWeekByNum(now_week).toString());		
		if(mCourseCursor.getCount()==0){
			setTitle("第"+interval_weeks+"周--星期"+now_week+"的课程信息");
			Toast.makeText(getApplicationContext(), "今天没有课,去玩吧", 0).show();
		}else{
		Toast.makeText(course_activity.this,  
                "当前是第"+interval_weeks +"周-星期"+now_week, Toast.LENGTH_SHORT).show();
		Log.e("weeks"," " +interval_weeks);
		Log.e("done", "donegetcourse");
		setTitle("第"+interval_weeks +""+"课程信息"+"--"+"今天是星期"+now_week);
		}
		startManagingCursor(mCourseCursor);
		
		String[] from = new String[] { DbAdapter.KEY_NAME, DbAdapter.KEY_PLACE, DbAdapter.KEY_INDEX, DbAdapter.KEY_WEEK_INDEX};
		int[] to = new int[] { R.id.item_name, R.id.item_place, R.id.item_index, R.id.item_week_index };
		SimpleCursorAdapter courses = new SimpleCursorAdapter(this,
				R.layout.course_list_item, mCourseCursor, from, to);
		//setListAdapter(courses);
		listView.setAdapter(courses);
		
		mDbHelper.closeclose();
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		 super.onCreateOptionsMenu(menu);
		 menu.add(0, NEW, 0, "新建").setIcon(R.drawable.new_course);
		menu.add(0, SET, 0, "设置").setIcon(R.drawable.setting);
		menu.add(0,NOW, 0, "回到今天").setIcon(R.drawable.setting);
		
		return true;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return gd.onTouchEvent(event);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case NOW: 
			 updateCourseView();
			 move_week=interval_weeks;
			 move_week_index=now_week;
			 return true;
		case NEW:
			 Intent new_course_intent = new Intent();
			 move_week=interval_weeks;
			 move_week_index=now_week;
			 new_course_intent.setClass(course_activity.this, course_new_activity.class);
			 startActivityForResult(new_course_intent, REQUEST_NEW);
			 return true;
		case SET: 
			Intent set_intent = new Intent();
			move_week=interval_weeks;
			 move_week_index=now_week;
			set_intent.setClass(course_activity.this, course_set_activity.class);
			startActivityForResult(set_intent, REQUEST_SET);
			 return true;
		
		}
		return super.onOptionsItemSelected(item);
	}
	
	//得到现在是第几周
	private int get_interval_weeks(Date ds, Date de)
	{
		   long total = (de.getTime()-ds.getTime())/(24*60*60*1000);
			Log.e("total"," " +total);
		   return ((int)(total/7.0) + 1);
	}
	private int get_now_week(Date ds, Date de)
	{
		   long total = (de.getTime()-ds.getTime())/(24*60*60*1000);
			Log.e("total"," " +total);
		  int result=((int)(total%7.0)+1 );
		  if(result==0) result=7;
		  return result;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		//设置第一周的回复
		if(requestCode == REQUEST_SET)
		{
			if(resultCode == RESULT_OK)
			{
				
				Bundle extras = data.getExtras();
				if(extras != null)
				{
					
					first_year = extras.getInt("year");
					first_month = extras.getInt("month");
					first_day = extras.getInt("day");
					start_date = new Date(first_year,first_month,first_day);
					Log.e("now_month"," " +now_month);
					Log.e("now_day"," " +now_day);
					interval_weeks = get_interval_weeks(start_date, now_date);
					now_week=get_now_week(start_date, now_date);
					move_week=interval_weeks;
					move_week_index=now_week;
			        Toast.makeText(course_activity.this,  
			                "当前是第"+interval_weeks +"周", Toast.LENGTH_LONG).show();
				//	setTitle("�?+interval_weeks +"�?"+"课程信息");
					Log.e("weeks"," " +interval_weeks);
					updateCourseView();
				}
				updateCourseView();
			}
		}
		
		//添加新课程信息的回复
		else if(requestCode == REQUEST_NEW)
		{
			if(resultCode == RESULT_OK)
			{
				updateCourseView();
			}
		}
	}
	Date getFirstDate() {
		Date date = new Date(0, 0, 0);
		int day = MainActivity.first_week_message.getFirstDate("first_day");
		int year = MainActivity.first_week_message.getFirstDate("first_year");
		int month = MainActivity.first_week_message.getFirstDate("first_month");
		date.setDate(day);
		date.setYear(year);
		date.setMonth(month);
		return date;
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO 自动生成的方法存根
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO 自动生成的方法存根
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO 自动生成的方法存根
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO 自动生成的方法存根
		float X=e2.getX()-e1.getX();
		float Y=e2.getY()-e1.getY();
		final int FLING_Min_DISTANCE =50;		

		
		if(X>FLING_Min_DISTANCE&&Math.abs(velocityX)>Math.abs(velocityY)){
		//right			
			if(move_week_index!=1){
			move_week_index--;
			getMyCourse(move_week,move_week_index);
			}
			else {
				Toast.makeText(this, "已经是星期一了！", 0).show();
			}
			return true;
		}else if(X<-FLING_Min_DISTANCE&&Math.abs(velocityX)>Math.abs(velocityY)){
		//left		
			if(move_week_index!=7){
				move_week_index++;
				getMyCourse(move_week,move_week_index);
				}
				else {
					Toast.makeText(this, "已经是星期天了！", 0).show();
				}
			return true;
		}else if(Y>FLING_Min_DISTANCE&&Math.abs(velocityX)<Math.abs(velocityY)){
		//buttom		
			if(move_week!=1){
				move_week--;
				getMyCourse(move_week,move_week_index);
				}
				else {
					Toast.makeText(this, "已经是第一周了！", 0).show();
				}
			return true;
		}else if(Y<-FLING_Min_DISTANCE&&Math.abs(velocityX)<Math.abs(velocityY)){
		//top				
			if(move_week!=20){
				move_week++;
				getMyCourse(move_week,move_week_index);
				}
				else {
					Toast.makeText(this, "已经是第20周了！", 0).show();
				}
			return true;
		}
		return false;
	}

public void getMyCourse(int week_number,int week_index){
	// TODO Auto-generated method stub
	
	Log.e("done", "getcourse");
	String a="";
	mDbHelper.open();
	mCourseCursor = mDbHelper.getCourseOfWeek(week_number,getWeekByNum(week_index).toString());   

	
	Log.e("weeks"," " +interval_weeks);
	Log.e("done", "donegetcourse");
	setTitle("第"+week_number +"周--星期"+week_index+"的课程信息");
	startManagingCursor(mCourseCursor);
	if(mCourseCursor.getCount()==0){
		Toast.makeText(getApplicationContext(), "今天没有课，可以睡懒觉了", 0).show();
	}
	String[] from = new String[] { DbAdapter.KEY_NAME, DbAdapter.KEY_PLACE, DbAdapter.KEY_INDEX, DbAdapter.KEY_WEEK_INDEX};
	int[] to = new int[] { R.id.item_name, R.id.item_place, R.id.item_index, R.id.item_week_index };
	SimpleCursorAdapter courses = new SimpleCursorAdapter(this,
			R.layout.course_list_item, mCourseCursor, from, to);
	//setListAdapter(courses);
	listView.setAdapter(courses);
	
	mDbHelper.closeclose();
}

	public String getWeekByNum(int num){
		if(num==1) return "周一";
		else if(num==2) return "周二";
		else if(num==3) return "周三";
		else if(num==4) return "周四";
		else if(num==5) return "周五";
		else if(num==6) return "周六";
		else return "周日";
		
	}
}



