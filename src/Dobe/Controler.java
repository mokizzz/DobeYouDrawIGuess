package Dobe;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.sql.*;
import java.util.*;

//���������������ã����ƴ˳�������;��������￪ʼ(Main)
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
	static boolean gamerunningFlag = false,hostFlag = false,myturn = false;//��Ϸ�Ƿ�������/�Ƿ�����/�Ƿ� my turn
	static String ciyu;//������ݴ�
	public Controler(){
		//�յĹ��췽��
	}
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException	{
		//GUI�Ż�
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
	public void HouseOpen() throws IOException{	//���������Ҫ������ʱ���ô˷���
		hostFlag = true;
		this.creatMainFrame();//��������
		this.dobeserver = new DobeServer();//ʹ���Ǹ�����
	}
	public void HouseJoin(String IP,Player p) throws UnknownHostException, IOException{	//����������뷿�䡱ʱ���ô˷���
		this.creatMainFrame();
		this.dobeclient = new DobeClient(IP,p);
	}
	public void GameStart(){//����Ϸ��ʼʱ���ô˷���
		
	}
}
