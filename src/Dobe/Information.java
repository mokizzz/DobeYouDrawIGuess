package Dobe;

import java.io.Serializable;

public class Information implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int i;
	Object object;
	/*
	i=
		-1:是否丢包的反馈
		0:断开连接				null
		1:新玩家加入 				Player	
		2:主机players对象传递			add	玩家信息
		3:chat消息传输			String	
		4:画板信息传输			OnePoint
		5:绘画者的词语				add	提示1	提示2
		6:该你了 my turn			boolean
		7:本轮回答的词语			String
		8:有人猜对了答案			null
		9:画板clear				null
		10:倒计时数字				int
		11:当前画画玩家号			int
		12:PGuide文字传输			String
		99:游戏开始和结束			boolean
		100+:游戏分数传输			i-100为玩家号		object为分数
	 */
	public Information(int i,Object object){
		this.i = i;
		this.object = object;
	}
}