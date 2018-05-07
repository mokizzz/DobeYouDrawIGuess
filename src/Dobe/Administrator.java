package Dobe;

import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;

public class Administrator extends JFrame {
	public Administrator(){
		super("Administrator");
		this.setLayout(new FlowLayout());
		JPanel tianjia=new JPanel();
		tianjia.setSize(500,150);
		JPanel shanchu=new JPanel();
		shanchu.setSize(500,150);
		this.add(tianjia,BorderLayout.NORTH);
		this.add(shanchu,BorderLayout.SOUTH);
		
		JTextField f1=new JTextField(10);
		JTextField f2=new JTextField(10);
		JTextField f3=new JTextField(10);
		JButton b1,b2,b3;
		b1=new JButton("添加");
		b1.addActionListener(new ActionListener(){	
			public void actionPerformed (ActionEvent e)	
			{try {
				DobeSQL.insertwords(f1.getText(),f2.getText());
				f1.setText("");
				f2.setText("");
				}
			catch(Exception ed){};
			}
		});
		b2=new JButton("删除");
		b2.addActionListener(new ActionListener(){	
			public void actionPerformed (ActionEvent e)	
			{try {
				DobeSQL.delete(f3.getText());
				DobeSQL.getwords();
				f3.setText("");
				
				}
			catch(Exception ed){};
			}
		});
		b3=new JButton("开始游戏");
		b3.addActionListener(new ActionListener(){	
			public void actionPerformed (ActionEvent e)	
			{try {
				Controler.adm.dispose();
				Controler.controler = new Controler();
				//游戏从这里登录
				Controler.start = new Start();
				}
			catch(Exception ed){};
			}
		});
		this.setSize(500, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		f1.setFont(new Font("仿宋",Font.PLAIN,18));
		f1.setBackground(Color.RED);
		f2.setFont(new Font("仿宋",Font.PLAIN,18));
		f2.setBackground(Color.GREEN);
		f3.setFont(new Font("仿宋",Font.PLAIN,18));
		f3.setBackground(Color.YELLOW);
		tianjia.add(b1,FlowLayout.LEFT);
		tianjia.add(f1,FlowLayout.LEFT);
		tianjia.add(f2,FlowLayout.LEFT);
		shanchu.add(f3,FlowLayout.LEFT);
		shanchu.add(b2,FlowLayout.LEFT);
		shanchu.add(b3);
	}

}
