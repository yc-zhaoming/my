package com.myHelper.course;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class first_week_message {

	
	 SharedPreferences sp;
	public first_week_message(Context context){
		
		sp = context.getSharedPreferences("first_year", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("first_month", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("first_day", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("password", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("isfirsttime", Context.MODE_PRIVATE);
	
		
	}
	public int getFirstDate(String str){
		int scode = sp.getInt(str, 1);
		return scode;
	}
	public String getPassWord(String s) {
		String passWord=sp.getString(s, "123456");
		return passWord;
	}
//	public String getFirstDate(String str){
//		String s=sp.getString(str, "1");
//		return s;
//	}
	public void setPssWord(String key,String value){
		Editor editor = sp.edit();
		editor.putString(key, value);		
		editor.commit();
	}
	public void setFirstDate(int Scode,String str){
		Editor editor = sp.edit();
		editor.putInt(str, Scode);
		editor.commit();
	}
//	public void setFirstDate(String name,String str){
//		Editor editor = sp.edit();
//		editor.putString(str, name);
//		editor.commit();
//	}

}
