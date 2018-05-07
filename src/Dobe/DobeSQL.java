package Dobe;
import java.lang.String;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class DobeSQL {
	static private String url="jdbc:mysql://localhost:3306/Dobe";  
	static private String user="root";  
	static private String password="123456";  
	public DobeSQL()
	{
		
	}
	static private String[] words=new String[100];
	static private String[] tips=new String[100];
	private static int ans;
	public static String[] getwords()throws Exception
	{
		String sql="select * from wordbd";
		Class.forName("com.mysql.jdbc.Driver");
		int i=0;
		Random generator=new Random();
		Connection con=DriverManager.getConnection(url,user,password);
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		while (rs.next())
		{
			words[i]=rs.getString("words");
			tips[i++]=rs.getString("tips");
		}
		ans=generator.nextInt(i);
		rs.close();
		stmt.close();
		con.close();
		return (new String[]{words[ans],tips[ans]});
	}
	public static void insertwords(String iword1,String iword2)throws Exception
	{
		String sql="insert into wordbd (words,tips) values(?,?)";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection(url,"root",password);
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,iword1);
		pstmt.setString(2,iword2);
		pstmt.executeUpdate();
		pstmt.close();
		con.close();
	}
	public static void delete(String dword) throws Exception
	{
		String sql="delete from wordbd where words='" + dword + "'";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection(url,"root",password);
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.executeUpdate();
		pstmt.close();
		con.close();
		
	}
	
	
}
