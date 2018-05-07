package Dobe;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.Vector;
@SuppressWarnings("serial")
class OnePoint implements Serializable
{
	int x,y;
	Color cc;
	int size;
	boolean cut;
	public OnePoint(int x,int y,Color cc,int size,boolean cut)
	{
		this.x = x;
		this.y = y;
		this.cc = cc;
		this.size = size;
		this.cut = cut;
	}
}
@SuppressWarnings("serial")
public class PDraw extends JPanel implements MouseMotionListener,MouseListener
{
	Color currentColor = Color.BLACK;//画笔颜色
	int currentSize = 1;//画笔大小
	private Point p = new Point();
	Vector<OnePoint> points = new Vector<OnePoint>();
	boolean clear = false;
	
	public PDraw ()
	{
		this.setBounds(240,100,800,520);
		this.setBackground(Color.WHITE);
		this.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		this.setVisible(true);
		this.setLayout(null);
		this.resetListener();
	}
	public void resetListener(){
		//这个方法用来重置画布监听器是否打开
		if(Controler.myturn){
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		} else {
			this.removeMouseListener(this);
			this.removeMouseMotionListener(this);
		}
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		if(clear)
		{
			g.clearRect(0, 0, getSize().width, getSize().height);
			clear = !clear;
		}
		for(int i=0;i<points.size()-1;i++)
		{
			g2d.setColor(points.elementAt(i).cc);
			g2d.setStroke(new BasicStroke(points.elementAt(i).size));
			if(points.elementAt(i).cut == points.elementAt(i+1).cut)
			{
				g2d.draw(new Line2D.Double(points.elementAt(i).x, points.elementAt(i).y, points.elementAt(i+1).x, points.elementAt(i+1).y));
			}
		}
	}
	public void mouseDragged(MouseEvent e)
	{
		p = e.getPoint();
		OnePoint op = new OnePoint(p.x,p.y,currentColor,currentSize,true);
		points.addElement(op);
		repaint();
		if(Controler.hostFlag){
			Controler.mainframe.gamethread.sendAll(new Information(4,op));
		} else {
			Controler.controler.dobeclient.send(new Information(4,op));
		}
	}
	public void mouseMoved(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) 
	{
		OnePoint op = new OnePoint(-1,-1,currentColor,currentSize,false);
		points.addElement(op);
		if(Controler.hostFlag){
			Controler.mainframe.gamethread.sendAll(new Information(4,op));
		} else {
			Controler.controler.dobeclient.send(new Information(4,op));
		}
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
