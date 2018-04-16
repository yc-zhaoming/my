package com.ming.L2048;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.l2048.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity  implements OnGestureListener{
	private int width;
	private int heigth;	
	private int iwidth;
	private int iheight;
	private LinearLayout centerlayout;
	private List<ViewCell> lists = new ArrayList<ViewCell>();
	private int[][] arrays = new int[4][4];
	private int[][] arraysForUndo = new int[4][4];
	private int[][] arraysForBack = new int[4][4];
	int scoreback[]=new int[1000];
	private BackArray backArray[]=new BackArray[1000];
	private int bushu_time=500;//濮濄儲鏆熺拋鎯х暰
	static  int shengyutime=300;//閺冨爼妫跨拋鎯х暰
	private int backbushu=0;
	private Random random = new Random();//
	private int isfirst2048 =1;
	private int isstop=0;
	private GestureDetector gd;
//	private int testnumber=1;//濞村鐦弫鏉跨摟閺傝鐗搁崚鍡楃閹懎鍠�
	private boolean flag_move = false;
	private TextView tv_currenScore;
	private TextView tv_bestScore;
	static TextView time_bushu;
	private TextView shengyu;
	private Button undo;
	private int current_score = 0;
	private BestScode record;
	private   String name;
	EditText editText;
	private  Handler handler;
	private Thread timeThread;
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		//timeThread.stop();
		super.onCreate(savedInstanceState);
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display dp = wm.getDefaultDisplay();
		for (int i = 0; i < 1000; i++) {
			backArray[i]=new BackArray();
		}
		width = dp.getWidth();
		heigth = dp.getHeight();
		iwidth=width/4;
		iheight=iwidth;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
		centerlayout = new LinearLayout(this);
		centerlayout.setLayoutParams(params);
		centerlayout.setOrientation(LinearLayout.VERTICAL);
		centerlayout.setBackgroundColor(Color.WHITE);
		
		centerlayout.addView(getMainLayout());
		View scode =View.inflate(this, R.layout.main, null);
		centerlayout.addView(scode);
		setContentView(centerlayout);
		record = new BestScode(this);
		undo=(Button)findViewById(R.id.undo);
		undo=(Button)findViewById(R.id.undo);
		time_bushu=(TextView) findViewById(R.id.time);
		shengyu=(TextView) findViewById(R.id.textView6);
		shengyu.setVisibility(0x00000004);
		tv_currenScore = (TextView) findViewById(R.id.tv_currenScore);
		tv_bestScore = (TextView) findViewById(R.id.tv_bestScore);
		tv_bestScore.setText(record.getBestScode()+"");
		gd = new GestureDetector(this);		
		random2Or4();
		markNumForBack();
		backArray[0].setArrays(arraysForBack);	

		undo.setOnClickListener(new OnClickListener() {
					
			public void onClick(View arg0) {
				if(backbushu>0){
				current_score=current_score-scoreback[backbushu];
				tv_currenScore.setText(current_score+"");
				backbushu--;
			//	bushu--;
				}
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						ViewCell view = lists.get(i*4+j);
						view.setNumber(backArray[backbushu].getArrays()[i][j]);
						//view.setNumber(arraysForUndo[i][j]);
					}
				}
			if(login.mode==1){
				bushu_time++;
				time_bushu.setText(((Integer)bushu_time).toString());
			}		

			}
		});
		if(login.mode==1){
			time_bushu.setText(((Integer)(bushu_time)).toString()+"濮濓拷");
			shengyu.setVisibility(0x00000000);
			//timeThread.stop();
		}
		if(login.mode==2){
			time_bushu.setText(((Integer)(shengyutime)).toString()+"缁夛拷");
			shengyu.setVisibility(0x00000000);
			//new MyTimeThread().start();							
			//this.runOnUiThread(new MyTimeThread(MainActivity.this));
			/**
			 * 鐎规碍妞傞崳銊у殠缁嬶拷
			 */
		threadService();
		}
				
	}
	//闂呭繑婧�娴溠呮晸2閹达拷4
	private void random2Or4() {
		editText=new EditText(this);
		String name="";
		if(!getFillAll()){//濞屸�冲帠濠婏繝娼�0閺佹澘鐡�
			  

			int row = random.nextInt(4);
			int col = random.nextInt(4);
			ViewCell randomview = lists.get(row*4+col);
			if(randomview.getNumber()==0){
				int n2Or4 = (random.nextInt(2)+1)*2;
				randomview.setNumber(n2Or4);
				if(getFillAll()&&notAdd()){gg();}
			}else{
				random2Or4();
			}
		}
	}

	//鏉╂柨娲栨稉璇茬鐏烇拷
	private View getMainLayout() {
		LinearLayout colLayout = new LinearLayout(this);
		colLayout.setOrientation(LinearLayout.VERTICAL);
		for(int i=0;i<4;i++){
			colLayout.addView(getRowLayout());
		}
		return colLayout;
	}

	//鐞涳拷
	private View getRowLayout() {
		LinearLayout rowLayout = new LinearLayout(this);
		rowLayout.setOrientation(LinearLayout.HORIZONTAL);
//		rowLayout.setBackgroundColor(Color.GRAY);
		for(int i=0;i<4;i++){
			ViewCell viewcell = new ViewCell(this,iwidth,iheight);
			rowLayout.addView(viewcell);
			//濞村鐦弫鏉跨摟閺傝鐗搁幒鎺戝灙閹懎鍠�
//			viewcell.setNumber((int) Math.pow(2, testnumber));
//			testnumber++;
			lists.add(viewcell);
		}
		return rowLayout;
	}
	/**
	 * 閸掋倖鏌囬弰顖氭儊閸忓懏寮ч棃锟�0閺佹澘鐡�
	 * @return
	 */
	private boolean getFillAll(){
		markNumber();
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				if(arrays[i][j]==0){
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * 閸掋倖鏌囬弰顖氭儊閺堝娴夐柇缁樻殶鐎涙妲搁崥锔炬祲缁涘绱�
	 * @return 
	 */
	private boolean notAdd(){
		markNumber();
		boolean notadd = true;
		for(int i=0;i<4;i++){
			for(int j=0;j<3;j++){
				if(arrays[i][j]==arrays[i][j+1]&&arrays[i][j]!=0){
					notadd = false;
				}else if(arrays[j][i]==arrays[j+1][i]&&arrays[j][i]!=0){
					notadd = false;
				}
			}
		}
		return notadd;
	}


	public boolean onDown(MotionEvent e) {
		return false;
	}

	public void onShowPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	public void onLongPress(MotionEvent e) {
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float X=e2.getX()-e1.getX();
		float Y=e2.getY()-e1.getY();
		final int FLING_Min_DISTANCE =50;

		if(X>FLING_Min_DISTANCE&&Math.abs(velocityX)>Math.abs(velocityY)){
			toRight();
			Toast.makeText(this, "閸欙拷", 0).show();
		}else if(X<-FLING_Min_DISTANCE&&Math.abs(velocityX)>Math.abs(velocityY)){
			toLeft();
			Toast.makeText(this, "瀹革拷", 0).show();
		}else if(Y>FLING_Min_DISTANCE&&Math.abs(velocityX)<Math.abs(velocityY)){
			toDown();
			Toast.makeText(this, "娑擄拷", 0).show();
		}else if(Y<-FLING_Min_DISTANCE&&Math.abs(velocityX)<Math.abs(velocityY)){
			toUp();
			Toast.makeText(this, "娑擄拷", 0).show();
		}
		if(is2048()&&isfirst2048==1){
			final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle("濞撳憡鍨欑紒鎾存将閿涳拷");				
			
			builder2.setMessage("娴ｇ姾鑰芥禍锟�");										
			isfirst2048=0;											
			builder2.setPositiveButton("缂佈呯敾濞撳憡鍨�", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				if(login.mode==1||login.mode==2)
						{
							current_score = 0;
							tv_currenScore.setText(0+"");
							backbushu=0;
								bushu_time=500;
								if(login.mode==2)shengyutime=0;
								shengyutime=300;
							//閸掗攱鏌婇幒鎺曨攽濮掞拷
							for(int x=0;x<arrays.length;x++){
								for(int y=0;y<arrays[x].length;y++){
									ViewCell view = lists.get(x*4+y);
									view.setNumber(0);
								}
							}
							random2Or4();
							if(login.mode==1)time_bushu.setText(((Integer)bushu_time).toString()+"濮濓拷");
							if(login.mode==2)time_bushu.setText(((Integer)shengyutime).toString()+"缁夛拷");
							markNumForBack();
							backArray[0].setArrays(arraysForBack);		
							isfirst2048=1;
							if(login.mode==2)threadService();;
						}					
					}
					});
			builder2.setNegativeButton("闁拷閸戯拷", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
					
					System.exit(0);
						
						
						}
						});												
			builder2.show();
		}
			if(bushu_time==0){gg();}
			if(shengyutime==0){gg();}
		
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gd.onTouchEvent(event);
	}
	/**
	 * 閸氭垳绗傞崚鎺戝З
	 */
	private void toUp() {
		markNumber();
		markNumberForUndo();
		orderUp();
		//閸氬牆鑻熼惄绋挎倱閺佺増宓�
		int addscode = 0;
		for(int p =0;p<4;p++){
			for(int q=0;q<3;q++){
				if(arrays[q][p]==arrays[q+1][p]&&arrays[q][p]!=0){
					flag_move = true;
					arrays[q][p]+=arrays[q+1][p];
					addscode+=arrays[q][p];
					arrays[q+1][p]=0;
					q++;
				}
			}
		}
		
		orderUp();
		move2draw();
		
		if(flag_move){
			if(login.mode==1){
				bushu_time--;
				time_bushu.setText(((Integer)bushu_time).toString()+"濮濓拷");
			}
			
			backbushu++;
			markNumForBack();
			backArray[backbushu].setArrays(arraysForBack);
			}
		scoreback[backbushu]=addscode;		
		current_score+=addscode;
		tv_currenScore.setText(current_score+"");
		flag_move = false;
	}

	/**
	 * 閸氭垳绗呴崚鎺戝З
	 */
	private void toDown() {
		markNumber();
		 markNumberForUndo();
		//闁劒绔撮幒鎺戝灙
		orderDown();
		//閸氬牆鑻熼惄绋挎倱閺佺増宓�
		int addscode = 0;
		for(int p=0;p<4;p++){
			for(int q=3;q>0;q--){
				if(arrays[q][p]==arrays[q-1][p]&&arrays[q][p]!=0){
					flag_move = true;
					arrays[q][p]+=arrays[q-1][p];
					addscode+=arrays[q][p];
					arrays[q-1][p]=0;
					q--;
				}
			}
		}
		orderDown();
		move2draw();
		
		if(flag_move){
			if(login.mode==1){
				bushu_time--;
				time_bushu.setText(((Integer)bushu_time).toString()+"濮濓拷");
			}
		
			backbushu++;
			markNumForBack();
			backArray[backbushu].setArrays(arraysForBack);
			}	
			
			scoreback[backbushu]=addscode;
		current_score+=addscode;
		tv_currenScore.setText(current_score+"");
		flag_move = false;
	}

	/**
	 * 閸氭垵涔忛崚鎺戝З
	 */
	private void toLeft() {
		markNumber();
		 markNumberForUndo();
		orderLeft();
		//閸氬牆鑻熼惄鍝ョ搼閺佺増宓�
		int addscode = 0;
		for(int p =0;p<4;p++){
			for(int q=0;q<3;q++){
				if(arrays[p][q]==arrays[p][q+1]&&arrays[p][q]!=0){
					flag_move = true;
					arrays[p][q]+=arrays[p][q+1];
					addscode+=arrays[p][q];
					arrays[p][q+1]=0;
					q++;
				}
			}
		}
		orderLeft();
		move2draw();
	
		if(flag_move){
			if(login.mode==1){
				bushu_time--;
				time_bushu.setText(((Integer)bushu_time).toString()+"濮濓拷");
			}
		
			backbushu++;
			markNumForBack();
			backArray[backbushu].setArrays(arraysForBack);	
			}
			scoreback[backbushu]=addscode;
		current_score+=addscode;
		tv_currenScore.setText(current_score+"");
		flag_move = false;
	}


	/**
	 * 閸氭垵褰搁崚鎺戝З
	 */
	private void toRight() {
		Toast.makeText(this, "tight", 0).show();
		markNumber();
		 markNumberForUndo();
		orderRight();
		//閸氬牆鑻熼惄绋挎倱閺佺増宓�
		int addscode = 0;
		for(int p =0;p<4;p++){
			for(int q=3;q>0;q--){
				if(arrays[p][q]==arrays[p][q-1]&&arrays[p][q]!=0){
					flag_move = true;
					arrays[p][q]+=arrays[p][q-1];
					addscode+=arrays[p][q];
					arrays[p][q-1]=0;
					q--;
				}
			}
		}
		//orderRight();
		move2draw();	
		if(flag_move){
			if(login.mode==1){
				bushu_time--;
				time_bushu.setText(((Integer)bushu_time).toString()+"濮濓拷");
			}
		
			backbushu++;
			markNumForBack();
			backArray[backbushu].setArrays(arraysForBack);	
			}		
			scoreback[backbushu]=addscode;
		current_score+=addscode;
		tv_currenScore.setText(current_score+"");
		flag_move = false;
	}
	/**
	 * 閸氭垳绗傞崚鎺戝З閿涘本鏌熼弽鍏煎笓閸掓绱濋棃锟�0闂堢姴澧犻敍锟�0闂堢姴鎮楅敍灞筋洤0閵嗭拷2閵嗭拷0閵嗭拷4 閳ユ柡锟斤拷>2閵嗭拷4閵嗭拷0閵嗭拷0
	 */
	private void orderUp() {
		//闁劒绔撮幒鎺戝灙
		for(int n=0;n<4;n++){
			//閸愭帗鍦洪幒鎺戠碍濞夛拷
			for(int m=0;m<4;m++){
				for(int i=m+1;i<4;i++){
					if(arrays[m][n]==0&&arrays[i][n]!=0){
						flag_move = true;
						arrays[m][n]=arrays[i][n];
						arrays[i][n]=0;
					}
				}
			}
		}
	}
	/**
	 * 閸氭垳绗呴崚鎺戝З閿涘本鏌熼弽鍏煎笓閸掓绱濋棃锟�0闂堢姴澧犻敍锟�0闂堢姴鎮楅敍灞筋洤0閵嗭拷2閵嗭拷0閵嗭拷4 閳ユ柡锟斤拷>2閵嗭拷4閵嗭拷0閵嗭拷0
	 */
	private void orderDown() {
		for(int n=0;n<4;n++){
			//閸愭帗鍦洪幒鎺戠碍濞夛拷
			for(int m=3;m>=0;m--){
				for(int i=m-1;i>=0;i--){
					if(arrays[m][n]==0&&arrays[i][n]!=0){
						flag_move = true;
						arrays[m][n]=arrays[i][n];
						arrays[i][n]=0;
					}
				}
			}
		}
	}
	/**
	 * 閸氭垵涔忛崚鎺戝З閿涘本鏌熼弽鍏煎笓閸掓绱濋棃锟�0闂堢姴澧犻敍锟�0闂堢姴鎮楅敍灞筋洤0閵嗭拷2閵嗭拷0閵嗭拷4 閳ユ柡锟斤拷>2閵嗭拷4閵嗭拷0閵嗭拷0
	 */
	private void orderLeft() {
		//闁劒绔撮幒鎺戝灙
		for(int n=0;n<4;n++){
			//閸愭帗鍦洪幒鎺戠碍濞夛拷
			for(int m=0;m<4;m++){
				for(int i=m+1;i<4;i++){
					if(arrays[n][m]==0&&arrays[n][i]!=0){
						flag_move = true;
						arrays[n][m]=arrays[n][i];
						arrays[n][i]=0;
					}
				}
			}
		}
	}
	/**
	 * 閸氭垵褰搁崚鎺戝З閿涘本鏌熼弽鍏煎笓閸掓绱濋棃锟�0闂堢姴澧犻敍锟�0闂堢姴鎮楅敍灞筋洤0閵嗭拷2閵嗭拷0閵嗭拷4 閳ユ柡锟斤拷>2閵嗭拷4閵嗭拷0閵嗭拷0
	 */
	private void orderRight() {
		//闁劒绔撮幒鎺戝灙
		Toast.makeText(this, "orderRight", 0).show();
		for(int n=0;n<4;n++){
			//閸愭帗鍦洪幒鎺戠碍濞夛拷
			for(int m=3;m>=0;m--){
				for(int i=m-1;i>=0;i--){
					if(arrays[n][m]==0&&arrays[n][i]!=0){
						flag_move = true;
						arrays[n][m]=arrays[n][i];
						arrays[n][i]=0;
					}
				}
			}
		}
	}
	/**
	 * 鐠佹澘缍嶉弫鏉跨摟閺傝鐗哥悰銊よ厬閻ㄥ嫭鏆熺�涙ぜ锟斤拷
	 */
	private void markNumber() {
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				ViewCell view =lists.get(i*4+j);
				arrays[i][j] = view.getNumber();
			}
		}
	}
	/**
	 * 閸ョ偤锟斤拷
	 */
	private void markNumberForUndo() {
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				ViewCell view =lists.get(i*4+j);
				arraysForUndo[i][j] = view.getNumber();
			}
		}
	}
	private void markNumForBack(){
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				ViewCell view =lists.get(i*4+j);
				arraysForBack[i][j] = view.getNumber();
			}
		}
	}

	/**
	 * /閺堝些閸斻劍鏆熺�涙鏌熼弽鐓庢皑闁插秶鏁鹃弫鏉跨摟閺傝鐗哥悰銊ヨ嫙闂呭繑婧�娴溠呮晸2閹达拷4閺佹澘鐡ч弬瑙勭壐閿涘苯鎯侀崚娆欑礉娑撳秹鍣搁悽璇叉嫲娴溠呮晸閺佹澘鐡ч弬瑙勭壐閵嗭拷
	 */
	private void move2draw() {
		int count=0;
		if(flag_move){//閸掋倖鏌囬弰顖氭儊閺堝些閸旓拷
			//闁插秶鏁鹃弬瑙勭壐
			for(int x=0;x<arrays.length;x++){
				for(int y=0;y<arrays[x].length;y++){
					ViewCell view = lists.get(x*4+y);
					view.setNumber(arrays[x][y]);
					count++;
				}
			}
			random2Or4();
			Toast.makeText(this,Integer.toString(count), 0).show();
			//flag_move = false;
		}
		
	}
	public void save(EditText editText){
		if(current_score>record.getBestScode()){
			record.setBestScode(current_score);
			tv_bestScore.setText(current_score+"");
			login.ranking.setScode(login.ranking.getScode("Score4"), "Score5");
        	login.ranking.setScode(login.ranking.getScode("Score3"), "Score4");
        	login.ranking.setScode(login.ranking.getScode("Score2"), "Score3");
        	login.ranking.setScode(login.ranking.getScode("Score1"), "Score2");
          	login.ranking.setName(login.ranking.getName("Name4"), "Name5");
          	login.ranking.setName(login.ranking.getName("Name3"), "Name4");
          	login.ranking.setName(login.ranking.getName("Name2"), "Name3");
          	login.ranking.setName(login.ranking.getName("Name1"), "Name2");
        	login.ranking.setName(editText.getText().toString(), "Name1");
			
		}
		else{
			
	        if(current_score>login.ranking.getScode("Score2")){
	        	login.ranking.setScode(login.ranking.getScode("Score4"), "Score5");
	        	login.ranking.setScode(login.ranking.getScode("Score3"), "Score4");
	        	login.ranking.setScode(login.ranking.getScode("Score2"), "Score3");
	        	login.ranking.setScode(current_score, "Score2");
	          	login.ranking.setName(login.ranking.getName("Name4"), "Name5");
	          	login.ranking.setName(login.ranking.getName("Name3"), "Name4");
	          	login.ranking.setName(login.ranking.getName("Name2"), "Name3");
	        	login.ranking.setName(editText.getText().toString(), "Name2");
	        	
	        }
	    	else{
	    		   if(current_score>login.ranking.getScode("Score3")){
	    				login.ranking.setScode(login.ranking.getScode("Score4"), "Score5");
			        	login.ranking.setScode(login.ranking.getScode("Score3"), "Score4");
			        	login.ranking.setScode(current_score, "Score3");
			          	login.ranking.setName(login.ranking.getName("Name4"), "Name5");
			          	login.ranking.setName(login.ranking.getName("Name3"), "Name4");
			        	login.ranking.setName(editText.getText().toString(), "Name3");
			        	
			        }
		        
		    	else{
		    		   if(current_score>login.ranking.getScode("Score4")){
		    			   login.ranking.setScode(login.ranking.getScode("Score4"), "Score5");
				        	login.ranking.setScode(current_score, "Score4");
				        	login.ranking.setName(login.ranking.getName("Name4"), "Name5");
				        	login.ranking.setName(editText.getText().toString(), "Name4");
				        	
				        }
			    	else{
			    		   if(current_score>login.ranking.getScode("Score5")){					    			
					        	login.ranking.setScode(current_score, "Score5");
					        	login.ranking.setName(editText.getText().toString(), "Name5");
					        	
					        }
					}
				}
			}
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 閻╂垶甯�/閹凤附鍩�/鐏炲繗鏂�鏉╂柨娲栭柨锟�
		//do something
			if(login.mode==2){
				shengyutime=0;
				System.exit(0);}
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

		//do something
			
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {

		//鏉╂瑩鍣烽幙宥勭稊閺勵垱鐥呴張澶庣箲閸ョ偟绮ㄩ弸婊呮畱
		}
		return super.onKeyDown(keyCode, event);
		}
	public boolean is2048(){
		int temp=0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if(temp<lists.get(i*4+j).getNumber()){
					temp=lists.get(i*4+j).getNumber();
				}
			}
		}
		if(temp<2028)return false;
		else return true;
	}

	public void gg(){
		if(login.mode==2)isstop=1;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("濞撳憡鍨欑紒鎾存将閿涳拷");				
		if(current_score>login.ranking.getScode("Score5"))
		{
			//EditText editText=new EditText(this);
			builder.setMessage("閸掗攱鏌婇幒鎺曨攽濮掓粣绱掔拠鐤翻閸忋儱鎮曠�涳拷");
			builder.setIcon(android.R.drawable.ic_dialog_info).setView(editText);
			 name=editText.getText().toString().trim();						
		}
		else builder.setMessage("閺勵垰鎯佺紒褏鐢诲〒鍛婂灆閿涳拷");
		
		builder.setPositiveButton("缂佈呯敾濞撳憡鍨�", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
//				// TODO Auto-generated method stub
//				//缂佈呯敾濞撳憡鍨�
				for(int x=0;x<arrays.length;x++){
					for(int y=0;y<arrays[x].length;y++){
						ViewCell view = lists.get(x*4+y);
						view.setNumber(0);
					}
				}
			
				save(editText);	
				bushu_time=500;
				if(login.mode==2)isstop=0;
				shengyutime=300;
				if(login.mode==1)time_bushu.setText(((Integer)bushu_time).toString()+"濮濓拷");
				if(login.mode==2)time_bushu.setText(((Integer)shengyutime).toString()+"缁夛拷");
				shengyutime=300;
				current_score = 0;
				tv_currenScore.setText(0+"");
				random2Or4();
				//bushu=0;
				backbushu=0;
				markNumForBack();
				backArray[0].setArrays(arraysForBack);
				if(login.mode==2)threadService();
			}

			
		});
		builder.setNegativeButton("闁拷閸戠儤鐖堕幋锟�", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
//				// TODO Auto-generated method stub
//				//缂佈呯敾濞撳憡鍨�
				for(int x=0;x<arrays.length;x++){
					for(int y=0;y<arrays[x].length;y++){
						ViewCell view = lists.get(x*4+y);
						view.setNumber(0);
					}
				}
			
		save(editText);
   
				
			System.exit(0);					
			}

			
		});
		builder.setCancelable(false);//娑撳秷鍏橀幐澶愶拷锟介崶鐐烘暛閸忔娊妫�
		builder.show();
	
	}
	public void threadService(){
		handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case 0:
                    time_bushu.setText(msg.obj+"缁夛拷");
                }
            }
        };
        timeThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (shengyutime>0&&!is2048()&&isstop==0) {
                        Thread.currentThread().sleep(1000);
                        shengyutime--;
                        Message message = new Message();
                        message.obj = shengyutime;
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        timeThread.start();
	}
}
