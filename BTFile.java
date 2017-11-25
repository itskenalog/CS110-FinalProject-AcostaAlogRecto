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
			this.f.writeLong(this.recordCount);
		}
		else{
			this.f = new RandomAccessFile(filename, "rwd");
			this.f.seek(OFFSET_VALUE);
			this.recordCount = this.f.readLong();
		}
	}
}