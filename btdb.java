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

		while(!input[0].equals("exit")){
			String value = "";
			if(input.length>=2){
				for(int i=2; i<input.length; i++){
					value = value+input[i];
					if(i!=value.length()-1){
						value+=" ";
					}
				}
			}

			if(input[0].equals("insert")){
				System.out.println(input[1]+" "+value);
				try{
					insert(Integer.parseInt(input[1]), value);
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

			inpu = in.nextLine();
			input = inpu.split(" ");
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
		
		//keep checking the keys until its less then
		for(int i=0; i<n.accessOrder()-1; i++){
			if(key<n.accessKey(i)||n.accessKey(i)==-1){
				System.out.println("Key is less than accessKey");
				//if less than, check if there's a child
				if(n.accessChild(i)!=-1){
					//if there is go to the child then search()
					System.out.println("There is a child so searching");
					return search(key, n.accessChild(i));
				}
				else{
					//else return the location of where you place it
					System.out.println("There is no child. Returning node");
					return n;
				}
			}
			else if(i==n.accessOrder()-2&&key>n.accessKey(i)){
				//if its the last one and its still greater than the last key, search the last child
				System.out.println("Greater than all the keys. Searching last child");
				if(n.accessChild(i+1)!=-1){
					System.out.println("Found last child. Searching the child");
					//if there is go to the child then search()
					return search(key, n.accessChild(i+1));
				}
				else{
					//else return the node itself
					System.out.println("No child. Returning this node");
					return n;
				}
			}
			/*
			else if(key>n.accessKey(i)&&key<n.accessKey(i+1)){
				System.out.println("Key is greater than accessKey");
				if(n.accessChild(i+1)!=-1){
					//if there is go to the child then search()
					System.out.println("There is a child so searching");
					return search(key, n.accessChild(i));
				}
			}
			*/
			else if(key==n.accessKey(i)){
				//if key is in the node, return the node
				System.out.println("Key found. Returning node");
				return n;
			}
			/*
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
			*/
		}

		return null;
	}

	public static void split(Node n)throws IOException{
		//instantiate parent node of the node passed
		System.out.println("Entering split method");
		Node parent = null;
		//checks if parent does not exist
		boolean checkerP = n.accessParent()!=-1;
		if(checkerP){
			System.out.println("Found parent. Loading parent");
			try{
				parent = bt.read(n.accessParent());
			}
			catch(IOException ie){
				System.out.println("Cannot load parent");
			}
		}
		else{
			System.out.println("No parent. Creating a new parent");
			parent = new Node();
		}

		//get the middle index of the tree (sabi ni Sir okay lang if not even split as long as it splits)
		int middle = (n.accessOrder()/2);
		//middle index goes up to the parent (get kung pangilang child siya, then it becomes the key with the same index)
		int childNum = 0;
		//while the first key of the child is not the same as the location of the child, keep counting
		for(int i=0; i<parent.accessOrder()-1; i++){
			if(n.getLocation()==parent.accessChild(i)){
				childNum = i;
			}
		}

		//move all the components one space based on where the child was found
		parent.assignChild(parent.accessChild(parent.accessOrder()-1), parent.accessOrder());
		for(int i=parent.accessOrder()-2; i>=childNum; i--){
			parent.assignAll(parent.accessChild(i), parent.accessKey(i), parent.accessOffset(i), i+1);
		}

		//put Key in its respective place
		parent.assignKey(n.accessKey(middle), childNum);
		parent.assignOffset(n.accessOffset(middle), childNum);
		//right child will remain the same (contents will be the unsplitted cell)
		//move the left child up with you
		parent.assignChild(n.accessChild(middle), childNum);





		System.out.print(parent.accessParent()+" ");
		for(int a=0; a<parent.accessOrder(); a++){
			System.out.print(parent.accessChild(a)+" "+parent.accessKey(a)+" "+parent.accessOffset(a)+" ");
		}
		System.out.println(parent.accessChild(parent.accessOrder()));



		//now, write parent to the btree file
		//if you created a new rootNode, set the parent as the root node
		if(!checkerP){
			bt.write(parent);
			bt.updateRootNode(parent.getLocation());
		}
		else{
			bt.update(parent, parent.getLocation());
		}





		//left side becomes a new node which will be the left child
		Node leftChild = new Node();

		//put all values before middle in left child
		for(int i=0; i<middle; i++){
			leftChild.assignAll(n.accessChild(i), n.accessKey(i), n.accessOffset(i), i);
			if(i==middle-1){
				leftChild.assignChild(n.accessChild(middle), middle);
			}
		}
		//assign left child to parent
		leftChild.assignParent(parent.getLocation());
		//write into BTree file
		bt.write(leftChild);

		//after this, you go through the children of the left child and make them point to it (because at this point, they are still pointing at the original location)
		for(int i=0; i<=middle; i++){
			if(leftChild.accessChild(i)!=-1){
				Node tempChild = bt.read(leftChild.accessChild(i));
				tempChild.assignParent(leftChild.getLocation());
				bt.update(tempChild, tempChild.getLocation());
			}
		}

		//assign parent's correct child slot to the left child
		parent.assignChild(leftChild.getLocation(), childNum);



		//right side becomes right child, this will just remain in the original node in the file if you did not create a new parent
		Node rightChild = bt.read(n.getLocation());

		//move all values after middle to the start of the node and clear all extraneous values
		rightChild.assignAll(-1,-1,-1, middle);
		for(int i=0; i<middle; i++){
			rightChild.assignAll(n.accessChild(middle+i+1), n.accessKey(middle+i+1), n.accessOffset(middle+i+1), i);
			//after moving it, make those original values negative 1
			rightChild.assignAll(-1, -1, -1, middle+i+1);
			if(i==middle-1){
				rightChild.assignChild(n.accessChild(n.accessOrder()), middle);
				rightChild.assignChild(-1, middle+i+2);
			}
		}
		rightChild.assignParent(parent.getLocation());
		//write it all to the Btree file, first check if you created a new parent or not
		bt.update(rightChild, rightChild.getLocation());
		parent.assignChild(rightChild.getLocation(), childNum+1);

		System.out.println("Done with left child and right child");


		//now you have to upadte your parent since you made changes to its children
		bt.update(parent, parent.getLocation());







		for(int i=0; i<bt.getNumNodes(); i++){
			Node alright = bt.read(i);
			System.out.print(alright.accessParent()+" ");
			for(int a=0; a<alright.accessOrder()-1; a++){
				System.out.print(alright.accessChild(a)+" "+alright.accessKey(a)+" "+alright.accessOffset(a)+" ");
			}
			System.out.println(alright.accessChild(alright.accessOrder()-1));
		}









		//First check if parent will overfllow once you split
		if(parent.accessKey(parent.accessOrder()-1)!=-1){
			//If it is, you have to split parent
			try{
				split(parent);
			}
			catch(IOException ie){

			}
		}
	}

	public static void insert(int key, String value) throws IOException{
		System.out.println("Entering insert method");
		//first, find where it is in the array and get the node
		Node location = search(key, bt.getRootNode());
		System.out.println("Found location "+location);

		//first, keep comparing the values for all the keys in the node
		for(int i=0; i<location.accessOrder(); i++){
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

		System.out.print(location.accessParent()+" ");
		for(int a=0; a<location.accessOrder()-1; a++){
			System.out.print(location.accessChild(a)+" "+location.accessKey(a)+" "+location.accessOffset(a)+" ");
		}
		System.out.println(location.accessChild(location.accessOrder()-1));

		//check if the node is full, this means there is an element in the last node	
		if(location.accessKey(location.accessOrder()-1)!=-1){
			//then split it
			try{
				split(location);
			}
			catch(IOException ie){

			}
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
		Node location = search(key, bt.getRootNode());
		if (location == null){
			System.out.println("error, node not found");
		}
		else{
			for(int i=0; i<location.accessOrder()-1; i++){
				location.assignKey(key,i);
				try{
					values.write(value);
				}
				catch(IOException ie){

				}
				System.out.println("Successfully updated");
			}

		}

	}

	public static void select(int key){

	}
}