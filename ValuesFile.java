import java.io.*;
import java.nio.*;

public class ValuesFile{
	private long recordCount; //number of records in the flie
	private RandomAccessFile f; // file (you can use file ints from here)
	private final int OFFSET_VALUE = 0;
	private final int START_OF_ENTRIES = 8;
	private final int BYTES = 256; 

	public ValuesFile(String filename) throws IOException{
		File tf = new File(filename);

		if(!tf.exists()){
			System.out.println("New File");
			this.recordCount = 0;
			this.f = new RandomAccessFile(filename, "rwd");
			//update the pointer to where it should start printing
			this.f.seek(OFFSET_VALUE);
			this.f.writeLong(this.recordCount);
		}
		else{
			System.out.println("File Exists");
			this.f = new RandomAccessFile(filename, "rwd");
			this.f.seek(OFFSET_VALUE);
			this.recordCount = this.f.readLong();
		}
	}

	public String access(Long s) throws IOException{
		//get a value from the file
		//you look for the word in the file
		//RandomAccessFile.seek(8 + i * 256) <-- code to point to where it is in the array
		this.f.seek(OFFSET_VALUE);
		this.f.seek(START_OF_ENTRIES+s*BYTES);
		short length = this.f.readShort();
		byte[] byteArray = new byte[length];
		for(int i=0; i<length; i++){
			byteArray[i] = this.f.readByte();
		}
		return new String(byteArray, "UTF8");
	}

	public void write(String s) throws IOException{
		//write into the file
		//first you access for where it is
		//write the length of the string
		//then write the string itself
		//256 bytes per slot
		byte[] byteArray = s.getBytes("UTF8");
		this.f.seek(START_OF_ENTRIES+(recordCount)*BYTES);
		this.f.writeShort(byteArray.length);
		this.f.write(byteArray);

		recordCount++;
		this.f.seek(OFFSET_VALUE);
		this.f.writeLong(recordCount);

	}

	public long getRecordCount(){
		return recordCount;
	}
}