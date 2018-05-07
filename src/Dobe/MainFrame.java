package Dobe;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener,WindowListener{
	PPlayer pplayer;
	PDraw pdraw;
	PDrawTools pdrawtools;
	PChat pchat;
	PGuide pguide;
	StartButton kaishiyouxi;
	GameThread gamethread;
	public MainFrame(Controler controler)
	{
		super("Dobeの你画我猜");
		this.setLayout(null);
		//----添加JPanel(各个组件)----
		this.pplayer = controler.pplayer;//添加玩家列表
		this.add(pplayer);
		this.pplayer.setLocation(25,20);
		
		this.pdraw = controler.pdraw;//添加画板
		this.add(pdraw);
		this.pdraw.setLocation(240,100);
		
		this.pdrawtools = controler.pdrawtools;//添加绘图工具
		this.add(pdrawtools);
		this.pdrawtools.setLocation(240,620);
		
		this.pchat = controler.pchat;//添加聊天框
		this.add(pchat);
		this.pchat.setLocation(1055,160);
		
		this.pguide = controler.pguide;//添加游戏导航
		this.add(pguide);
		this.pguide.setLocation(240,20);
		
		if(Controler.hostFlag){
			try {
				this.kaishiyouxi = new StartButton(new ImageIcon(
						ImageIO.read(new FileInputStream("images\\gameStart.png")).getScaledInstance(180, 64, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}//添加开始游戏按钮
			this.add(kaishiyouxi);
			this.kaishiyouxi.setLocation(1055,20);
			this.kaishiyouxi.setBounds(1055,80,200,80);
			this.kaishiyouxi.addActionListener(this);
			this.setVisible(true);
		} else {
			//在那个位置放一个LOGO图片
		}
		//----必要设置----
		this.setSize(1280,720);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setBackground(Color.black);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		//监听关闭窗口
		this.addWindowListener(this);
	}
	class StartButton extends JButton{
		public StartButton(ImageIcon s){
			super(s);
			try {
				this.setPressedIcon(new ImageIcon(
						ImageIO.read(new FileInputStream("images\\gameStart_press.png")).getScaledInstance(200, 80, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.setContentAreaFilled(false);
			this.setLayout(new FlowLayout());
			this.setVisible(true);
		}
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==kaishiyouxi){
			if(Controler.controler.pplayer.players.size()==1){
				JOptionPane.showMessageDialog(null, "房间里只有你一个人不能开始游戏。", "孤单", JOptionPane.WARNING_MESSAGE);
			} else {
				Controler.gamerunningFlag = true;
				kaishiyouxi.setEnabled(false);
				gamethread = new GameThread();
			}
		}
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		if(Controler.hostFlag && Controler.controler.pplayer.players.size()>1){
			Controler.controler.dobeserver.sendAll(new Information(12,"完蛋了！房主退出了游戏，带着他的小姨子跑了！我们散伙吧(＞﹏＜)！"));
			Controler.controler.dobeserver.sendAll(new Information(0,null));
		} else if(!Controler.hostFlag) {
			Controler.controler.dobeclient.send(new Information(0,null));
		}
		this.dispose();
	}
	@Override
	public void windowClosed(WindowEvent e) {
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		
	} 
}
