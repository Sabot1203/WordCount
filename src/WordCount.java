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
		if(output==null)//δָ������ļ� ��Ĭ��Ϊresult
		{
			this.output=result;
		}
		else
		{
			this.output=output;
		}
		this.stop=stop;
	}
	
	//�����ļ��е��ַ���
	public void  CountChars() throws IOException
	{
		BufferedInputStream input= new BufferedInputStream(
				new FileInputStream(this.input));
		BufferedOutputStream output=new BufferedOutputStream(
				new FileOutputStream(this.output,true));
		int count=0;
		int x=-1;
		char ch;
		while((x=input.read())!=-1) //����ȡ��ı�־�Ƿ���-1  ����ʱ�򷵻ص�Ϊ�ַ���asciiֵ
		{
			ch=(char)x;
			if(ch!='\r'&&ch!='\n')//����\n�ͻس�\r��ͳ��Ϊ�ַ�
			{
				count++;
			}
		}
		input.close();
		String str=this.input+",�ַ���:"+count;
		output.write(str.getBytes());
		output.write("\r\n".getBytes()); //�س� /r�ͻ���/nλ�ò��ɻ�
		output.flush();
		output.close();
	}
	
	//�����ļ��е�������
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
			if((char)x=='\n')//�������з���ʾһ�н���
			{
				count++;
			}
		}
		count++;
		input.close();
		String str=this.input+",����:"+(count);
		output.write(str.getBytes());
		output.write("\r\n".getBytes());
		output.flush();
		output.close();
	}
	
	//�����ļ��е��ܵ�����
	public void CountWords() throws IOException
	{
		BufferedInputStream input=new BufferedInputStream(
				new FileInputStream(this.input));
		BufferedOutputStream output=new BufferedOutputStream(
				new FileOutputStream(this.output,true));
		int count=0;
		int x=-1;
		boolean InWord=false;//�Ƿ��ȡ��ĳ��������
		char ch='0';
		while(true)
		{   
			x=input.read();
			ch=(char)x;
			if(Character.isLetter(ch)&&InWord==false)//��һ�ζ�ȡ����ĸ ���ȡ���뵽������
			{
				InWord=true;
			}
			else if(!Character.isLetter(ch)&&InWord==true)//��ȡ��������ʱ ��ȡ����һ������ĸ���ַ� �����һ������
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
		String str=this.input+",������:"+(count);
		output.write(str.getBytes());
		output.write("\r\n".getBytes());
		output.flush();
		output.close();
	}
	
	//���� ������/����/ע���� ������
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
		
		//bug:���ļ�ĩβ�ж���س� ��β���ɶ�  ������������1 δ���
		while((strLine=input.readLine())!=null)
		{
			all_lines++;
			strLine.replaceAll("\r", "");//ȥ�����з��Ϳո� ���ں������
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
			else if(strLine.startsWith("/*")||strLine.startsWith("{/*")) //����ע����
			{
				note_lines++;
				if(!strLine.endsWith("*/")&&!strLine.endsWith("*/}"))//����δע�ͽ���
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
		String str=this.input+",������/����/ע����:"+code_lines+"/"+blank_lines+"/"+note_lines;
		output.write(str.getBytes());
		output.write("\r\n".getBytes());
		output.flush();
		output.close();
	}
	
	//ʹ��ͣ�ô��ļ� ͳ�Ƶ��ʸ���
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
		
		//ȡ��ͣ�ô�װ��stopList
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
			if(Character.isLetter(ch)&&InWord==false)//��һ�ζ�ȡ����ĸ ���ȡ���뵽������
			{
				InWord=true;
				str+=ch;
			}
			else if(!Character.isLetter(ch)&&InWord==true)//��ȡ��������ʱ ��ȡ����һ������ĸ���ַ� �����һ������
			{
				if(!stopList.contains(str))//����ͣ�ô���
				{
					count++;
				}
				InWord=false;
				str="";
			}
			else if(InWord==true)//��ȡ��ĸ�ڵ�����
			{
				str+=ch;
			}
			if(x==-1)
			{
				break;
			}
		}
		input.close();
		String str0=this.input+",������:"+(count);
		output.write(str0.getBytes());
		output.write("\r\n".getBytes());
		output.flush();
		output.close();
	}
}