package com.myHelper.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.myHelper.R;

public class diary_login_activity extends Activity{
	
	Button bt_save;
	TextView textView1;
	TextView textView2;
	EditText editText1;
	EditText editText2;
	int time=0;
	String passw;
	@Override	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.diary_welcome);
		//View view=View.inflate(this, R.layout.diary_welcome, null);
		bt_save=(Button) findViewById(R.id.bt_save);
		textView1=(TextView) findViewById(R.id.p1);
		textView2=(TextView) findViewById(R.id.p2);
		editText1=(EditText) findViewById(R.id.ps1);
		editText2=(EditText) findViewById(R.id.ps2);
		time=MainActivity.first_week_message.getFirstDate("isfirsttime");
		passw=MainActivity.first_week_message.getPassWord("password");
		if(time==0){
			textView2.setText("aa");
			textView2.setVisibility(View.INVISIBLE);
			editText2.setVisibility(View.INVISIBLE);
			//Toast.makeText(getApplicationContext(), Integer.toString(time), 0).show();
		}
		bt_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根		
				passw=MainActivity.first_week_message.getPassWord("password");
				if(time==1){
					if(editText1.getText()==null||editText1.getText().toString().equals(editText2.getText().toString())==false){
						Toast.makeText(getApplicationContext(), "请输入两次同样的密码", 0).show();
					}
					else{
						MainActivity.first_week_message.setFirstDate(0, "isfirsttime");
						MainActivity.first_week_message.setPssWord("password", editText1.getText().toString());
						startActivity(new Intent(diary_login_activity.this,diary_activity.class));
						Toast.makeText(getApplicationContext(), "保存密码成功", 0).show();
					}
				}
				else{
					if(editText1.getText().toString().equals(passw))
					{
						startActivity(new Intent(diary_login_activity.this,diary_activity.class));
					}
					else{
						Toast.makeText(getApplicationContext(), "密码不正确", 0).show();
					}
				}
			}
		});
}
}