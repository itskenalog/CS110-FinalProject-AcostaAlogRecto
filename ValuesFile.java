import java.io.*;
import java.nio.*;

public class ValuesFile{
	private long recordCount; //number of records in the flie
	private RandomAccessFile f; // file (you can use file ints from here)
	private final int OFFSET_VALUE = 0;
	private final int BYTES = 256; 

	public ValuesFile(String filename) throws IOException{
		File tf = new File(filename);

		if(!tf.exists()){
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

	public void access(Long s){
		//get a value from the file
		//you look for the word in the file
		//RandomAccessFile.seek(8 + i * 256) <-- code to point to where it is in the array
			this.f.seek(0);
			System.out.println(this.f.readlong());
			int recordCount = 0;
			int w = 0;
			for (int i = 1; i <= 4; ++i){
				raf.seek(8 + w * BYTES);
				length = this.f.readShort();
				byte[] byteArray = new byte[length];
				this.f.read(byteArray);
				System.out.println(length);
				System.out.println(new String (byteArray, "UTF8"));
				w++;
			}
			this.f.seek(8 + (s + 1)) * BYTES));
			int length = this.f.readShort();
			byte[] byteArray = new byte[length];
			this.f.read(byteArray);
			System.out.println(new String(byteArray, "UTF8"));
			return new String(byteArray, "UTF8");
	}

	public void write(String s){
		//write into the file
		//first you access for where it is
		//write the length of the string
		//then write the string itself
		//256 bytes per slot
		recordCount++;
		byte[] byteArray = s.getBytes("UTF8");
		this.f.writeShort(byteArray.length);
		this.f.write(byteArray);
		this.f.seek(0);
		this.f.writeLong(recordCount);
	}

	public void print(){
		//access first to find out where to print
		//check the length
		//print the thing on the access based on the length of the string
	}
}