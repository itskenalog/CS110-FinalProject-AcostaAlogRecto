import java.util.*;
import java.io.*;
import java.nio.file.*;

public class btdb{
	private String[] input;

	public static void main(String args[]){
		try{
			BTFile bt = new BTFile(args[0]);
			ValuesFile values = new ValuesFile(args[1]);
		}
		catch(IOException ie){

		}

		//accept input
		Scanner in = new Scanner(System.in);
		String inpu = in.nextLine();
		input = inpu.split(" ");

		if(input[0].equals("insert")){
			//insert();
		}
		else if(input[0].equals("update")){
			//update();
		}
		else if(input[0].equals("select")){
			//select();
		}
		else{
			//ERROR
		}
	}

	public static Node search(long key){
		//first you read from the file and get root node
		//make a node based on what you read
		//keep checking the keys until its less then
		//if less than, check if its the child
		//if there is go to the child then search()
		//else return node
		return null;
	}

	public static void split(){

	}

	public static void insert(long key, String value){

	}

	public static void update(long key, String value){

	}

	public static void select(long key){

	}
}