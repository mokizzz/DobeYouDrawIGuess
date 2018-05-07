package Dobe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

//���������ﶨ�� PORT=55555
public class DobeServer {
	ServerSocket serversocket;
	Vector<DobeCommunication> dobes = new Vector<DobeCommunication>();
	static final int PORT = 55555;//�˿�
	public DobeServer() throws IOException{
		//����������
		serversocket = new ServerSocket(PORT);
		System.out.println("�����������ɹ�");
		//������Ҽ�����Ϣ
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
	//��ÿ����ҵ��źŴ����߳���
	class DobeCommunication extends Thread {
		Socket socket;
		Player player = null;
		ObjectInputStream in = null;
		public DobeCommunication(Socket socket) throws IOException{
			this.socket = socket;
			this.start();
		}
		public void run(){
			//ʵʱ���䣬������ȡ��Ϣ
			while(true){
				try {
					in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					Object obj = in.readObject();
					if(obj!=null){
						Information information = (Information)obj;
						//����information
						if(information.i==0) {		//����뿪��Ϸ
							Controler.controler.pplayer.removePlayer(player);
							Controler.controler.pchat.send(new Information(3,"<ϵͳ>:���<"+player.ID+">������뿪����Ϸ��\n"));
							break;
						} else if(information.i==1) {	//����Ҽ���
							addNewPlayer(information);//д�������
							Controler.controler.pplayer.send();//����������ҵ��б�
							Controler.controler.pchat.send(new Information(3,"<ϵͳ>:���<"+player.ID+">�����˷��䡣\n"));
							
							String str = "��"+Controler.controler.pplayer.players.size()+"λ����ڷ�����";
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
						} else if(information.i==3) {	//�������˵���Ϣ����
							Controler.controler.pchat.send(information);
						} else if(information.i==4) {	//�ɿͻ��˴�������Ϣ
							Controler.controler.pdraw.points.addElement((OnePoint)information.object);
							Controler.controler.pdraw.repaint();
							Controler.mainframe.gamethread.sendAll(information);
						} else if(information.i==8) {	//���˲¶��˴�
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
				System.out.println("socket�ر�ʧ��");
			}
			System.out.println("�ͻ��˹ر���...");
		}
		//������Ҽ��뵽PPlayer�е�players�б���
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