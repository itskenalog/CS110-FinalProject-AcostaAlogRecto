import java.io.*;
import java.nio.*;

public class BTFile{
	private long numNodes;
	private long rootNode;
	private File f;
	private final long OFFSET_VALUE = 0;

	public BTFile(String filename) throws IOException{
		this.f = new File(filename);

		if(!f.exists()){
			this.rootNode = 0;
			this.numNodes = 1;
			this.f = new RandomAccessFile(filename, "rwd");
			//update the pointer to where it should start printing
			this.f.seek(OFFSET_VALUE);
			this.writeLong(this.numNodes);
			this.writeLong(this.rootNode);
			this.f.writeLong(this.recordCount);
		}
		else{
			this.f = new RandomAccessFile(filename, "rwd");
			this.f.seek(OFFSET_VALUE);
			this.numNodes = this.f.readLong();
			this.rootNodes = this.f.readLong();
		}
	}

	public void updateRootNode(long n){
		rootNode = n;
		//write root node into the file
	}

	public void updateNumNodes(long n){
		numeNodes = n;
		//write number of nodes into the file
	}

	public Node getRootNode(){
		//first check the root node value in the file
	}
}