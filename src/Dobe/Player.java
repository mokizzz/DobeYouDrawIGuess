package Dobe;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.SoftBevelBorder;

public class Player extends JPanel implements Serializable {
    private static final long serialVersionUID = 1L;  
	String ID;
	int head,score;
	boolean daduile = false;
	Socket socket;
	JLabel idlabel;
	public Player(int head,String ID)	{
		//存储信息
		this.ID = ID;
		this.head = head;
		//panel外观设计
		this.setSize(190,80);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE);
		this.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		//添加组件
		try {
			this.add(new JLabel(new ImageIcon(ImageIO.read(
					new FileInputStream("images\\"+head+".jpg")).getScaledInstance(77, 77, Image.SCALE_SMOOTH))),BorderLayout.WEST);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.idlabel = new JLabel(ID);
		this.idlabel.setFont(new Font("幼圆",Font.PLAIN,16));
		this.add(idlabel);
	}
	public void setSocket(Socket socket){
		this.socket = socket;
	}
	public void setScore(int s){
		this.score = s;
		this.idlabel.setText("<html>"+ID+"<br>"+score+"分<html>");
	}
	public void noScore(){
		this.idlabel.setText(ID);
	}
}