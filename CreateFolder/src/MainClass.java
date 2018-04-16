import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class MainClass implements ActionListener{

	/**
	 * @param args
	 */
	int count=0;
	static JFrame jf=new JFrame("这是一个问题");
	static JButton jb=new JButton("等待下班？？？？？？？");
	JLabel jl=new JLabel("");
	JLabel jl1=new JLabel("");
	Date date=new Date();
	myThread mt;
	static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public void getFrame(){
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.setLayout(null);
		jf.add(jb);
		jf.add(jl);
		jf.add(jl1);
		jl.setBounds(20, 200, 300, 100);
		jl.setFont(new Font("微软雅黑", 1, 24));
		jl1.setBounds(20, 250, 300, 100);
		jl1.setFont(new Font("微软雅黑", 1, 24));
		jf.setSize(400, 400);
		jb.setBounds(50, 50, 300, 50);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jl.setText(sdf.format(date));
		jf.setVisible(true);
		jb.addActionListener(this);
		mt=new myThread(jl,jl1);
		mt.start();
	}
	public static String getShengYuTime(Date d){
		long min=0;
		long second=0;
		try {
			Date now=new Date();

			Date date=sdf.parse("2018-04-14 12:00:00");
			long tst=date.getTime()-d.getTime();
			System.out.println("sss"+date.getTime());
			System.out.println("sss1"+d.getTime());
			min=tst/(60*1000);
			second=(tst%(60*1000))/1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(min==0&&second==0){
			
			JOptionPane.showMessageDialog(null, "下班");
			return "";
		}
		else
		return "距离下班时间："+min+"分"+second+"秒";
	}
	public static void main(String[] args) {
		int testNum=-1;
		try {
			new MyExc(testNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new MainClass().getFrame();
		// TODO Auto-generated method stub
//		String localPath="d:/新建文件夹/2016";
//		String localPathOfMobth="";
//		String localPathOfDay="";
//		File localFile=new File(localPath);
//		if(!localFile.exists()){
//			localFile.mkdirs();
//		}
//		for(int i=1;i<=12;i++){
//			if(i<10){
//			localPathOfMobth=localPath+"/"+"2016年0"+i+"月份相片";
//			new File(localPathOfMobth).mkdir();
//					}
//			else{
//				localPathOfMobth=localPath+"/"+"2016年"+i+"月份相片";
//				new File(localPathOfMobth).mkdir();
//			}
//			for(int j=1;j<=31;j++){
//				 localPathOfDay=localPathOfMobth+"/"+j;
//				 new File(localPathOfDay).mkdir();
//				 String localPathA=localPathOfDay+"/"+"A";
//				 new File(localPathA).mkdir();
//				 new File(localPathA+"/"+"附件").mkdir();
//				 String localPathB=localPathOfDay+"/"+"B";
//				 new File(localPathB).mkdir();
//				 new File(localPathB+"/"+"附件").mkdir();
//				}
//		}
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==jb){
			JOptionPane.showMessageDialog(null, "我错了，我还是记得的");
//			if(count==0){
//				mt.stop();count=1;}
//			
//			else if(count==1){
//				mt=new myThread(jl,jl1);
//				mt.start();count=0;}
		}
	}

}
class myThread extends Thread{
	JLabel label=new JLabel();
	JLabel label1=new JLabel();
	Date date=new Date();
	int i=1;
	SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss");
	public myThread(JLabel jl,JLabel j2){
		this.label=jl;
		this.label1=j2;
		}
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(true){
			try {
				Thread.currentThread().sleep(1000);
				date=new Date();
				label.setText("现在时间是："+sdf.format(date));
				label1.setText(MainClass.getShengYuTime(date));
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
		
	}
}
class MyExc extends Exception{
	
	public MyExc(int num) throws Exception{
		if(num<0){
			Exception ae=new Exception("aaaa");
			throw ae;
		}
	}
	
}
