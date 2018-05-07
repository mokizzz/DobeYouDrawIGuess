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
		System.out.println("�ͻ��˴����ɹ�");
		this.send(new Information(1,Controler.self));//���͸��������Լ�����Ϣ
		System.out.println("�ͻ��������������");
		new Thread(
				new Runnable(){
					//���մ������Է���������Ϣ
					public void run(){
						while(true){
							ObjectInputStream in;
							try {
								in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
								Object obj = in.readObject();
								if(obj!=null){
									Information information = (Information)obj;
									//�ж���Ϣ����
									if(information.i==0){		//�����뿪��Ϸ
										send(new Information(-1,0));
										Controler.controler.pchat.chatcase.append("<ϵͳ>:�군�ˣ������˳�����Ϸ����������С�������ˣ�����ɢ���(���n��)��\n");
										break;
									} else if(information.i==2) {	//��Ϸ�����Ϣ����
										send(new Information(-1,2));
										Vector<Player> pl = new Vector<Player>();
										int a;	String b;
										for(int j=0;j<(int)information.object;j++){
											a = in.readInt();
											b = (String)in.readUTF();
											pl.addElement(new Player(a,b));
										}
										Controler.controler.pplayer.setPlayers(pl);
									} else if(information.i==3) {	//��Ϸ��Ϣ����
										send(new Information(-1,3));
										Controler.controler.pchat.chatcase.append((String)information.object);
										Controler.controler.pchat.chatcase.setCaretPosition(Controler.controler.pchat.chatcase.getText().length());
									} else if(information.i==4) {	//���崫��
										send(new Information(-1,4));
										if(!Controler.myturn){
											Controler.controler.pdraw.points.addElement((OnePoint)information.object);
											Controler.controler.pdraw.repaint();
										}
									} else if(information.i==5) {	//�滭�ߵĴ�����Ϣ/��ʾ����
										send(new Information(-1,5));
										Controler.controler.pchat.chatcase.append("����:"+(String)information.object);
									} else if(information.i==6) {	//�ò����㻭����
										send(new Information(-1,6));
										Controler.myturn = (boolean)information.object;
										Controler.controler.pdraw.resetListener();
										Controler.controler.pdrawtools.setButtonEnabled((boolean)information.object);
									} else if(information.i==7) {	//������ȷ����
										send(new Information(-1,7));
										Controler.ciyu = (String)information.object;
									} else if(information.i==9) {	//pdraw clear
										send(new Information(-1,9));
										Controler.controler.pdraw.clear = true;
										Controler.controler.pdraw.points.removeAllElements();
										Controler.controler.pdraw.repaint();
									} else if(information.i==10) {	//����ʱ����
										send(new Information(-1,10));
										Controler.controler.pguide.setnum((int)information.object);;
									} else if(information.i==11) {	//�ü�����һ�����
										send(new Information(-1,11));
										Controler.controler.pguide.sethead(Controler.controler.pplayer.players.elementAt((int)information.object).head);
										if((int)information.object<0){	//��Ϸ����
											
										} else {
											
										}
									} else if(information.i==12) {	//pguide����Ϣ����
										send(new Information(-1,12));
										String str = (String)information.object;
										Controler.controler.pguide.guide.setText(str);
										if(str.equals("Dobe��Ϸ��ʼ")){
											Controler.controler.pplayer.setAllScore();
										} else if(str.equals("��Ϸ����")){
											Controler.controler.pplayer.setNoScore();
										}
									} else if(information.i>=100) {	//��������
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
