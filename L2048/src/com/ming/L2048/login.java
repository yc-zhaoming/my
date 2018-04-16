package com.ming.L2048;



import com.example.l2048.R;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class login extends Activity {
	public static  Ranking ranking;
	Button bt1;
	Button bt2;
	Button bt3;
	static int mode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		ranking=new Ranking(this);
		bt1=(Button)findViewById(R.id.start);
		bt2=(Button)findViewById(R.id.rankingList);
		bt3=(Button)findViewById(R.id.button1);
		final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		bt1.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				//AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				dialog.setTitle("妯″紡閫夋嫨")  ;
				dialog.setItems(new String[] {"       鏅�氭ā寮�","       闄愭妯″紡","       闄愭椂妯″紡"}, new AlertDialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int which) {
						// TODO Auto-generated method stub
						if(which==0){
							mode=0;
							Intent intent=new Intent(login.this,MainActivity.class);
							startActivity(intent);
						}
						else if(which==1){
							mode=1;
							Intent intent=new Intent(login.this,MainActivity.class);
							startActivity(intent);
						}
						else{
							mode=2;
							Intent intent=new Intent(login.this,MainActivity.class);
							startActivity(intent);
						}
					}
				}) ; 			            
				dialog.setNegativeButton("鍙栨秷", null)  ;
				dialog.show();
			}
		}
		);
		bt2.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent(login.this,RankingList.class);
				startActivity(intent);
			}
		}
		);
	
	}
}