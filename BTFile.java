import java.io.*;
import java.nio.*;

public class BTFile{
	private long numNodes;
	private long rootNode;
	private RandomAccessFile f;
	private final long OFFSET_VALUE = 0;

	public BTFile(String filename) throws IOException{
		File tf = new File(filename);

		if(!tf.exists()){
			this.rootNode = 0;
			this.numNodes = 1;
			this.f = new RandomAccessFile(filename, "rwd");
			//update the pointer to where it should start printing
			this.f.seek(OFFSET_VALUE);
			this.f.writeLong(this.numNodes);
			this.f.writeLong(this.rootNode);
			//initialize a root node
			Node n = new Node();
			n.makeEmptyNode();
			//writeNode(n);
		}
		else{
			this.f = new RandomAccessFile(filename, "rwd");
			this.f.seek(OFFSET_VALUE);
			this.numNodes = this.f.readLong();
			this.rootNode = this.f.readLong();
		}
	}

	public Node read(long k){
		//look for key within the tree then initialize a node and return it
		return null;
	}

	public void write(long k){
		//you get a key, place where it is in the binary tree then just print it inside
	}

	public void updateRootNode(long n){
		rootNode = n;
		//write root node into the file
	}

	public void updateNumNodes(long n){
		numNodes = n;
		//write number of nodes into the file
	}

	public Node getRootNode(){
		//first check the root node value in the file
		return null;
	}
}