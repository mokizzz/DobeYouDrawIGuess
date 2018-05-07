package Dobe;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.SoftBevelBorder;

@SuppressWarnings("serial")
public class Start extends JFrame {
	//下面这些对象总体调用
	HeadSelection headselection = new HeadSelection();
	PlayerDetailsandGameCreateandGameJoin playerdetailsandgamecreateandgamejoin = new PlayerDetailsandGameCreateandGameJoin(this);
	public Start() {
		super("登录 Dobeの你画我猜");
		this.setLayout(new GridLayout(1,2));
		this.setSize(500, 300);
		
		this.add(headselection);
		this.add(playerdetailsandgamecreateandgamejoin);
		
		//this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public void Close(){
		this.dispose();
	}
	class HeadSelection extends JPanel implements ActionListener{
		ImageButton heads[] = new ImageButton[16];
		int headInt = 0;
		public HeadSelection(){
			this.setSize(560,560);
			this.setLayout(new GridLayout(4,4));
			this.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
			for(int i=0;i<16;i++){
				try {
					heads[i] = new ImageButton(new ImageIcon(ImageIO.read(new FileInputStream("images\\"+i+".jpg")).getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				heads[i].addActionListener(this);
				this.add(heads[i]);
			}
			this.setVisible(true);
		}
		public void actionPerformed(ActionEvent e){
			for(int i=0;i<16;i++){
				if(e.getSource()==heads[i]){
						try {
							Controler.start.playerdetailsandgamecreateandgamejoin.playerdetails.head.setIcon(new ImageIcon(
									ImageIO.read(new FileInputStream("images\\"+i+".jpg")).getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						headInt = i;
						break;
				}
			}
		}
	}
	class PlayerDetailsandGameCreateandGameJoin extends JPanel {
		Start start;
		PlayerDetails playerdetails = new PlayerDetails();
		GameCreate gamecreater = new GameCreate();
		GameJoin gamejoin = new GameJoin();
		public PlayerDetailsandGameCreateandGameJoin(Start start){
			super(new GridLayout(3,1));
			this.setSize(230,560);
			this.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
			this.setVisible(true);
			this.add(playerdetails);
			this.add(gamecreater);
			this.add(gamejoin);
			this.start = start;
		}
		class PlayerDetails extends JPanel {
			JPanel panel = new JPanel(new GridLayout(2,1));
			JLabel head = new JLabel();
			JLabel label = new JLabel("游戏ID：",JLabel.LEFT);
			JTextField name = new JTextField("大板R");
			public PlayerDetails(){
				try {
					head.setIcon(new ImageIcon(ImageIO.read(new FileInputStream("images\\0.jpg")).getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				panel.add(label);
				panel.add(name);
				//this.setSize(230,180);
				this.setLayout(new GridLayout(1,2));
				this.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
				this.add(head);
				this.add(panel);
				this.setVisible(true);
			}
		}
		class GameCreate extends JPanel implements ActionListener {
			JButton createbutton = new JButton("我要开房");
			JPanel panel = new JPanel(new GridLayout(2,1));
			JLabel label = new JLabel("本机IP",JLabel.LEFT);
			JTextField tf = new JTextField();
			public GameCreate(){
				tf.setEditable(false);
				panel.add(label);
				panel.add(tf);
				try {
					InetAddress ia = InetAddress.getLocalHost();
					tf.setText(ia.getHostAddress());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				finally{
					//this.setSize(230,180);
					this.setLayout(new BorderLayout());
					this.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
					this.add(panel);
					this.add(createbutton, BorderLayout.EAST);
					this.setVisible(true);
					createbutton.addActionListener(this);
				}
			}
			//当点击“我要开房”
			public void actionPerformed(ActionEvent e){
				//这里的player对象的头像未定义！！！！！！！！！！！！！！！！！！！
				Player p = new Player(headselection.headInt,playerdetails.name.getText());
				Controler.controler.pplayer.addMe(p);
				try {
					Controler.controler.HouseOpen();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				start.Close();
			}
		}
		class GameJoin extends JPanel implements ActionListener {
			JButton joinbutton = new JButton("加入房间");
			JTextField text = new JTextField(gamecreater.tf.getText());
			JPanel panel = new JPanel(new GridLayout(2,1));
			JLabel label = new JLabel("主机IP",JLabel.LEFT);
			public GameJoin(){
				panel.add(label);
				panel.add(text);
				//this.setSize(230,180);
				this.setLayout(new BorderLayout());
				this.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
				this.add(panel);
				this.add(joinbutton, BorderLayout.EAST);
				this.setVisible(true);
				joinbutton.addActionListener(this);
			}
			//当点击“加入房间”
			public void actionPerformed(ActionEvent e){
				//这里的player对象的头像未定义！！！！！！！！！！！！！！！！！！！
				Player p = new Player(headselection.headInt,playerdetails.name.getText());
				Controler.controler.pplayer.addMe(p);
				try {
					Controler.controler.HouseJoin(gamejoin.text.getText(),p);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				start.Close();
			}
		}
	}
}
class ImageButton extends JButton{
	private static final long serialVersionUID = 1L;
	public ImageButton(ImageIcon s){
		super(s);
		setContentAreaFilled(false);
		setLayout(new FlowLayout());
		setVisible(true);
	}
}