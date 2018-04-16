package com.ming.L2048;

import android.app.Activity;
import android.widget.TextView;

public class MyTimeThread implements Runnable  {
	
	Activity activity;
	public MyTimeThread(Activity activity) {
		// TODO Auto-generated constructor stub
		this.activity=activity;
	}
	public void run()
	{
	
			
//		try {
//			MainActivity.shengyutime--;
//			MainActivity.time_bushu.setText(((Integer)MainActivity.shengyutime).toString()+"秒");
//			Thread.currentThread().sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		while(true){
		MainActivity.time_bushu.postDelayed(new Runnable() {  
			 			 
           // @Override            
            public void run() {  
                // TODO Auto-generated method stub 
            	MainActivity.shengyutime--;
            	MainActivity.time_bushu.setText(((Integer)MainActivity.shengyutime).toString()+"秒");  
            } 
        }, 1000);  
		
	}}
}
