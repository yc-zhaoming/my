package com.ming.L2048;

import java.util.ArrayList;
import java.util.List;

import com.example.l2048.R;

import android.app.Activity;
import android.content.Context;
import android.content.Loader.ForceLoadContentObserver;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class RankingList extends Activity {
	private int width;
	private int height;
	private int iwidth;
	private int iheight;
    private ListView listView;
    private LinearLayout centerlayout;
    //public static  Ranking ranking;
		
    //private List<String> data = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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
		centerlayout.setBackgroundColor(Color.WHITE);
        listView = new ListView(this);
       
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));
        View addview=View.inflate(this,R.layout.bestscorelist, null);
        centerlayout.addView(addview);
        centerlayout.addView(listView);
        setContentView(centerlayout);
    
        
    }
     
      
    private List<String> getData(){
         	
        List<String> data = new ArrayList<String>();
        SharedPreferences sp=getSharedPreferences("bestscode",Context.MODE_PRIVATE);
        int  i=sp.getInt("bestscode", 0);       
        login.ranking.setScode(i, "Score1");
        data.add(((Integer)(login.ranking.getScode("Score1"))).toString()+"            "+login.ranking.getName("Name1"));
        data.add(((Integer)(login.ranking.getScode("Score2"))).toString()+"            "+login.ranking.getName("Name2"));
        data.add(((Integer)(login.ranking.getScode("Score3"))).toString()+"            "+login.ranking.getName("Name3"));
        data.add(((Integer)(login.ranking.getScode("Score4"))).toString()+"            "+login.ranking.getName("Name4"));
        data.add(((Integer)(login.ranking.getScode("Score5"))).toString()+"            "+login.ranking.getName("Name5"));
    
         
        return data;
    }

    
}
class Ranking {
	
	 SharedPreferences sp;
	public Ranking(Context context){
		
		sp = context.getSharedPreferences("Score1", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("Score2", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("Score3", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("Score4", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("Score5", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("Name1", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("Name2", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("Name3", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("Name4", Context.MODE_PRIVATE);
		sp = context.getSharedPreferences("Name5", Context.MODE_PRIVATE);
	}
	public int getScode(String str){
		int scode = sp.getInt(str, 0);
		return scode;
	}
	public String getName(String str){
		String s=sp.getString(str, "noname");
		return s;
	}
	public void setScode(int Scode,String str){
		Editor editor = sp.edit();
		editor.putInt(str, Scode);
		editor.commit();
	}
	public void setName(String name,String str){
		Editor editor = sp.edit();
		editor.putString(str, name);
		editor.commit();
	}
}

