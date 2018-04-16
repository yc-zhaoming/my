/**
 * 
 */
package com.myHelper.course;



import com.myHelper.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class course_new_activity extends Activity {
	
	//���ݿ����
	private DbAdapter mDbHelper;
	
	//�γ���Ϣ��Ĭ������
	String course_name = "default";;
	String week_start = "1";
	String week_end = "15";
	String course_index = "1";
	String week_index = "��һ";
	CharSequence course_place = "��ѧ¥";
	
	//TextWatcher place_watcher;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mDbHelper = new DbAdapter(this);
		
		setContentView(R.layout.course_new);
		setTitle("�½�һ���γ���Ϣ");
		
		//�γ����Զ���������
		final AutoCompleteTextView course_name_autocompelete = (AutoCompleteTextView) findViewById(R.id.course_input_autocompelte);
		course_name_autocompelete.setThreshold(1);
		String[] course_name_array = getResources().getStringArray(R.array.course_name_array);
		final ArrayAdapter<String> name_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, course_name_array);
		course_name_autocompelete.setAdapter(name_adapter);
		
		//�õ��γ�����
		course_name_autocompelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(course_name_autocompelete.hasFocus()==true){
					course_name_autocompelete.setText("");
				}
				
			}
		});
	
		course_name_autocompelete.setOnItemClickListener(new OnItemClickListener() {
			
		    @Override 
		    public void onItemClick(AdapterView parent, View view, int position, long id) { 	 
		    	
		    	course_name = name_adapter.getItem(position); 
		    
		    };
		});
	//	course_name=course_name_autocompelete.getText().toString();
	
		//ȷ�ϰ�ť
		Button btn_course_new_confirm = (Button) findViewById(R.id.btn_course_new_confirm);
		btn_course_new_confirm.setOnClickListener(btn_confirm_listener);
		
		//�ܴ�ѡ��
		final ArrayAdapter<CharSequence> week_adapter = ArrayAdapter.createFromResource(
				this, R.array.week_array, android.R.layout.simple_spinner_dropdown_item);
		week_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//��ʼ��
		Spinner course_time_start_spinner = (Spinner) findViewById(R.id.course_time_start_spinner);		
		course_time_start_spinner.setAdapter(week_adapter);
		
		
		OnItemSelectedListener start_listener = new  OnItemSelectedListener(){  
		    	//week_start = week_adapter.getItemAtPosition(arg2).toString(); 
		    	
		    	 public void onItemSelected(AdapterView<?> adapter,View v,   
		                 int pos, long id) {   
		    		 week_start = week_adapter.getItem(pos).toString();
		         }   

		         public void onNothingSelected(AdapterView<?> arg0) {   
		             // TODO Auto-generated method stub   
		               week_start = "1";
		         }   
		};
		 course_time_start_spinner.setOnItemSelectedListener(start_listener);
		
		
				
		//������
		Spinner course_time_end_spinner = (Spinner) findViewById(R.id.course_time_end_spinner);
		course_time_end_spinner.setAdapter(week_adapter);
		
		OnItemSelectedListener end_listener = new  OnItemSelectedListener(){  
	    	//week_start = week_adapter.getItemAtPosition(arg2).toString(); 
	    	
	    	 public void onItemSelected(AdapterView<?> adapter,View v,   
	                 int pos, long id) {   
	    		 week_end =  week_adapter.getItem(pos).toString();
	         }   

	         public void onNothingSelected(AdapterView<?> arg0) {   
	             // TODO Auto-generated method stub   
	        	 week_end= "1";
	         }   
		};
		course_time_end_spinner.setOnItemSelectedListener(end_listener);
		
		//�γ̽ڴ�ѡ��
		Spinner course_index_spinner = (Spinner) findViewById(R.id.course_index_spinner);
		course_index_spinner.setAdapter(week_adapter);
		
		OnItemSelectedListener index_listener = new  OnItemSelectedListener(){  
	    	//week_start = week_adapter.getItemAtPosition(arg2).toString(); 
	    	
	    	 public void onItemSelected(AdapterView<?> adapter,View v,   
	                 int pos, long id) {   
	    		 course_index =  "��" + week_adapter.getItem(pos).toString() + "��";
	         }   

	         public void onNothingSelected(AdapterView<?> arg0) {   
	             // TODO Auto-generated method stub   
	        	 course_index= "1";
	         }   
		};
		course_index_spinner.setOnItemSelectedListener(index_listener);
		
		//�γ��ܴ�
		final ArrayAdapter<CharSequence> week_index_adapter = ArrayAdapter.createFromResource(
				this, R.array.week_index_array, android.R.layout.simple_spinner_dropdown_item);
		week_index_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner week_index_spinner = (Spinner) findViewById(R.id.week_index_spinner);
		week_index_spinner.setAdapter(week_index_adapter);
		
		OnItemSelectedListener week_index_spinner_listener = new  OnItemSelectedListener(){  
	    	//week_start = week_adapter.getItemAtPosition(arg2).toString(); 
	    	
	    	 public void onItemSelected(AdapterView<?> adapter,View v,   
	                 int pos, long id) {   
	    		 week_index =  week_index_adapter.getItem(pos).toString();
	         }   

	         public void onNothingSelected(AdapterView<?> arg0) {   
	             // TODO Auto-generated method stub   
	        	 week_index= "��һ";
	         }   
		};
		week_index_spinner.setOnItemSelectedListener(week_index_spinner_listener);
		
		
		//place
		EditText course_place_edittext = (EditText)findViewById(R.id.course_place_edittext);
		course_place = course_place_edittext.getText();
		Log.e("place", course_place.toString());
	}

	//��ť��������
	private Button.OnClickListener btn_confirm_listener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setTitle(course_name + "\\" + course_place+ "\\"  +week_start+ "\\"+week_end + "\\"+ course_index);						
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			Log.e("name", ""+course_name);
			Log.e("start", week_start);
			Log.e("end", week_end);
			Log.e("index", course_index);
			Log.e("place", course_place.toString());
			mDbHelper.open();
			mDbHelper.createCourse(course_name, Integer.parseInt(week_start), Integer.parseInt(week_end), course_index, course_place.toString(),week_index);
			mDbHelper.closeclose();
			finish();
		}
	};
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // ���/����/���η��ؼ�
		//do something
			Intent intent = new Intent();
			
			setResult(RESULT_OK, intent);
			finish();
		
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

		//do something
			
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {

		//���������û�з��ؽ����
		}
		return super.onKeyDown(keyCode, event);
		}
}



