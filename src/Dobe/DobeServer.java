package Dobe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

//我们在这里定义 PORT=55555
public class DobeServer {
	ServerSocket serversocket;
	Vector<DobeCommunication> dobes = new Vector<DobeCommunication>();
	static final int PORT = 55555;//端口
	public DobeServer() throws IOException{
		//创建服务器
		serversocket = new ServerSocket(PORT);
		System.out.println("服务器创建成功");
		//接受玩家加入消息
		new Thread(
			new Runnable(){
				public void run(){
					while(true){
						while(!Controler.gamerunningFlag){
							try{
								Socket socket = serversocket.accept();
								dobes.addElement(new DobeCommunication(socket));
							} catch (IOException e) {
								Controler.controler.pchat.chatcase.append("IO");
							}
						}
					}
				}
			}
		).start();
	}
	//与每个玩家的信号传输线程类
	class DobeCommunication extends Thread {
		Socket socket;
		Player player = null;
		ObjectInputStream in = null;
		public DobeCommunication(Socket socket) throws IOException{
			this.socket = socket;
			this.start();
		}
		public void run(){
			//实时传输，处理，获取消息
			while(true){
				try {
					in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					Object obj = in.readObject();
					if(obj!=null){
						Information information = (Information)obj;
						//处理information
						if(information.i==0) {		//玩家离开游戏
							Controler.controler.pplayer.removePlayer(player);
							Controler.controler.pchat.send(new Information(3,"<系统>:玩家<"+player.ID+">无情的离开了游戏。\n"));
							break;
						} else if(information.i==1) {	//新玩家加入
							addNewPlayer(information);//写入新玩家
							Controler.controler.pplayer.send();//发送所有玩家的列表
							Controler.controler.pchat.send(new Information(3,"<系统>:玩家<"+player.ID+">加入了房间。\n"));
							
							String str = "有"+Controler.controler.pplayer.players.size()+"位玩家在房间中";
							Controler.controler.pguide.guide.setText(str);
							new Thread(
								new Runnable(){
									public void run(){
										try {
											sleep(500);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										sendAll(new Information(12,str));
									}
								}
							).start();
						} else if(information.i==3) {	//服务器端的消息传输
							Controler.controler.pchat.send(information);
						} else if(information.i==4) {	//由客户端传来点信息
							Controler.controler.pdraw.points.addElement((OnePoint)information.object);
							Controler.controler.pdraw.repaint();
							Controler.mainframe.gamethread.sendAll(information);
						} else if(information.i==8) {	//有人猜对了答案
							if(Controler.mainframe.gamethread.countdown>30)
								Controler.mainframe.gamethread.countdown = 30;
							player.daduile = true;
							player.setScore(player.score+Controler.basicscore);
							if(Controler.basicscore>1)	Controler.basicscore--;
							sendAll(new Information(100+Controler.controler.pplayer.players.indexOf(player),player.score));
							Controler.controler.pplayer.doudaduileme();
						} else if(information.i==9) {	//pdraw clear
							Controler.controler.pdraw.clear = true;
							Controler.controler.pdraw.points.removeAllElements();
							Controler.controler.pdraw.repaint();
							Controler.mainframe.gamethread.sendAll(new Information(9,null));
						}
					}
				} catch (ClassNotFoundException | IOException e) {
					//e.printStackTrace();
				}
			}
			try {
				in.close();
				socket.close();
			} catch (IOException e) {
				System.out.println("socket关闭失败");
			}
			System.out.println("客户端关闭中...");
		}
		//将新玩家加入到PPlayer中的players列表中
		public void addNewPlayer(Information information){
			this.player = (Player)information.object;
			this.player.setSocket(socket);
			Controler.controler.pplayer.addPlayer(player);
		}
	}
	public void send(Information information,Socket socket) {
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
}