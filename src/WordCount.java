import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordCount {
	private String input;
	
	private String output;
	
	private String stop;
	
	public static String result="bin/result.txt";
	
	public WordCount(String input,String stop,String output) throws FileNotFoundException
	{
		this.input=input;
		if(output==null)//未指定输出文件 则默认为result
		{
			this.output=result;
		}
		else
		{
			this.output=output;
		}
		this.stop=stop;
	}
	
	//返回文件中的字符数
	public void  CountChars() throws IOException
	{
		BufferedInputStream input= new BufferedInputStream(
				new FileInputStream(this.input));
		BufferedOutputStream output=new BufferedOutputStream(
				new FileOutputStream(this.output,true));
		int count=0;
		int x=-1;
		char ch;
		while((x=input.read())!=-1) //流读取完的标志是返回-1  其他时候返回的为字符的ascii值
		{
			ch=(char)x;
			if(ch!='\r'&&ch!='\n')//换行\n和回车\r不统计为字符
			{
				count++;
			}
		}
		input.close();
		String str=this.input+",字符数:"+count;
		output.write(str.getBytes());
		output.write("\r\n".getBytes()); //回车 /r和换行/n位置不可换
		output.flush();
		output.close();
	}
	
	//返回文件中的总行数
	public void CountLines() throws IOException
	{
		BufferedInputStream input=new BufferedInputStream(
				new FileInputStream(this.input));
		BufferedOutputStream output=new BufferedOutputStream(
				new FileOutputStream(this.output,true));
		int count=0;
		int x=-1;
		while((x=input.read())!=-1)
		{
			if((char)x=='\n')//遇到换行符表示一行结束
			{
				count++;
			}
		}
		count++;
		input.close();
		String str=this.input+",行数:"+(count);
		output.write(str.getBytes());
		output.write("\r\n".getBytes());
		output.flush();
		output.close();
	}
	
	//返回文件中的总单词数
	public void CountWords() throws IOException
	{
		BufferedInputStream input=new BufferedInputStream(
				new FileInputStream(this.input));
		BufferedOutputStream output=new BufferedOutputStream(
				new FileOutputStream(this.output,true));
		int count=0;
		int x=-1;
		boolean InWord=false;//是否读取到某个单词中
		char ch='0';
		while(true)
		{   
			x=input.read();
			ch=(char)x;
			if(Character.isLetter(ch)&&InWord==false)//第一次读取到字母 则读取进入到单词中
			{
				InWord=true;
			}
			else if(!Character.isLetter(ch)&&InWord==true)//读取到单词中时 读取到第一个非字母的字符 则读完一个单词
			{
				count++;
				InWord=false;
			}
			if(x==-1)
			{
				break;
			}
		}
		input.close();
		String str=this.input+",单词数:"+(count);
		output.write(str.getBytes());
		output.write("\r\n".getBytes());
		output.flush();
		output.close();
	}
	
	//返回 代码行/空行/注释行 的行数
	public void CountLinesByKind()throws IOException
	{
		BufferedReader input=new BufferedReader(
				new FileReader(
						new File(this.input)));
		BufferedOutputStream output=new BufferedOutputStream(
				new FileOutputStream(this.output,true));
		int all_lines=0;
		int blank_lines=0;
		int note_lines=0;
		int code_lines=0;
		String strLine=null;
		boolean InNoteLines=false;
		
		//bug:若文件末尾有多个回车 则尾不可读  导致总行数少1 未解决
		while((strLine=input.readLine())!=null)
		{
			all_lines++;
			strLine.replaceAll("\r", "");//去除换行符和空格 便于后面操作
			strLine.replaceAll("\n", "");
			strLine=strLine.trim();
			strLine.replaceAll(" ", "");
			if(InNoteLines==true)
			{
				note_lines++;
				if(strLine.endsWith("*/")||strLine.endsWith("*/}"))
				{
					InNoteLines=false;
				}
			}
			else if(strLine.startsWith("/*")||strLine.startsWith("{/*")) //进入注释行
			{
				note_lines++;
				if(!strLine.endsWith("*/")&&!strLine.endsWith("*/}"))//本行未注释结束
				{
					InNoteLines=true;
				}
			}
			else if(strLine.startsWith("//")||strLine.startsWith("{//"))
			{
				note_lines++;
			}
			else if(strLine.equals("")||strLine.equals("{")||strLine.equals("}"))
			{
				blank_lines++;
			}
		}
		code_lines=all_lines-blank_lines-note_lines;
		String str=this.input+",代码行/空行/注释行:"+code_lines+"/"+blank_lines+"/"+note_lines;
		output.write(str.getBytes());
		output.write("\r\n".getBytes());
		output.flush();
		output.close();
	}
	
	//使用停用词文件 统计单词个数
	public void CountWordsWithLimite() throws IOException
	{
		BufferedInputStream input=new BufferedInputStream(
				new FileInputStream(this.input));
		BufferedInputStream stop=new BufferedInputStream(
				new FileInputStream(this.stop));
		BufferedOutputStream output=new BufferedOutputStream(
				new FileOutputStream(this.output,true));
		int count=0;
		int x=-1;
		char ch;
		String stopStr="";
		String str="";
		List<String> stopList =new ArrayList<>();
		boolean InWord=false;
		
		//取出停用词装到stopList
		while(true)
		{
			x=stop.read();
			if(x==-1)
			{
				stopList.add(stopStr);
				stopStr="";
				stop.close();
				break;
			}
			ch=(char)x;
			if(ch==' ')
			{
				stopList.add(stopStr);
				stopStr="";
			}
			else
			{
				stopStr+=ch;
			}
		}
		
		while(true)
		{   
			x=input.read();
			ch=(char)x;
			if(Character.isLetter(ch)&&InWord==false)//第一次读取到字母 则读取进入到单词中
			{
				InWord=true;
				str+=ch;
			}
			else if(!Character.isLetter(ch)&&InWord==true)//读取到单词中时 读取到第一个非字母的字符 则读完一个单词
			{
				if(!stopList.contains(str))//不在停用词中
				{
					count++;
				}
				InWord=false;
				str="";
			}
			else if(InWord==true)//读取字母在单词中
			{
				str+=ch;
			}
			if(x==-1)
			{
				break;
			}
		}
		input.close();
		String str0=this.input+",单词数:"+(count);
		output.write(str0.getBytes());
		output.write("\r\n".getBytes());
		output.flush();
		output.close();
	}
}