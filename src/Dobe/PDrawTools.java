package Dobe;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

@SuppressWarnings("serial")
public class PDrawTools extends JPanel implements ActionListener{
	public JButton pclear;
	public JButton pbrush[] = new PDButton[4];
	public JButton c[] = new PDButton[9];
	private int size = 1;
	class PDButton extends JButton
	{
		public PDButton(ImageIcon s)
		{
			super(s);
			setContentAreaFilled(false);
			setLayout(new FlowLayout());
			setVisible(true);
		}
	}
	public PDrawTools(){
		setBounds(240,620,800,40);
		setBackground(Color.WHITE);
		setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		setVisible(true);
		setLayout(new GridLayout(1,15));
		//添加按钮
		pclear = new PDButton(new ImageIcon("images\\clear.png"));
		pclear.setPressedIcon(new ImageIcon("images\\clear_press.png"));
		for(int i=0;i<4;i++)
		{
			try {
				pbrush[i]=new PDButton(new ImageIcon(ImageIO.read(
						new FileInputStream("images\\pbrush" + (i+1) + ".png")).getScaledInstance(36, 36, Image.SCALE_SMOOTH)));
				pbrush[i].setPressedIcon(new ImageIcon(ImageIO.read(
						new FileInputStream("images\\pbrush" + (i+1) + "_press.png")).getScaledInstance(36, 36, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			add(pbrush[i]);
			pbrush[i].addActionListener(this);
		}
		for(int i=0;i<9;i++){
			c[i] = new PDButton(new ImageIcon("images\\c" + i + ".png"));
			c[i].setPressedIcon(new ImageIcon("images\\c" + i + "_press.png"));
			add(c[i]);
			c[i].addActionListener(this);
		}
		add(pclear);
		pclear.addActionListener(this);
		this.setButtonEnabled(false);
	}
	public void setButtonEnabled(boolean bool){	//设置按钮是否可用
		pclear.setEnabled(bool);
		for(int i=0;i<4;i++)	pbrush[i].setEnabled(bool);
		for(int i=0;i<9;i++)	c[i].setEnabled(bool);
	}
	public void actionPerformed(ActionEvent e){//当画板按钮被点击
		if(e.getSource()==c[0]){
			Controler.controler.pdraw.currentColor = new Color(255,255,255);
			Controler.controler.pdraw.currentSize = 20;
		}
		else if(e.getSource()==c[1])
			Controler.controler.pdraw.currentColor = new Color(255,0,0);
		else if(e.getSource()==c[2])
			Controler.controler.pdraw.currentColor = new Color(255,165,0);
		else if(e.getSource()==c[3])
			Controler.controler.pdraw.currentColor = new Color(255,255,0);
		else if(e.getSource()==c[4])
			Controler.controler.pdraw.currentColor = new Color(0,255,0);
		else if(e.getSource()==c[5])
			Controler.controler.pdraw.currentColor = new Color(0,255,255);
		else if(e.getSource()==c[6])
			Controler.controler.pdraw.currentColor = new Color(0,0,255);
		else if(e.getSource()==c[7])
			Controler.controler.pdraw.currentColor = new Color(255,0,255);
		else if(e.getSource()==c[8])
			Controler.controler.pdraw.currentColor = new Color(0,0,0);
		else if(e.getSource()==pbrush[0])
			size = 1;
		else if(e.getSource()==pbrush[1])
			size = 2;
		else if(e.getSource()==pbrush[2])
			size = 3;
		else if(e.getSource()==pbrush[3])
			size = 4;
		else if(e.getSource()==pclear){
			Controler.controler.pdraw.clear = true;
			Controler.controler.pdraw.points.removeAllElements();
			Controler.controler.pdraw.repaint();
			if(Controler.hostFlag){
				Controler.mainframe.gamethread.sendAll(new Information(9,null));
			} else {
				Controler.controler.dobeclient.send(new Information(9,null));
			}
		}
		if(e.getSource()!=c[0])
			Controler.controler.pdraw.currentSize = size;
	}
}
