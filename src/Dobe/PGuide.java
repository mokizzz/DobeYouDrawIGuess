package Dobe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
//238，221，203
@SuppressWarnings("serial")
public class PGuide extends JPanel{
	JLabel head,guide,num,countdown;
	public PGuide(){
		//基本设置
		this.setBounds(240,20,800,80);
		this.setBackground(new Color(238,221,203));
		this.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		this.setLayout(new BorderLayout());
		//初始化组件
		try {
			head = new JLabel(new ImageIcon(ImageIO.read(new FileInputStream("images\\null.jpg")).getScaledInstance(78, 78, Image.SCALE_SMOOTH)));
			countdown = new JLabel(new ImageIcon(ImageIO.read(new FileInputStream("images\\countdown.png")).getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
			num = new JLabel("",JLabel.CENTER);
			num.setFont(new Font("黑体",Font.PLAIN,22));
			num.setForeground(Color.WHITE);
			//num.setOpaque(false);
			countdown.setLayout(new BorderLayout());
			countdown.add(num,BorderLayout.CENTER);
		} catch (IOException e) {
			e.printStackTrace();
		}
		guide = new JLabel("有1位玩家在房间中",JLabel.CENTER);
		guide.setFont(new Font("幼圆",Font.PLAIN,18));
		//添加组件
		this.add(head,BorderLayout.WEST);
		this.add(countdown, BorderLayout.EAST);
		this.add(guide, BorderLayout.CENTER);
		this.setVisible(true);
	}
	public void setnum(int n){
		this.num.setText(Integer.toString(n));
	}
	public void sethead(int head){
		
		try {
			if(head>=0){
				this.head.setIcon(new ImageIcon(ImageIO.read(new FileInputStream("images\\"+head+".jpg")).getScaledInstance(78, 78, Image.SCALE_SMOOTH)));
			} else {
				this.head.setIcon(new ImageIcon(ImageIO.read(new FileInputStream("images\\null.jpg")).getScaledInstance(78, 78, Image.SCALE_SMOOTH)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}