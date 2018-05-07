package Dobe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

//������д�����촰��
@SuppressWarnings("serial")
public class PChat extends JPanel
{
	JTextArea chatcase;
	private JTextField chatin;
	private JButton chatsend;
	public PChat()
	{
		setSize(200,500);
		setLayout(new BorderLayout());
		//----new˽�б���----
		chatcase = new JTextArea("��ӭ����Dobe���㻭�Ҳ£�\n",180,200);
		chatin = new JTextField(11);
		chatsend = new JButton("����");
		//----������----
		chatin.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e)	//�س������� / ����޸�ʱ�����Ǹ�Ҳ�޸�
			{
				if(e.getKeyCode()==KeyEvent.VK_ENTER && chatin.getText().length()!=0)
				{	
					if(Controler.hostFlag){	//���������������
						if(chatin.getText().equals(Controler.ciyu))	{	//�����¶��˴�
							if(Controler.myturn){
								send(new Information(3,"<"+Controler.self.ID+">��ͼ͸©�𰸡�\n"));
							} else {
								send(new Information(3,"<"+Controler.self.ID+">�¶��˴𰸡�\n"));
								if(Controler.mainframe.gamethread.countdown>30)
									Controler.mainframe.gamethread.countdown = 30;
								Controler.self.daduile = true;
								Controler.self.setScore(Controler.self.score+Controler.basicscore);
								if(Controler.basicscore>1)	Controler.basicscore--;
								Controler.controler.dobeserver.sendAll(new Information(100,Controler.self.score));
								Controler.controler.pplayer.doudaduileme();
							}
						} else {	//ֻ�Ƿ���Ϣ
							send(new Information(3,"<"+Controler.self.ID+">:"+chatin.getText()+"\n"));
						}						
					} else {	//����
						if(chatin.getText().equals(Controler.ciyu))	{	//��Ҳ¶��˴�
							if(Controler.myturn){
								Controler.controler.dobeclient.send(new Information(3,"<"+Controler.self.ID+">��ͼ͸©�𰸡�\n"));
							} else {
								Controler.controler.dobeclient.send(new Information(3,"<"+Controler.self.ID+">�¶��˴𰸡�\n"));
								Controler.controler.dobeclient.send(new Information(8,null));
							}
						} else {	//ֻ�Ƿ���Ϣ
							Controler.controler.dobeclient.send(new Information(3,"<"+Controler.self.ID+">:"+chatin.getText()+"\n"));
						}
					}
					chatin.setText("");
					chatcase.setCaretPosition(chatcase.getText().length());
				}
			}
		});
		chatsend.addActionListener(new ActionListener(){	//�������Ǹ���ͬ!!!!!!
			public void actionPerformed(ActionEvent e)	//���Ͱ�ť������
			{
				if(e.getSource()==chatsend && chatin.getText().length()!=0)
				{	
					if(Controler.hostFlag){	//���������������
						if(chatin.getText().equals(Controler.ciyu))	{	//�����¶��˴�
							if(Controler.myturn){
								send(new Information(3,"<"+Controler.self.ID+">��ͼ͸©�𰸡�\n"));
							} else {
								send(new Information(3,"<"+Controler.self.ID+">�¶��˴𰸡�\n"));
								if(Controler.mainframe.gamethread.countdown>30)
									Controler.mainframe.gamethread.countdown = 30;
								Controler.self.daduile = true;
								Controler.self.setScore(Controler.self.score+Controler.basicscore);
								if(Controler.basicscore>1)	Controler.basicscore--;
								Controler.controler.dobeserver.sendAll(new Information(100,Controler.self.score));
								Controler.controler.pplayer.doudaduileme();
							}
						} else {	//ֻ�Ƿ���Ϣ
							send(new Information(3,"<"+Controler.self.ID+">:"+chatin.getText()+"\n"));
						}						
					} else {	//����
						if(chatin.getText().equals(Controler.ciyu))	{	//��Ҳ¶��˴�
							if(Controler.myturn){
								Controler.controler.dobeclient.send(new Information(3,"<"+Controler.self.ID+">��ͼ͸©�𰸡�\n"));
							} else {
								Controler.controler.dobeclient.send(new Information(3,"<"+Controler.self.ID+">�¶��˴𰸡�\n"));
								Controler.controler.dobeclient.send(new Information(8,null));
							}
						} else {	//ֻ�Ƿ���Ϣ
							Controler.controler.dobeclient.send(new Information(3,"<"+Controler.self.ID+">:"+chatin.getText()+"\n"));
						}
					}
					chatin.setText("");
					chatcase.setCaretPosition(chatcase.getText().length());
				}
			}
		});
		//----���������----
		chatcase.setEditable(false);
		chatcase.setSelectedTextColor(Color.WHITE);
		chatcase.setLineWrap(true);
		chatcase.setWrapStyleWord(true);
		chatcase.setFont(new Font("����",Font.PLAIN,16));
		chatsend.setFont(new Font("����",Font.PLAIN,18));
		chatin.setFont(new Font("����",Font.PLAIN,18));
		//----���ò���----
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.add(chatin);
		panel1.add(chatsend,BorderLayout.EAST);
		//----������----
		add(panel1,BorderLayout.SOUTH);
		add(new JScrollPane(chatcase),BorderLayout.CENTER);
		//----��Ҫ����----
		setVisible(true);
	}
	//ֻ�з���������ʹ�õ�send����������������Ϣ��
	public void send(Information information){
		chatcase.append((String)information.object);
		chatcase.setCaretPosition(chatcase.getText().length());
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
