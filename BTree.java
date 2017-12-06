import java.util.*;
import java.io.*;
import java.nio.file.*;

public class BTree{
	public static BTFile bt; //bt file
	public static ValuesFile values; //values file

	public BTree(BTFile b, ValuesFile v){
		//assign bt file and values file based on paremters once it is instantialized
		bt = b;
		values = v;
	}

	public static Node search(int key, long node){
		//first you read from the file, get root node and instantiate it
		Node n = null;
		try{
			n = bt.read(node);
		}
		catch(IOException ie){
			System.out.println("Cannot read node");
		}

		//check all the keys in the node
		for(int i=0; i<n.accessOrder()-1; i++){
			if(key<n.accessKey(i)||n.accessKey(i)==-1){
				//if less than, or if you find an empty slot in the node first check if there's a child
				if(n.accessChild(i)!=-1){
					//if there is go to the child then search it
					return search(key, n.accessChild(i));
				}
				else{
					//else return the location of the current node where you place it
					return n;
				}
			}
			else if(i==n.accessOrder()-2&&key>n.accessKey(i)){
				//if its the last one and its still greater than the last key, search the last child
				if(n.accessChild(i+1)!=-1){
					//if there is go to the child then search()
					return search(key, n.accessChild(i+1));
				}
				else{
					//else return the node itself
					return n;
				}
			}
			else if(key==n.accessKey(i)){
				//if key is in the node, return the node
				return n;
			}
		}

		//else it cannot be found in the tree 
		return null;
	}

	public static void split(Node n)throws IOException{
		//instantiate parent node of the node passed
		Node parent = null;
		//checks if parent does not exist
		boolean checkerP = n.accessParent()!=-1;
		if(checkerP){
			//if it exists, read it from the file
			try{
				parent = bt.read(n.accessParent());
			}
			catch(IOException ie){
				System.out.println("Cannot load parent");
			}
		}
		else{
			//if it doesnt, create a new node for the parent
			parent = new Node();
		}

		//get the middle index of the tree (sabi ni Sir okay lang if its not evenly split as long as it splits)
		int middle = (n.accessOrder()/2);
		//middle index goes up to the parent (get kung pangilang child siya, then it becomes the key with the same index)
		//the for loop below is to find out which child it is
		//while the first key of the child is not the same as the location of the child, keep counting
		int childNum = 0;
		for(int i=0; i<parent.accessOrder(); i++){
			if(n.getLocation()==parent.accessChild(i)){
				//once you find its location, then you assign that to be its child number
				childNum = i;
				break;
			}
		}

		//move all the components one space based on where the child was found
		parent.assignChild(parent.accessChild(parent.accessOrder()-1), parent.accessOrder());
		for(int i=parent.accessOrder()-2; i>=childNum; i--){
			parent.assignAll(parent.accessChild(i), parent.accessKey(i), parent.accessOffset(i), i+1);
		}

		//put the middle key and offset of the node your splitting in its respective place
		parent.assignKey(n.accessKey(middle), childNum);
		parent.assignOffset(n.accessOffset(middle), childNum);
		//right child will remain the same (contents will be the unsplitted cell)
		//move the location of the left child up with you
		parent.assignChild(n.accessChild(middle), childNum);

		//now, write parent to the btree file
		//if you created a new rootNode, set the parent as the root node
		if(!checkerP){
			bt.write(parent);
			bt.updateRootNode(parent.getLocation());
		}
		else{
			//if you didnt make a new parent, just update the existing one
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

		//assign left child to the left of the key that moved up to the parent
		parent.assignChild(leftChild.getLocation(), childNum);

		//right side becomes right child, this will just remain in the original node in the file
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
		//write it all to the Btree file (just update because your using the location of the old node)
		bt.update(rightChild, rightChild.getLocation());
		//assign right child to the right of the key that moved up to the parent
		parent.assignChild(rightChild.getLocation(), childNum+1);

		//now you have to upadte your parent since you made changes to its children
		bt.update(parent, parent.getLocation());

		//Check if parent will overfllow once you split
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
		//First, find where it is in the array and get the node
		Node location = search(key, bt.getRootNode());

		//Keep comparing the values for all the keys in the node
		for(int i=0; i<location.accessOrder(); i++){
			//if it reaches negative 1 insert it there
			if(location.accessKey(i)==-1){
				location.assignKey(key,i);
				//put value in values file
				values.write(value);
				//get the offset
				//assign offset to the offset key
				location.assignOffset(values.getRecordCount()-1, i);
				break;
			}
			//if key is found, dont do any of the operations. Place error message 
			if(key==location.accessKey(i)){
				System.out.println("ERROR: "+key+" already exists.");
				break;
			}
			//if its smaller than one of the keys, push all the other values back then put the key there
			if(key<location.accessKey(i)){
				for(int j=location.accessOrder()-2; j>=i; j--){
					location.assignAll(location.accessChild(j), location.accessKey(j), location.accessOffset(j), j+1);
				}
				location.assignKey(key,i);
				//put value in values file
				values.write(value);
				//get the offset
				//assign offset to the offset key
				location.assignOffset(values.getRecordCount()-1, i);
				break;
			}
		}

		//check if the node is full, this means there is an element in the last node	
		if(location.accessKey(location.accessOrder()-1)!=-1){
			//then split it
			try{
				split(location);
			}
			catch(IOException ie){

			}
			System.out.println(key+" inserted.");
		}
		else{
			//if not then just update the current node in the bt file		
			bt.update(location, location.getLocation());
			System.out.println(key+" inserted.");
		}
	}

	public static void updateFile(int key, String value){
		//look for its location in the tree based on the root node
		Node location = search(key, bt.getRootNode());

		boolean found = false;
		//check where the key  is in the node
		for(int i=0; i<location.accessOrder()-1; i++){
			if(key==location.accessKey(i)){	
				try{
					//update its value in the value's file once you find it
					values.update(value, location.accessOffset(i));
				}
				catch(IOException ie){}
				found = true;
				//print that you updated it
				System.out.println(location.accessKey(i)+" updated.");
				break;
			}
		}
		//if not found in node, print error
		if(!found){
			System.out.println("ERROR: "+key+" already exists.");
		}
	}

	public static void select(int key){
		//look for its location in the tree based on the root node
		Node location = search(key, bt.getRootNode());

		String output = "";
		boolean found = false;
		//look for where the key is in the btree
		for(int i=0; i<location.accessOrder()-1; i++){
			if(key==location.accessKey(i)){
				try{
					//get the value of the key in the values file
					output = values.access(location.accessOffset(i));
				}
				catch(IOException ie){}
				found = true;
				//print the output
				System.out.println(location.accessKey(i)+" ==> "+output);
				break;
			}
		}
		//if not found in node, print error
		if(!found){
			System.out.println("ERROR: "+key+" already exists.");
		}
	}
}