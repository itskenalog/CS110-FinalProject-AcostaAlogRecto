import java.io.*;
import java.nio.*;

public class ValuesFile{
	private long recordCount;
	private File f;
	private final OFFSET_VALUE = 0;

	public ValuesFile(String filename) throws IOException{
		this.f = new File(filename);

		if(!f.exists()){
			this.recordCount = 0;
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