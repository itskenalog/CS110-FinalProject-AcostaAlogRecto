import java.io.*;
import java.nio.*;

public class BTFile{
	private long numNodes; //number of nodes in the file
	private long rootNode; //location of root node in file
	private RandomAccessFile f; //file itself where you store all the data
	private final int OFFSET_VALUE = 0; //where you start placing the data
	private final int START_OF_ENTRIES = 16; //where the start of the entries begin
	private final int BT_ORDER = 7; // order of the Btree
	private final int NODES_SIZE = 3*BT_ORDER-1; //how big each of the nodes will be
	private final int BYTESIZE = NODES_SIZE*8; //how many bytes the nodes will occupy

	public BTFile(String filename) throws IOException{
		//first check if file was already created
		File tf = new File(filename);

		if(!tf.exists()){
			//if not, make a new file
			this.rootNode = 0;
			this.numNodes = 0;
			this.f = new RandomAccessFile(filename, "rwd");
			//update the pointer to where it should start printing
			//write the number of nodes and location of root node in file
			this.f.seek(OFFSET_VALUE);
			this.f.writeLong(this.numNodes);
			this.f.seek(OFFSET_VALUE+START_OF_ENTRIES/2);
			this.f.writeLong(this.rootNode);
			//initialize a root node
			Node n = new Node();
			//write root node to the file
			write(n);
		}
		else{
			//if file exists
			this.f = new RandomAccessFile(filename, "rwd");
			//read the number of nodes and location of the root node from file
			this.f.seek(OFFSET_VALUE);
			this.numNodes = this.f.readLong();
			this.f.seek(OFFSET_VALUE+START_OF_ENTRIES/2);
			this.rootNode = this.f.readLong();
		}
	}

	public Node read(long k) throws IOException{
		//go to where node's information is
		this.f.seek((BYTESIZE*k)+START_OF_ENTRIES);
		//instantiate Node  
		Node n = new Node();
		//set the location of the node so you know where it is in the file
		n.setLocation(k);

		//assign information to the node based on what you read in the file
		n.assignParent(this.f.readLong());
		for(int i=0; i<n.accessOrder()-1; i++){
			n.assignAll(this.f.readLong(), (int)this.f.readLong(), this.f.readLong(), i);
		}
		n.assignChild(this.f.readLong(), n.accessOrder()-1);

		//return th node
		return n;
	}

	public void write(Node n) throws IOException{
		//call the update function so that you write your node in the next empty space
		update(n, numNodes);

		//update your total number of nodes and write it to the file;
		numNodes++;
		this.f.seek(OFFSET_VALUE);
		this.f.writeLong(this.numNodes);

		//set location of Node once you print it (it will be number of nodes-1)
		n.setLocation(numNodes-1);
	}

	public void update(Node n, long l)throws IOException{
		//change an existing node based on the location given;
		//go to location
		this.f.seek(BYTESIZE*l+START_OF_ENTRIES);

		//write the data from the node to the file
		this.f.writeLong(n.accessParent());
		for(int i=0; i<n.accessOrder()-1; i++){
			this.f.writeLong(n.accessChild(i));
			this.f.writeLong(n.accessKey(i));
			this.f.writeLong(n.accessOffset(i));
		}
		this.f.writeLong(n.accessChild(n.accessOrder()-1));
	}

	public void updateRootNode(long n){
		//change the value of the root node based on parameter
		rootNode = n;
		try{
			//write root node into the file
			this.f.seek(OFFSET_VALUE+START_OF_ENTRIES/2);
			this.f.writeLong(this.rootNode);
		}
		catch(IOException ie){
			
		}
	}

	public long getRootNode(){
		//return root node's location
		return rootNode;
	}

	public long getNumNodes(){
		//return number of nodes in the file
		return numNodes;
	}

	public void close() throws IOException{
		//close the file
		f.close();
	}
}