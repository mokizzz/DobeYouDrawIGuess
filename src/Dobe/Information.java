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
		-1:�Ƿ񶪰��ķ���
		0:�Ͽ�����				null
		1:����Ҽ��� 				Player	
		2:����players���󴫵�			add	�����Ϣ
		3:chat��Ϣ����			String	
		4:������Ϣ����			OnePoint
		5:�滭�ߵĴ���				add	��ʾ1	��ʾ2
		6:������ my turn			boolean
		7:���ֻش�Ĵ���			String
		8:���˲¶��˴�			null
		9:����clear				null
		10:����ʱ����				int
		11:��ǰ������Һ�			int
		12:PGuide���ִ���			String
		99:��Ϸ��ʼ�ͽ���			boolean
		100+:��Ϸ��������			i-100Ϊ��Һ�		objectΪ����
	 */
	public Information(int i,Object object){
		this.i = i;
		this.object = object;
	}
}