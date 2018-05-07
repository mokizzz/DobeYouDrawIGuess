package Dobe;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.sql.*;
import java.util.*;

//这个类用于总体调用，控制此程序运行;程序从这里开始(Main)
public class Controler extends Thread implements Serializable{
	private static final long serialVersionUID = 1L;
	static Start start;
	static MainFrame mainframe;
	static Controler controler;
	public static Player self;
	public static int basicscore;
	public static Administrator adm;
	PPlayer pplayer = new PPlayer();
	PDraw pdraw = new PDraw();
	PDrawTools pdrawtools = new PDrawTools();
	PChat pchat = new PChat();
	PGuide pguide = new PGuide();
	DobeServer dobeserver;
	DobeClient dobeclient;
	static boolean gamerunningFlag = false,hostFlag = false,myturn = false;//游戏是否运行中/是否主机/是否 my turn
	static String ciyu;//词语的暂存
	public Controler(){
		//空的构造方法
	}
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException	{
		//GUI优化
		for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
				UIManager.setLookAndFeel(info.getClassName());
				break;
			}
		}
		adm=new Administrator();
		
	} 
	void creatMainFrame(){
		mainframe = new MainFrame(controler);
	}
	public void HouseOpen() throws IOException{	//当点击“我要开房”时调用此方法
		hostFlag = true;
		this.creatMainFrame();//打开主界面
		this.dobeserver = new DobeServer();//使用那个方法
	}
	public void HouseJoin(String IP,Player p) throws UnknownHostException, IOException{	//当点击“加入房间”时调用此方法
		this.creatMainFrame();
		this.dobeclient = new DobeClient(IP,p);
	}
	public void GameStart(){//当游戏开始时调用此方法
		
	}
}
