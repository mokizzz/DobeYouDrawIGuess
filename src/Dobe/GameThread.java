package Dobe;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

//����ר�����е���Ϸ�߳���
public class GameThread extends Thread {
	final int ROUND = 3;//�ܹ����еĻغ���
	private int playerAmount,firstpainter;
	int countdown;
	private Vector<Player> players = Controler.controler.pplayer.players;
	private Random ran = new Random();
	public GameThread(){
		this.playerAmount = players.size();
		firstpainter = ran.nextInt(playerAmount);//ѡ����ʼ�������
		start();
	}
	public void run() {
		//��Ϸ��ʼ
		PChat chat = Controler.controler.pchat;
		String ciyu,tishi;	//�����������ں�������ݿ���ȡ
		String[] wordss=new String[2];
		chat.send(new Information(3,"<"+Controler.self.ID+">��ʼ����Ϸ��\n"));
		this.sendAll(new Information(12,"Dobe��Ϸ��ʼ"));
		Controler.controler.pplayer.setAllScore();
		try {sleep(2000);}catch(InterruptedException e){e.printStackTrace();}				//2s��
		
		for(int i=0;i<ROUND;i++){
			for(int j=0;j<playerAmount;j++){
				int t = (j+firstpainter)%playerAmount;
				chat.send(new Information(3,"���غϿ�ʼ���\n"));
				Controler.basicscore = 3;
				Controler.controler.pguide.guide.setText("�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID);
				this.sendAll(new Information(12,"�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID));
				this.sendAll(new Information(12,"�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID));
				this.sendAll(new Information(12,"�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID));
				
				//��ʾ����
				try{
				wordss=DobeSQL.getwords();
				}catch(Exception e){}
				ciyu =wordss[0];
				tishi =wordss[1];
				Controler.ciyu = ciyu;
				Controler.controler.pguide.sethead(players.elementAt(t).head);
				this.sendAll(new Information(11,t));
				this.sendAll(new Information(11,t));
				this.sendAll(new Information(11,t));
				if(t==0){	//���Լ�׼������
					Controler.controler.pguide.guide.setText("<html>�ֵ��㻭��<br>����:"+ciyu+"<html>");
				} else {	//�ñ���׼������
					send(new Information(12,"<html>�ֵ��㻭��<br>����:"+ciyu+"<html>"),players.elementAt(t).socket);
					send(new Information(12,"<html>�ֵ��㻭��<br>����:"+ciyu+"<html>"),players.elementAt(t).socket);
					send(new Information(12,"<html>�ֵ��㻭��<br>����:"+ciyu+"<html>"),players.elementAt(t).socket);
				}
				//����ʱ5s�������߿���ʱ��
				countdown = 5;
				for(int k=0;k<5;k++){
					Controler.controler.pguide.setnum(countdown);;
					this.sendAll(new Information(10,countdown));
					try {sleep(1000);}catch(InterruptedException e){e.printStackTrace();}	//5s��
					countdown--;
				}
				//��Ϸ��ʼ�ĳ�ʼ��
				sendAll(new Information(7,ciyu));
				sendAll(new Information(7,ciyu));
				sendAll(new Information(7,ciyu));
				Controler.controler.pplayer.doumeidadui();
				players.elementAt(t).daduile = true;
				chat.send(new Information(3,"������<"+players.elementAt(t).ID+">������\n"));
				if(t==0){	//���Լ�����
					Controler.myturn = true;
					Controler.controler.pdraw.resetListener();
					Controler.controler.pdrawtools.setButtonEnabled(true);
				} else {	//�ñ��˻���
					send(new Information(6,true),players.elementAt(t).socket);
					send(new Information(6,true),players.elementAt(t).socket);
					send(new Information(6,true),players.elementAt(t).socket);
				}
				//��Ϸʱ��90��																//��ʼ��Ϸ
				countdown = 90;
				//����ʱ10s
				for(int k=0;k<10;k++){
					if(countdown<31)
						break;
					Controler.controler.pguide.setnum(countdown);;
					this.sendAll(new Information(10,countdown));
					try {sleep(1000);}catch(InterruptedException e){e.printStackTrace();}	//10s��
					countdown--;
				}
				//��ʾ��һ����ʾ
				if(t==0){
					this.sendAll(new Information(12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����<html>"));
					this.sendAll(new Information(12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����<html>"));
					this.sendAll(new Information(12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����<html>"));
				} else {
					Controler.controler.pguide.guide.setText("<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����<html>");
					this.sendAllExcept(new Information(12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����<html>"), t);
					this.sendAllExcept(new Information(12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����<html>"), t);
					this.sendAllExcept(new Information(12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����<html>"), t);
				}																			//��ʾ1
				//����ʱ10s
				for(int k=0;k<10;k++){
					if(countdown<31)
						break;
					Controler.controler.pguide.setnum(countdown);;
					this.sendAll(new Information(10,countdown));
					try {sleep(1000);}catch(InterruptedException e){e.printStackTrace();}	//10s��
					countdown--;
				}
				//��ʾ�ڶ�����ʾ
				if(t==0){
					this.sendAll(new Information(12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����,"+tishi+"<html>"));
					this.sendAll(new Information(12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����,"+tishi+"<html>"));
					this.sendAll(new Information(12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����,"+tishi+"<html>"));
				} else {
					Controler.controler.pguide.guide.setText(
							"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����,"+tishi+"<html>");
					this.sendAllExcept(new Information(
							12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����,"+tishi+"<html>"), t);
					this.sendAllExcept(new Information(
							12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����,"+tishi+"<html>"), t);
					this.sendAllExcept(new Information(
							12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����,"+tishi+"<html>"), t);
				}																			//��ʾ2
				//�жϵ���ʱ����
				while(countdown>0){
					Controler.controler.pguide.setnum(countdown);;
					this.sendAll(new Information(10,countdown));
					try {sleep(1000);}catch(InterruptedException e){e.printStackTrace();}
					countdown--;
				}																			//��Ϸ����
				//��Ϸ�������ɨս��
				if(t==0){	//���Լ����ܻ���
					Controler.myturn = false;
					Controler.controler.pdraw.resetListener();
					Controler.controler.pdrawtools.setButtonEnabled(false);
				} else {	//�ñ��˲��ܻ���
					send(new Information(6,false),players.elementAt(t).socket);
					send(new Information(6,false),players.elementAt(t).socket);
					send(new Information(6,false),players.elementAt(t).socket);
				}
				//��ʾ��
				Controler.ciyu = "";
				sendAll(new Information(7,""));
				sendAll(new Information(7,""));
				sendAll(new Information(7,""));
				chat.send(new Information(3,"����:"+ciyu+"\n"));
				Controler.controler.pguide.guide.setText(
						"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����,"+tishi+"<br>����:"+ciyu+"<html>");
				this.sendAll(new Information(
						12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����,"+tishi+"<br>����:"+ciyu+"<html>"));
				this.sendAll(new Information(
						12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����,"+tishi+"<br>����:"+ciyu+"<html>"));
				this.sendAll(new Information(
						12,"<html>�� "+(i+1)+"/"+ROUND+" ��     �滭��:"+players.elementAt(t).ID+"<br>��ʾ:"+ciyu.length()+"����,"+tishi+"<br>����:"+ciyu+"<html>"));
				//��Ϣ5s
				try {sleep(5000);}catch(InterruptedException e){e.printStackTrace();}
				//�������
				Controler.controler.pdraw.clear = true;
				Controler.controler.pdraw.points.removeAllElements();
				Controler.controler.pdraw.repaint();
				sendAll(new Information(9,null));
				sendAll(new Information(9,null));
				sendAll(new Information(9,null));
			}
		}
		//��Ϸ����
			//��ʾ�÷�
		Controler.controler.pguide.sethead(-1);
		this.sendAll(new Information(11,-1));
		Controler.gamerunningFlag = false;
		Controler.mainframe.kaishiyouxi.setEnabled(true);
		chat.send(new Information(3,"----��Ϸ����----"));
		Controler.controler.pguide.guide.setText("��Ϸ����");
		this.sendAll(new Information(12,"��Ϸ����"));
		Controler.controler.pplayer.setNoScore();
		System.out.println("��Ϸ����");
	}
	public void send(Information information,Socket socket){
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.writeObject(information);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void sendAll(Information information){
		ObjectOutputStream out = null;
		for(int i=1;i<Controler.controler.pplayer.players.size();i++){
			try {
				out = new ObjectOutputStream(new BufferedOutputStream(
						Controler.controler.pplayer.players.elementAt(i).socket.getOutputStream()));
				out.writeObject(information);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void sendAllExcept(Information information,int t){
		ObjectOutputStream out = null;
		for(int i=1;i<Controler.controler.pplayer.players.size();i++){
			if(i!=t){
				try {
					out = new ObjectOutputStream(new BufferedOutputStream(
							Controler.controler.pplayer.players.elementAt(i).socket.getOutputStream()));
					out.writeObject(information);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
