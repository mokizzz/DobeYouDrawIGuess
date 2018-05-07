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
		super("Dobe���㻭�Ҳ�");
		this.setLayout(null);
		//----���JPanel(�������)----
		this.pplayer = controler.pplayer;//�������б�
		this.add(pplayer);
		this.pplayer.setLocation(25,20);
		
		this.pdraw = controler.pdraw;//��ӻ���
		this.add(pdraw);
		this.pdraw.setLocation(240,100);
		
		this.pdrawtools = controler.pdrawtools;//��ӻ�ͼ����
		this.add(pdrawtools);
		this.pdrawtools.setLocation(240,620);
		
		this.pchat = controler.pchat;//��������
		this.add(pchat);
		this.pchat.setLocation(1055,160);
		
		this.pguide = controler.pguide;//�����Ϸ����
		this.add(pguide);
		this.pguide.setLocation(240,20);
		
		if(Controler.hostFlag){
			try {
				this.kaishiyouxi = new StartButton(new ImageIcon(
						ImageIO.read(new FileInputStream("images\\gameStart.png")).getScaledInstance(180, 64, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}//��ӿ�ʼ��Ϸ��ť
			this.add(kaishiyouxi);
			this.kaishiyouxi.setLocation(1055,20);
			this.kaishiyouxi.setBounds(1055,80,200,80);
			this.kaishiyouxi.addActionListener(this);
			this.setVisible(true);
		} else {
			//���Ǹ�λ�÷�һ��LOGOͼƬ
		}
		//----��Ҫ����----
		this.setSize(1280,720);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setBackground(Color.black);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		//�����رմ���
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
				JOptionPane.showMessageDialog(null, "������ֻ����һ���˲��ܿ�ʼ��Ϸ��", "�µ�", JOptionPane.WARNING_MESSAGE);
			} else {
				Controler.gamerunningFlag = true;
				kaishiyouxi.setEnabled(false);
				gamethread = new GameThread();
			}
		}
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO �Զ����ɵķ������
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		if(Controler.hostFlag && Controler.controler.pplayer.players.size()>1){
			Controler.controler.dobeserver.sendAll(new Information(12,"�군�ˣ������˳�����Ϸ����������С�������ˣ�����ɢ���(���n��)��"));
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
		// TODO �Զ����ɵķ������
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO �Զ����ɵķ������
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO �Զ����ɵķ������
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		
	} 
}
