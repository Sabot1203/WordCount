import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) throws Exception
	{		
		analyseCommand(args);
	}
	
	public static void analyseCommand(String[] args) throws IOException
	{	
		List<List<String>> commandsList =new ArrayList<>();
		List<String> commands=new ArrayList<>();
		if(args.length==0)//需要用户输出参数
		{
			System.out.println("Command codes are needed !");
		}
		else
		{
			for(int i=0;i<args.length;i++)
			{
				commands.add(args[i]);
				if(!args[i].matches("^-.*"))//不是命令符号
				{
					if(args[i].contains("."))//是文件名或目录
					{
						if(!new File(args[i]).exists())//文件不存在
						{
							System.out.println("The file named "+args[i]+" does not exist");
							System.exit(0);
						}
						else
						{
							commandsList.add(commands);
							commands=new ArrayList<>();
						}
					}
					else//指令有错
					{
						System.out.println("The "+(i+1)+"th code("+args[i]+") must begin with '-'");
						System.exit(0);
					}
				}
			}
		}
		commandAction(commandsList);
	}
	
	public static void commandAction(List<List<String>> commandList) throws IOException
	{	
		String input=null,output=null,stop=null;
		
		for(List<String> commands:commandList)
		{
			if(commands.contains("-o"))
			{
				output=commands.get(commands.size()-1);
			}
			else if(commands.contains("-e"))
			{
				stop=commands.get(commands.size()-1);
			}
			else
			{
				input=commands.get(commands.size()-1);
			}
		}
		
		WordCount wc=new WordCount(input,stop,output);
		
		for(List<String> commands:commandList)
		{
			for(int i=0;i<commands.size()-1;i++)
		    {
				switch(commands.get(i))
			    {
				    case "-c":wc.CountChars();
			        break;
			        case "-w":wc.CountWords();
			        break;
			        case "-l":wc.CountLines();
			        break;
			        case "-s":wc.CountLinesByKind();
			        break;
			        case "-e":wc.CountWordsWithLimite();
			        break;
			        case "-o":break;
			        default:System.out.println("No such command code");
			    }
	        }
		}
	}
}

