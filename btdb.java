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
		//MAKE A CASE FOR WHEN THE PARENT DOESNT EXIST
		if(n.getParent()!=-1){
			Node parent = bt.read(n.getParent());
		}
		//get the middle index of the tree (sabi ni Sir okay lang if not even split as long as it splits)
		int middle = (n.accessOrder()/2)-1;
		//middle index goes up to the parent (get kung pangilang child siya, then it becomes the key with the same index)
		int childNum = 0;
		//while the first key of the child is greater than the key of the parent or you havent reached the end, keep counting
		for(int i=0; i<parent.accessOrder()-1; i++){
			if(parent.accessKey(i)==-1||n.accessKey(0)>parent.accessKey(i)){
				break;
			}
			childNum++;
		}
		//move all the components one space
		for(int i=parent.accessOrder()-1; i>childNum; i--){
			parent.assignKey(parent.accessKey(i),i+1);
			parent.assignChild(parent.accessChild(i),i+1);
			parent.assignOffset(parent.accessOffset(i),i+1);
		}
		//put Key in its respective place
		parent.assignKey(n.accessKey(middle), childNum);

		//left side becomes new node thats a left child
		Node leftChild = new Node();
		//put all values before middle in left child
		for(int i=0; i<middle; i++){
			leftChild.assignKey(n.accessKey(i), i);
			leftChild.assignChild(n.accessChild(i), i);
			leftChild.assignOffset(n.accessOffset(i), i);
		}
		//write into BTree file
		//parent.assignChild(leftChild());

		//right side becomes new node thats a right child
		Node rightChild = new Node();
		//put all values before middle in left child
		int counter = 0;
		for(int i=middle+1; i<n.accessOrder()-1; i++){
			leftChild.assignKey(n.accessKey(i), i);
			leftChild.assignChild(n.accessChild(i), i);
			leftChild.assignOffset(n.accessOffset(i), i);
		}
		//write it all to the Btree file
	}

	public static void insert(long key, String[] value){
		//first, find where it is in the array and get the node
		Node location = search(key, bt.getRootNode());

		//case 1: node is not full, insert normally
		//first, keep comparing the values for all the keys in the node
		for(int i=0; i<location.accessOrder()-1; i++){
			//if it reaches negative 1 insert it there
			if(location.accessKey(i)==-1){
				location.assignKey(key,i);
				//put value in values file
				//get the offset
				//assign offset to the offset key
			}
			//if its smaller than one of the keys, push all the other values back then put the key there
			if(key<location.accessKey(i)){
				for(int j=location.accessOrder()-1; j>i; j--){
					location.assignKey(location.acessKey(j), j+1);
					location.assignChild(location.accessChild(j), j+1);
					location.assignOffset(location.accessOffset(j), j+1);
				}

				location.assignKey(key,i);
				//put value in values file
				//get the offset
				//assign offset to the offset key
			}

			//whatever operation you do in the keys array, you do it in the offset array and children array (if they have children)
		}
		//check if the node is full, this means there is an element in the last node
		if(location.accessKey(location.accessOrder())!=0){
			//then split it
			//split();
		}
		
		//then write the same node to the Btree to update changes
	}

	public static void update(long key, String[] value){

	}

	public static void select(long key){

	}
}