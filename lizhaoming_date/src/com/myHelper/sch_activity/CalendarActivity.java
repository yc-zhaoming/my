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
	private static int jumpMonth = 0;      //ÿ�λ��������ӻ��ȥһ����,Ĭ��Ϊ0������ʾ��ǰ�£�
	private static int jumpYear = 0;       //������Խһ�꣬�����ӻ��߼�ȥһ��,Ĭ��Ϊ0(����ǰ��)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	boolean flag=true;
	private ScheduleDAO dao = null;
	

	public CalendarActivity() {

		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //��������
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
		int gvFlag = 0;         //ÿ�����gridview��viewflipper��ʱ���ı��

		
		if(X>FLING_Min_DISTANCE&&Math.abs(velocityX)>Math.abs(velocityY)){
			
			if(year_c+jumpYear==1901&&(month_c+(jumpMonth%12))%12==1){
				Toast.makeText(getApplicationContext(), "��֧���������", 0).show();
			}
			else{
			addGridView();   //���һ��gridView
			jumpMonth--;     //shangһ����			
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
				Toast.makeText(getApplicationContext(), "��֧���������", 0).show();
			}
			else{
			addGridView();   //���һ��gridView
			jumpMonth++;     //��һ����
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
				Toast.makeText(getApplicationContext(), "��֧���������", 0).show();
				
			}
			else{
			addGridView();   //���һ��gridView
			jumpYear--;     //��һ��	
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
			//Toast.makeText(this, "��", 0).show();
			return true;}
	
		}else if(Y<-FLING_Min_DISTANCE&&Math.abs(velocityX)<Math.abs(velocityY)){
			
			if(year_c+jumpYear==2049){
				Toast.makeText(getApplicationContext(), "��֧���������", 0).show();
			}
			else{
			addGridView();   //���һ��gridView
			jumpYear++;     //����		
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
			//Toast.makeText(this, "��", 0).show();
			return true;
			}
		}
		return false;
	}
	
	/**
	 * �����˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, menu.FIRST, menu.FIRST, "����");
		menu.add(0, menu.FIRST+1, menu.FIRST+1, "��ת");
		menu.add(0, menu.FIRST+2, menu.FIRST+2, "�ճ�");
		menu.add(0, menu.FIRST+3, menu.FIRST+3, "����ת��");
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * ѡ��˵�
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
        case Menu.FIRST:
        	//��ת������
        	
        	//startService(new Intent(CalendarActivity.this,MusicService.class));        	
        	int xMonth = jumpMonth;
        	int xYear = jumpYear;
        	int gvFlag =0;
        	jumpMonth = 0;
        	jumpYear = 0;
        	addGridView();   //���һ��gridView
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
						//���ڲ�ѯ��Χ��
						new AlertDialog.Builder(CalendarActivity.this).setTitle("��������").setMessage("��ת���ڷ�Χ(1901/1/1-2049/12/31)").setPositiveButton("ȷ��", null).show();
					}else{
						int gvFlag = 0;
						addGridView();   //���һ��gridView
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
				        //��ת֮����ת֮�����������Ϊ��������
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
	
	//���ͷ������� �����µ���Ϣ
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		draw = getResources().getDrawable(R.drawable.mylistviewbackground);
		view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowYear()).append("��").append(
				calV.getShowMonth()).append("��").append("\t");
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("��").append(calV.getLeapMonth()).append("��")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("��").append("(").append(
				calV.getCyclical()).append("��)");
		view.setText(textDate);
		view.setTextColor(Color.RED);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	//���gridview
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		//ȡ����Ļ�Ŀ�Ⱥ͸߶�
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
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // ȥ��gridView�߿�
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
        gridView.setBackgroundResource(R.drawable.gridview_bk);
		gridView.setOnTouchListener(new OnTouchListener() {
            //��gridview�еĴ����¼��ش���gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetector.onTouchEvent(event);
			}
		});

		
		gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView�е�ÿһ��item�ĵ���¼�
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {


		  //����κ�һ��item���õ����item������(�ų�����������յ�����(�������Ӧ))
		  int startPosition = calV.getStartPositon();
		  int endPosition = calV.getEndPosition();
		  int gvFlag = 0;
		  if(position<startPosition){
			  if(year_c+jumpYear==1901&&(month_c+(jumpMonth%12))%12==1){
					Toast.makeText(getApplicationContext(), "��֧���������", 0).show();
				}
				else{
				addGridView();   //���һ��gridView
				jumpMonth--;     //shangһ����			
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
					Toast.makeText(getApplicationContext(), "��֧���������", 0).show();
				}
				else{
				addGridView();   //���һ��gridView
				jumpMonth++;     //��һ����
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
			  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //��һ�������
			  //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //��һ�������
            String scheduleYear = calV.getShowYear();
            String scheduleMonth = calV.getShowMonth();
            String week = "";
            switch(position%7){
            case 0:
          	  week = "������";
          	  break;
            case 1:
          	  week = "����һ";
          	  break;
            case 2:
          	  week = "���ڶ�";
          	  break;
            case 3:
          	  week = "������";
          	  break;
            case 4:
          	  week = "������";
          	  break;
            case 5:
          	  week = "������";
          	  break;
            case 6:
          	  week = "������";
          	  break;
            }
            ArrayList<String> scheduleDate = new ArrayList<String>();
            scheduleDate.add(scheduleYear);
            scheduleDate.add(scheduleMonth);
            scheduleDate.add(scheduleDay);
            scheduleDate.add(week);
            //ͨ�����ڲ�ѯ��һ���Ƿ񱻱�ǣ����������ճ̾Ͳ�ѯ������������ճ���Ϣ
            String[] scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
            if(scheduleIDs != null && scheduleIDs.length > 0){
          	  
          	  //��ת����ʾ��һ��������ճ���Ϣ����
				  Intent intent = new Intent();
				  intent.setClass(CalendarActivity.this, ScheduleInfoView.class);
                intent.putExtra("scheduleID", scheduleIDs);
                intent.putStringArrayListExtra("scheduleDate", scheduleDate);
				  startActivity(intent);  
				  
            }else{
            //ֱ����ת����Ҫ����ճ̵Ľ���
          	  
                //�õ���һ�������ڼ�
          
				 
              
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
//				  //����κ�һ��item���õ����item������(�ų�����������յ�����(�������Ӧ))
//				  int startPosition = calV.getStartPositon();
//				  int endPosition = calV.getEndPosition();
//				  if(startPosition <= position  && position <= endPosition){
//					  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //��һ�������
//					  //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //��һ�������
//	                  String scheduleYear = calV.getShowYear();
//	                  String scheduleMonth = calV.getShowMonth();
//	                  String week = "";
//	                  switch(position%7){
//	                  case 0:
//	                	  week = "������";
//	                	  break;
//	                  case 1:
//	                	  week = "����һ";
//	                	  break;
//	                  case 2:
//	                	  week = "���ڶ�";
//	                	  break;
//	                  case 3:
//	                	  week = "������";
//	                	  break;
//	                  case 4:
//	                	  week = "������";
//	                	  break;
//	                  case 5:
//	                	  week = "������";
//	                	  break;
//	                  case 6:
//	                	  week = "������";
//	                	  break;
//	                  }
//	                  ArrayList<String> scheduleDate = new ArrayList<String>();
//	                  scheduleDate.add(scheduleYear);
//	                  scheduleDate.add(scheduleMonth);
//	                  scheduleDate.add(scheduleDay);
//	                  scheduleDate.add(week);
//	                  //ͨ�����ڲ�ѯ��һ���Ƿ񱻱�ǣ����������ճ̾Ͳ�ѯ������������ճ���Ϣ
//	                  String[] scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
//	                  if(scheduleIDs != null && scheduleIDs.length > 0){
//	                	  
//	                	  //��ת����ʾ��һ��������ճ���Ϣ����
//		  				  Intent intent = new Intent();
//		  				  intent.setClass(CalendarActivity.this, ScheduleInfoView.class);
//		                  intent.putExtra("scheduleID", scheduleIDs);
//		                  intent.putStringArrayListExtra("scheduleDate", scheduleDate);
//		  				  startActivity(intent);  
//		  				  
//	                  }else{
//	                  //ֱ����ת����Ҫ����ճ̵Ľ���
//	                	  
//		                  //�õ���һ�������ڼ�
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
		if (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ�
		//do something
			flag=false;
			Intent intent = new Intent();			
			intent.setClass(CalendarActivity.this, MainActivity.class);
			startActivity(intent);		
			
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

		//do something
			
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {

		//���������û�з��ؽ����
		}
		return super.onKeyDown(keyCode, event);
		}
	
}