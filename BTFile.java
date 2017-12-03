import java.io.*;
import java.nio.*;

public class BTFile{
	private long numNodes;
	private long rootNode;
	private RandomAccessFile f;
	private final int OFFSET_VALUE = 0;
	private final int START_OF_ENTRIES = 16;
	private final int BYTESIZE = 256;

	public BTFile(String filename) throws IOException{
		File tf = new File(filename);

		if(!tf.exists()){
			System.out.println("New File");
			this.rootNode = 0;
			this.numNodes = 0;
			this.f = new RandomAccessFile(filename, "rwd");
			//update the pointer to where it should start printing
			this.f.seek(OFFSET_VALUE);
			this.f.writeLong(this.numNodes);
			this.f.seek(OFFSET_VALUE+START_OF_ENTRIES/2);
			this.f.writeLong(this.rootNode);
			//initialize a root node
			Node n = new Node();
			write(n);
		}
		else{
			System.out.println("File Exists");
			this.f = new RandomAccessFile(filename, "rwd");
			this.f.seek(OFFSET_VALUE);
			this.numNodes = this.f.readLong();
			this.f.seek(OFFSET_VALUE+START_OF_ENTRIES/2);
			this.rootNode = this.f.readLong();
		}
	}

	public Node read(long k) throws IOException{
		//go to where node is
		this.f.seek((BYTESIZE*k)+START_OF_ENTRIES);
		//instantiate Node  
		Node n = new Node();
		n.setLocation(k);

		//assign stuff based on what you read in the file
		n.assignParent(this.f.readLong());

		for(int i=0; i<n.accessOrder()-1; i++){
			n.assignAll(this.f.readLong(), this.f.readLong(), this.f.readLong(), i);
		}

		n.assignChild(this.f.readLong(), n.accessOrder()-1);

		return n;
	}

	public void write(Node n) throws IOException{
		//a node, then update the values in the entire node based on what you are given
		update(n, numNodes);

		//update numNodes;
		numNodes++;
		this.f.seek(OFFSET_VALUE);
		this.f.writeLong(this.numNodes);

		//set location of Node once you print it (it will be recordCount-1)
		n.setLocation(numNodes-1);
	}

	public void update(Node n, long l)throws IOException{
		//change an existing node based on the location given;
		this.f.seek(BYTESIZE*l+START_OF_ENTRIES);
		this.f.writeLong(n.accessParent());

		for(int i=0; i<n.accessOrder()-1; i++){
			this.f.writeLong(n.accessChild(i));
			this.f.writeLong(n.accessKey(i));
			this.f.writeLong(n.accessOffset(i));
		}

		this.f.writeLong(n.accessChild(n.accessOrder()-1));
	}

	public void updateRootNode(long n)throws IOException{
		rootNode = n;
		//write root node into the filethis.f.seek(OFFSET_VALUE);
		this.f.seek(OFFSET_VALUE+START_OF_ENTRIES/2);
		this.f.writeLong(this.rootNode);
	}

	public long getRootNode(){
		return rootNode;
	}

	public long getNumNodes(){
		return numNodes;
	}
}