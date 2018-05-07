package Dobe;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

public class PPlayer extends JPanel implements Serializable{
	private static final long serialVersionUID = 1L;
	Vector<Player> players = new Vector<Player>();
	public PPlayer(){
		setSize(200,640);
		setBackground(Color.WHITE);
		setLayout(new GridLayout(8,1));
		setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
	}
	public void addMe(Player p){
		Controler.self = p;
		this.players.addElement(p);
		this.add(p);
		this.setVisible(false);
		this.setVisible(true);
	}
	public void addPlayer(Player p){//���һ����Ϸ���
		this.players.addElement(p);
		this.add(p);
		this.setVisible(false);
		this.setVisible(true);
	}
	public void removePlayer(Player p){ //�Ƴ�һ����Ϸ���
		this.players.removeElement(p);
		this.remove(p);
		this.setVisible(false);
		this.setVisible(true);
	}
	public void redrawPlayer(){
		this.removeAll();
		System.out.println(players.size());
		for(int i=0;i<players.size();i++){
			this.add(players.elementAt(i));
			System.out.println(players.elementAt(i).ID);
		}
		this.setVisible(false);
		this.setVisible(true);
	}
	public void send(){		//ֻ����������ʹ�õ�send��������������б���Ϣ��������ң�
		ObjectOutputStream out;
		for(int i=1;i<players.size();i++){
			try {
				out = new ObjectOutputStream(players.elementAt(i).socket.getOutputStream());
				out.writeObject(new Information(2,players.size()));
				for(int j=0;j<players.size();j++){
					Player pd = players.elementAt(j);
					out.writeInt(pd.head);
					out.writeUTF(pd.ID);
				}
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void setPlayers(Vector<Player> players) {	//ֻ�пͻ��˿���ʹ�õ�set��������ȡ����������б���Ϣ��
		this.players = players;
		this.redrawPlayer();
	}
	public void doudaduileme(){	//�������ô
		boolean doudaduile = true;
		for(int i=0;i<players.size();i++){
			doudaduile = doudaduile && players.elementAt(i).daduile;
		}
		if(doudaduile){
			Controler.mainframe.gamethread.countdown = 0;
		}
	}
	public void doumeidadui(){	//������ҡ�����ˡ�
		for(int i=0;i<players.size();i++){
			players.elementAt(i).daduile = false;
		}
	}
	public void setAllScore(){	//��������ҷ�������
		for(int i=0;i<players.size();i++){
			players.elementAt(i).setScore(0);
		}
	}
	public void setNoScore(){
		for(int k=0;k<players.size();k++){
			players.elementAt(k).noScore();
		}
	}
}
