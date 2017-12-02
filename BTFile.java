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
		//go to where node is
		this.f.seek((256*k)+16);
		//instantiate Node  
		Node n = new Node();

		//assign stuff based on what you read in the file
		n.assignParent(this.f.readLong);

		for(int i=0; i<n.accessOrder(); i++){
			n.assignChild(this.f.readLong(), i);
			n.assignKey(this.f.readInt(), i);
			n.assignOffset(this.f.readLong(), i);
		}

		n.assignChild(this.f.readLong(), n.accessOrder()-1);

		return n;
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

	public long getRootNode(){
		//first check the root node value in the file
		return rootNode;
	}
}