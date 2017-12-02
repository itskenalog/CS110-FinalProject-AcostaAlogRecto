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

	public static Node search(long key, long node){
		//first you read from the file and get root node
		//make a node based on what you read
		Node n = bt.read(node);
		//keep checking the keys until its less then
		for(int i=0; i<bt.accessOrder()-1; i++){
			if(key<n.accessKey(i)){
				//if less than, check if there's a child
				if(n.accessChild(i)!=-1){
					//if there is go to the child then search()
					search(key, n.accessChild(i));
				}
				else{
					//else return the location of where you place it
					return n;
				}
			}
			else if(key==n.accessKey(i)){
				//if key is in the node, return the node
				return n;
			}
			else if(i==bt.accessOrder()-1&&key>n.accessKey(i)){
				//if its the last one and its still greater than the last key, search the last child
				if(n.accessChild(i+1)!=-1){
					//if there is go to the child then search()
					search(key, n.accessChild(i+1));
				}
				else{
					//else return the node itself
					return n;
				}
			}
		}

		return null;
	}

	public static void split(Node n){
		//instantiate parent node of the node passed
		//get the middle index of the tree (sabi ni Sir okay lang if not even split as long as it splits)
		//middle index goes up to the upper index
		//left side becomes new node thats a left child
		//right side becomes new node thats a right child
		//write it all to the Btree file
	}

	public static void insert(long key, String[] value){
		//first, find where it is in the array and get the node
		Node location = search(key, bt.getRootNode());

		//case 1: node is not full, insert normally
		//first, keep comparing the values for all the keys in the node
		for(int i=0; i<n.accessOrder(); i++){
			
		}
		//if it reaches negative 1 insert it there
		//if its smaller than one of the keys, push all the other values back then put the key there
		//whatever operation you do in the keys array, you do it in the offset array and children array (if they have children)
		//then write the same node to the Btree to update changes

		//case 2: the node is full
		//first just do the same as case 1
		// once it has been inserted, split
	}

	public static void update(long key, String[] value){

	}

	public static void select(long key){

	}
}