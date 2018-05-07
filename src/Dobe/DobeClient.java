package Dobe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class DobeClient {
	Socket socket;
	static final int PORT = 55555;
	public DobeClient(String host,Player p) throws UnknownHostException, IOException{
		socket = new Socket(host,PORT);
		System.out.println("客户端创建成功");
		this.send(new Information(1,Controler.self));//发送给服务器自己的信息
		System.out.println("客户端申请加入主机");
		new Thread(
				new Runnable(){
					//接收处理来自服务器的消息
					public void run(){
						while(true){
							ObjectInputStream in;
							try {
								in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
								Object obj = in.readObject();
								if(obj!=null){
									Information information = (Information)obj;
									//判断消息类型
									if(information.i==0){		//主机离开游戏
										send(new Information(-1,0));
										Controler.controler.pchat.chatcase.append("<系统>:完蛋了！房主退出了游戏，带着他的小姨子跑了！我们散伙吧(＞﹏＜)！\n");
										break;
									} else if(information.i==2) {	//游戏玩家信息传输
										send(new Information(-1,2));
										Vector<Player> pl = new Vector<Player>();
										int a;	String b;
										for(int j=0;j<(int)information.object;j++){
											a = in.readInt();
											b = (String)in.readUTF();
											pl.addElement(new Player(a,b));
										}
										Controler.controler.pplayer.setPlayers(pl);
									} else if(information.i==3) {	//游戏消息传输
										send(new Information(-1,3));
										Controler.controler.pchat.chatcase.append((String)information.object);
										Controler.controler.pchat.chatcase.setCaretPosition(Controler.controler.pchat.chatcase.getText().length());
									} else if(information.i==4) {	//画板传输
										send(new Information(-1,4));
										if(!Controler.myturn){
											Controler.controler.pdraw.points.addElement((OnePoint)information.object);
											Controler.controler.pdraw.repaint();
										}
									} else if(information.i==5) {	//绘画者的词语信息/显示词语
										send(new Information(-1,5));
										Controler.controler.pchat.chatcase.append("词语:"+(String)information.object);
									} else if(information.i==6) {	//该不该你画画了
										send(new Information(-1,6));
										Controler.myturn = (boolean)information.object;
										Controler.controler.pdraw.resetListener();
										Controler.controler.pdrawtools.setButtonEnabled((boolean)information.object);
									} else if(information.i==7) {	//传输正确词语
										send(new Information(-1,7));
										Controler.ciyu = (String)information.object;
									} else if(information.i==9) {	//pdraw clear
										send(new Information(-1,9));
										Controler.controler.pdraw.clear = true;
										Controler.controler.pdraw.points.removeAllElements();
										Controler.controler.pdraw.repaint();
									} else if(information.i==10) {	//倒计时数字
										send(new Information(-1,10));
										Controler.controler.pguide.setnum((int)information.object);;
									} else if(information.i==11) {	//该几号玩家画画了
										send(new Information(-1,11));
										Controler.controler.pguide.sethead(Controler.controler.pplayer.players.elementAt((int)information.object).head);
										if((int)information.object<0){	//游戏结束
											
										} else {
											
										}
									} else if(information.i==12) {	//pguide的信息传输
										send(new Information(-1,12));
										String str = (String)information.object;
										Controler.controler.pguide.guide.setText(str);
										if(str.equals("Dobe游戏开始")){
											Controler.controler.pplayer.setAllScore();
										} else if(str.equals("游戏结束")){
											Controler.controler.pplayer.setNoScore();
										}
									} else if(information.i>=100) {	//分数传输
										send(new Information(-1,100));
										Controler.controler.pplayer.players.elementAt(information.i-100).setScore((int)information.object);
									}
								}
							} catch (IOException | ClassNotFoundException e1) { 
								//e1.printStackTrace(); 
							}
						}
					}
				}
			).start();
	}
	public void send(Information information){
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.writeObject(information);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
