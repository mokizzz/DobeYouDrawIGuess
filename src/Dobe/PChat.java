package Dobe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

//这个类编写了聊天窗格
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
		//----new私有变量----
		chatcase = new JTextArea("欢迎来到Dobe的你画我猜！\n",180,200);
		chatin = new JTextField(11);
		chatsend = new JButton("发送");
		//----监听器----
		chatin.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e)	//回车监听器 / 这个修改时下面那个也修改
			{
				if(e.getKeyCode()==KeyEvent.VK_ENTER && chatin.getText().length()!=0)
				{	
					if(Controler.hostFlag){	//如果发送者是主机
						if(chatin.getText().equals(Controler.ciyu))	{	//主机猜对了答案
							if(Controler.myturn){
								send(new Information(3,"<"+Controler.self.ID+">试图透漏答案。\n"));
							} else {
								send(new Information(3,"<"+Controler.self.ID+">猜对了答案。\n"));
								if(Controler.mainframe.gamethread.countdown>30)
									Controler.mainframe.gamethread.countdown = 30;
								Controler.self.daduile = true;
								Controler.self.setScore(Controler.self.score+Controler.basicscore);
								if(Controler.basicscore>1)	Controler.basicscore--;
								Controler.controler.dobeserver.sendAll(new Information(100,Controler.self.score));
								Controler.controler.pplayer.doudaduileme();
							}
						} else {	//只是发消息
							send(new Information(3,"<"+Controler.self.ID+">:"+chatin.getText()+"\n"));
						}						
					} else {	//否则
						if(chatin.getText().equals(Controler.ciyu))	{	//玩家猜对了答案
							if(Controler.myturn){
								Controler.controler.dobeclient.send(new Information(3,"<"+Controler.self.ID+">试图透漏答案。\n"));
							} else {
								Controler.controler.dobeclient.send(new Information(3,"<"+Controler.self.ID+">猜对了答案。\n"));
								Controler.controler.dobeclient.send(new Information(8,null));
							}
						} else {	//只是发消息
							Controler.controler.dobeclient.send(new Information(3,"<"+Controler.self.ID+">:"+chatin.getText()+"\n"));
						}
					}
					chatin.setText("");
					chatcase.setCaretPosition(chatcase.getText().length());
				}
			}
		});
		chatsend.addActionListener(new ActionListener(){	//与上面那个相同!!!!!!
			public void actionPerformed(ActionEvent e)	//发送按钮监听器
			{
				if(e.getSource()==chatsend && chatin.getText().length()!=0)
				{	
					if(Controler.hostFlag){	//如果发送者是主机
						if(chatin.getText().equals(Controler.ciyu))	{	//主机猜对了答案
							if(Controler.myturn){
								send(new Information(3,"<"+Controler.self.ID+">试图透漏答案。\n"));
							} else {
								send(new Information(3,"<"+Controler.self.ID+">猜对了答案。\n"));
								if(Controler.mainframe.gamethread.countdown>30)
									Controler.mainframe.gamethread.countdown = 30;
								Controler.self.daduile = true;
								Controler.self.setScore(Controler.self.score+Controler.basicscore);
								if(Controler.basicscore>1)	Controler.basicscore--;
								Controler.controler.dobeserver.sendAll(new Information(100,Controler.self.score));
								Controler.controler.pplayer.doudaduileme();
							}
						} else {	//只是发消息
							send(new Information(3,"<"+Controler.self.ID+">:"+chatin.getText()+"\n"));
						}						
					} else {	//否则
						if(chatin.getText().equals(Controler.ciyu))	{	//玩家猜对了答案
							if(Controler.myturn){
								Controler.controler.dobeclient.send(new Information(3,"<"+Controler.self.ID+">试图透漏答案。\n"));
							} else {
								Controler.controler.dobeclient.send(new Information(3,"<"+Controler.self.ID+">猜对了答案。\n"));
								Controler.controler.dobeclient.send(new Information(8,null));
							}
						} else {	//只是发消息
							Controler.controler.dobeclient.send(new Information(3,"<"+Controler.self.ID+">:"+chatin.getText()+"\n"));
						}
					}
					chatin.setText("");
					chatcase.setCaretPosition(chatcase.getText().length());
				}
			}
		});
		//----聊天框设置----
		chatcase.setEditable(false);
		chatcase.setSelectedTextColor(Color.WHITE);
		chatcase.setLineWrap(true);
		chatcase.setWrapStyleWord(true);
		chatcase.setFont(new Font("仿宋",Font.PLAIN,16));
		chatsend.setFont(new Font("仿宋",Font.PLAIN,18));
		chatin.setFont(new Font("仿宋",Font.PLAIN,18));
		//----设置布局----
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.add(chatin);
		panel1.add(chatsend,BorderLayout.EAST);
		//----添加组件----
		add(panel1,BorderLayout.SOUTH);
		add(new JScrollPane(chatcase),BorderLayout.CENTER);
		//----必要设置----
		setVisible(true);
	}
	//只有服务器可以使用的send方法（发送聊天信息）
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
