import java.util.*;
import java.io.*;
import java.nio.file.*;

public class btdb{
	public static BTFile bt;
	public static ValuesFile values;

	public static void main(String args[]){
		//initialize values and bt file
		try{
			bt = new BTFile(args[0]);
			values = new ValuesFile(args[1]);
		}
		catch(IOException ie){

		}

		//accept input
		Scanner in = new Scanner(System.in);
		String inpu = in.nextLine();
		String[] input = inpu.split(" ");

		String value = "";
		for(int i=2; i<input.length; i++){
			value = value+input[i];
			if(i!=value.length()-1){
				value+=" ";
			}
		}

		if(input[0].equals("insert")){
			System.out.println(input[1]+" "+value);
			try{
				insert(Integer.parseInt(input[1]),value);
			}
			catch(IOException ie){

			}
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

	public static Node search(int key, long node){
		//first you read from the file and get root node
		//make a node based on what you read
		System.out.println("Entering search method");
		Node n = null;
		try{
			n = bt.read(node);
		}
		catch(IOException ie){
			System.out.println("Cannot read node");
		}
		System.out.print(n.accessParent()+" ");
			for(int i=0; i<n.accessOrder()-1; i++){
				System.out.print(n.accessChild(i)+" "+n.accessKey(i)+" "+n.accessOffset(i)+" ");
			}
			System.out.println(n.accessChild(n.accessOrder()-1));	
		//keep checking the keys until its less then
		for(int i=0; i<n.accessOrder()-1; i++){
			System.out.println(n.accessKey(i));
			if(key<n.accessKey(i)){
				//if less than, check if there's a child
				System.out.println(n.accessChild(i));
				if(n.accessChild(i)!=-1){
					//if there is go to the child then search()
					System.out.println("There is a child so searching");
					search(key, n.accessChild(i));
				}
				else{
					//else return the location of where you place it
					System.out.println("There is no child. Returning node");
					return n;
				}
			}
			else if(key==n.accessKey(i)){
				//if key is in the node, return the node
				System.out.println("Key found. Returning node");
				return n;
			}
			else if(i==n.accessOrder()-2&&key>n.accessKey(i)){
				//if its the last one and its still greater than the last key, search the last child
				System.out.println("Greater than all the keys. Searching last child");
				if(n.accessChild(i+1)!=-1){
					System.out.println("Found last child. Searching the child");
					//if there is go to the child then search()
					search(key, n.accessChild(i+1));
				}
				else{
					//else return the node itself
					System.out.println("No child. Returning this node");
					return n;
				}
			}
		}

		return null;
	}

	public static void split(Node n){
		//instantiate parent node of the node passed
		//MAKE A CASE FOR WHEN THE PARENT DOESNT EXIST
		System.out.println("Entering split method");
		Node parent = null;
		if(n.accessParent()!=-1){
			try{
				parent = bt.read(n.accessParent());
			}
			catch(IOException ie){

			}
		}
		//get the middle index of the tree (sabi ni Sir okay lang if not even split as long as it splits)
		int middle = (n.accessOrder()/2);
		//middle index goes up to the parent (get kung pangilang child siya, then it becomes the key with the same index)
		int childNum = 0;
		//while the first key of the child is not the same as the location of the child, keep counting
		for(int i=0; i<parent.accessOrder()-1; i++){
			if(n.getLocation()==parent.accessChild(i)){
				childNum = i;
				break;
			}
		}
		//move all the components one space
		for(int i=parent.accessOrder()-1; i>childNum; i--){
			parent.assignAll(parent.accessChild(i), parent.accessKey(i), parent.accessOffset(i), i+1);
		}
		//put Key in its respective place
		parent.assignKey(n.accessKey(middle), childNum);
		parent.assignOffset(n.accessOffset(middle), childNum);

		//left side becomes new node thats a left child
		Node leftChild = new Node();
		//put all values before middle in left child
		for(int i=0; i<middle; i++){
			leftChild.assignAll(n.accessChild(i), n.accessKey(i), n.accessOffset(i), i);
		}
		leftChild.assignParent(parent.getLocation());
		//write into BTree file
		parent.assignChild(leftChild.getLocation(), childNum);

		//right side becomes new node thats a right child
		Node rightChild = new Node();
		//put all values before middle in left child
		for(int i=1; i<=middle; i++){
			if(middle+i!=rightChild.accessOrder());
			rightChild.assignAll(n.accessChild(middle+i), n.accessKey(middle+i), n.accessOffset(middle+i), i-1);
		}
		rightChild.assignParent(parent.getLocation());
		//write it all to the Btree file
		parent.assignChild(rightChild.getLocation(), childNum+1);

		//Then check if parent is overflowing
		if(parent.accessKey(parent.accessOrder())!=-1){
			//If it is, you have to split parent
			split(parent);
		}

	}

	public static void insert(int key, String value) throws IOException{
		System.out.println("Entering insert method");
		//first, find where it is in the array and get the node
		Node location = search(key, bt.getRootNode());
		System.out.println("Found location");
		//case 1: node is not full, insert normally
		//first, keep comparing the values for all the keys in the node
		for(int i=0; i<location.accessOrder()-1; i++){
			//if it reaches negative 1 insert it there
			if(location.accessKey(i)==-1){
				System.out.println("Found -1. Inserting it here");
				location.assignKey(key,i);
				//put value in values file
				values.write(value);
				System.out.println("Sucessfully uploaded in values file");
				
				//get the offset
				//assign offset to the offset key
				location.assignOffset(values.getRecordCount()-1, i);
				break;
			}
			//if key is found, dont do any of the operations.Place error message
			if(key==location.accessKey(i)){
				System.out.println("ERROR: "+key+" already exists.");
				break;
			}
			//if its smaller than one of the keys, push all the other values back then put the key there
			if(key<location.accessKey(i)){
				System.out.println("Key must be inserted. Pushing all the values backward");
				for(int j=location.accessOrder()-2; j>=i; j--){
					System.out.println(j);
					location.assignAll(location.accessChild(j), location.accessKey(j), location.accessOffset(j), j+1);
				}
				System.out.print(location.accessParent()+" ");
				for(int a=0; a<location.accessOrder()-1; a++){
					System.out.print(location.accessChild(a)+" "+location.accessKey(a)+" "+location.accessOffset(a)+" ");
				}
				System.out.println(location.accessChild(location.accessOrder()-1));
				System.out.println(location.accessKey(location.accessOrder()-1));	
				System.out.println("Sucessfully pushed. Inserting");
				location.assignKey(key,i);
				//put value in values file
				values.write(value);
				System.out.println("Sucessfully uploaded in values file");

				//get the offset
				//assign offset to the offset key
				location.assignOffset(values.getRecordCount()-1, i);
				break;
			}
			//whatever operation you do in the keys array, you do it in the offset array and children array (if they have children)
		}
		//check if the node is full, this means there is an element in the last node	
		if(location.accessKey(location.accessOrder()-1)!=-1){
			//then split it
			split(location);
		}
		else{
			System.out.println(location.getLocation());
			//if not then just update the current node in the bt file
			System.out.print(location.accessParent()+" ");
			for(int i=0; i<location.accessOrder()-1; i++){
				System.out.print(location.accessChild(i)+" "+location.accessKey(i)+" "+location.accessOffset(i)+" ");
			}
			System.out.println(location.accessChild(location.accessOrder()-1));			
			bt.update(location, location.getLocation());
		}
	}

	public static void update(int key, String value){

	}

	public static void select(int key){

	}
}