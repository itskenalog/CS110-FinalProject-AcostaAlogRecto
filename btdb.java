import java.util.*;
import java.io.*;
import java.nio.file.*;

public class btdb{
	public static BTFile bt;
	public static ValuesFile values;
	public static BTree bee3;

	public static void main(String args[]){
		//initialize values and bt file based on input from command line
		try{
			bt = new BTFile(args[0]);
			values = new ValuesFile(args[1]);
		}
		catch(IOException ie){

		}
		//initialize B Tree
		bee3 = new BTree(bt, values);

		//accept input
		Scanner in = new Scanner(System.in);
		String inpu = in.nextLine();
		String[] input = inpu.split(" ");

		while(!input[0].equals("exit")){
			//this is used to convert the words that comes after the number to a string instead of it being a string array
			String value = "";
			if(input.length>=2){
				for(int i=2; i<input.length; i++){
					value = value+input[i];
					if(i!=value.length()-1){
						value+=" ";
					}
				}
			}

			//if input is insert,  call insert function
			if(input[0].equals("insert")){
				try{
					bee3.insert(Integer.parseInt(input[1]), value);
				}
				catch(IOException ie){

				}
			}
			//if input is update, call update function
			else if(input[0].equals("update")){
				bee3.updateFile(Integer.parseInt(input[1]), value);
			}
			//if input is select, call select function
			else if(input[0].equals("select")){
				bee3.select(Integer.parseInt(input[1]));
			}
			//does not accept any other command
			else{
				System.out.println("ERROR: invalid command.");
			}

			//get next input
			inpu = in.nextLine();
			input = inpu.split(" ");
		}

		try{
			bt.close();
			values.close();
		}
		catch(IOException ie){
			
		}
	}
}