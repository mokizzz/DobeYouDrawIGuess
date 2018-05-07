package Dobe;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

//主机专属运行的游戏线程类
public class GameThread extends Thread {
	final int ROUND = 3;//总共进行的回合数
	private int playerAmount,firstpainter;
	int countdown;
	private Vector<Player> players = Controler.controler.pplayer.players;
	private Random ran = new Random();
	public GameThread(){
		this.playerAmount = players.size();
		firstpainter = ran.nextInt(playerAmount);//选择起始画画玩家
		start();
	}
	public void run() {
		//游戏开始
		PChat chat = Controler.controler.pchat;
		String ciyu,tishi;	//这两个数据在后面从数据库提取
		String[] wordss=new String[2];
		chat.send(new Information(3,"<"+Controler.self.ID+">开始了游戏。\n"));
		this.sendAll(new Information(12,"Dobe游戏开始"));
		Controler.controler.pplayer.setAllScore();
		try {sleep(2000);}catch(InterruptedException e){e.printStackTrace();}				//2s后
		
		for(int i=0;i<ROUND;i++){
			for(int j=0;j<playerAmount;j++){
				int t = (j+firstpainter)%playerAmount;
				chat.send(new Information(3,"★★回合开始★★\n"));
				Controler.basicscore = 3;
				Controler.controler.pguide.guide.setText("第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID);
				this.sendAll(new Information(12,"第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID));
				this.sendAll(new Information(12,"第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID));
				this.sendAll(new Information(12,"第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID));
				
				//显示词语
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
				if(t==0){	//该自己准备画了
					Controler.controler.pguide.guide.setText("<html>轮到你画了<br>词语:"+ciyu+"<html>");
				} else {	//该别人准备画了
					send(new Information(12,"<html>轮到你画了<br>词语:"+ciyu+"<html>"),players.elementAt(t).socket);
					send(new Information(12,"<html>轮到你画了<br>词语:"+ciyu+"<html>"),players.elementAt(t).socket);
					send(new Information(12,"<html>轮到你画了<br>词语:"+ciyu+"<html>"),players.elementAt(t).socket);
				}
				//倒计时5s给画画者看词时间
				countdown = 5;
				for(int k=0;k<5;k++){
					Controler.controler.pguide.setnum(countdown);;
					this.sendAll(new Information(10,countdown));
					try {sleep(1000);}catch(InterruptedException e){e.printStackTrace();}	//5s后
					countdown--;
				}
				//游戏开始的初始化
				sendAll(new Information(7,ciyu));
				sendAll(new Information(7,ciyu));
				sendAll(new Information(7,ciyu));
				Controler.controler.pplayer.doumeidadui();
				players.elementAt(t).daduile = true;
				chat.send(new Information(3,"现在由<"+players.elementAt(t).ID+">画画。\n"));
				if(t==0){	//该自己画了
					Controler.myturn = true;
					Controler.controler.pdraw.resetListener();
					Controler.controler.pdrawtools.setButtonEnabled(true);
				} else {	//该别人画了
					send(new Information(6,true),players.elementAt(t).socket);
					send(new Information(6,true),players.elementAt(t).socket);
					send(new Information(6,true),players.elementAt(t).socket);
				}
				//游戏时间90秒																//开始游戏
				countdown = 90;
				//倒计时10s
				for(int k=0;k<10;k++){
					if(countdown<31)
						break;
					Controler.controler.pguide.setnum(countdown);;
					this.sendAll(new Information(10,countdown));
					try {sleep(1000);}catch(InterruptedException e){e.printStackTrace();}	//10s后
					countdown--;
				}
				//显示第一个提示
				if(t==0){
					this.sendAll(new Information(12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字<html>"));
					this.sendAll(new Information(12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字<html>"));
					this.sendAll(new Information(12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字<html>"));
				} else {
					Controler.controler.pguide.guide.setText("<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字<html>");
					this.sendAllExcept(new Information(12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字<html>"), t);
					this.sendAllExcept(new Information(12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字<html>"), t);
					this.sendAllExcept(new Information(12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字<html>"), t);
				}																			//提示1
				//倒计时10s
				for(int k=0;k<10;k++){
					if(countdown<31)
						break;
					Controler.controler.pguide.setnum(countdown);;
					this.sendAll(new Information(10,countdown));
					try {sleep(1000);}catch(InterruptedException e){e.printStackTrace();}	//10s后
					countdown--;
				}
				//显示第二个提示
				if(t==0){
					this.sendAll(new Information(12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字,"+tishi+"<html>"));
					this.sendAll(new Information(12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字,"+tishi+"<html>"));
					this.sendAll(new Information(12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字,"+tishi+"<html>"));
				} else {
					Controler.controler.pguide.guide.setText(
							"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字,"+tishi+"<html>");
					this.sendAllExcept(new Information(
							12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字,"+tishi+"<html>"), t);
					this.sendAllExcept(new Information(
							12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字,"+tishi+"<html>"), t);
					this.sendAllExcept(new Information(
							12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字,"+tishi+"<html>"), t);
				}																			//提示2
				//判断倒计时结束
				while(countdown>0){
					Controler.controler.pguide.setnum(countdown);;
					this.sendAll(new Information(10,countdown));
					try {sleep(1000);}catch(InterruptedException e){e.printStackTrace();}
					countdown--;
				}																			//游戏结束
				//游戏结束后打扫战场
				if(t==0){	//该自己不能画了
					Controler.myturn = false;
					Controler.controler.pdraw.resetListener();
					Controler.controler.pdrawtools.setButtonEnabled(false);
				} else {	//该别人不能画了
					send(new Information(6,false),players.elementAt(t).socket);
					send(new Information(6,false),players.elementAt(t).socket);
					send(new Information(6,false),players.elementAt(t).socket);
				}
				//显示答案
				Controler.ciyu = "";
				sendAll(new Information(7,""));
				sendAll(new Information(7,""));
				sendAll(new Information(7,""));
				chat.send(new Information(3,"答案是:"+ciyu+"\n"));
				Controler.controler.pguide.guide.setText(
						"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字,"+tishi+"<br>答案是:"+ciyu+"<html>");
				this.sendAll(new Information(
						12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字,"+tishi+"<br>答案是:"+ciyu+"<html>"));
				this.sendAll(new Information(
						12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字,"+tishi+"<br>答案是:"+ciyu+"<html>"));
				this.sendAll(new Information(
						12,"<html>第 "+(i+1)+"/"+ROUND+" 轮     绘画者:"+players.elementAt(t).ID+"<br>提示:"+ciyu.length()+"个字,"+tishi+"<br>答案是:"+ciyu+"<html>"));
				//休息5s
				try {sleep(5000);}catch(InterruptedException e){e.printStackTrace();}
				//清除画板
				Controler.controler.pdraw.clear = true;
				Controler.controler.pdraw.points.removeAllElements();
				Controler.controler.pdraw.repaint();
				sendAll(new Information(9,null));
				sendAll(new Information(9,null));
				sendAll(new Information(9,null));
			}
		}
		//游戏结束
			//显示得分
		Controler.controler.pguide.sethead(-1);
		this.sendAll(new Information(11,-1));
		Controler.gamerunningFlag = false;
		Controler.mainframe.kaishiyouxi.setEnabled(true);
		chat.send(new Information(3,"----游戏结束----"));
		Controler.controler.pguide.guide.setText("游戏结束");
		this.sendAll(new Information(12,"游戏结束"));
		Controler.controler.pplayer.setNoScore();
		System.out.println("游戏结束");
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
