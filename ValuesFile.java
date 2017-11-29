import java.io.*;
import java.nio.*;

public class ValuesFile{
	private long recordCount; //number of records in the flie
	private File f; // file (you can use file ints from here)
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

	public void access(Long s){
		//get a value from the file
		//you look for the word in the file
		//RandomAccessFile.seek(8 + i*256) <-- code to point to where it is in the array
	}

	public void write(String s){
		//write into the file
		//first you access for where it is
		//write the length of the string
		//then write the string itself
		//256 bytes per slot
	}

	public void print(){
		//access first to find out where to print
		//check the length
		//print the thing on the access based on the length of the string
	}
}