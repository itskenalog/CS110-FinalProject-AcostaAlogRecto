import java.io.*;
import java.nio.*;

public class ValuesFile{
	private long recordCount; //number of records in the flie
	private RandomAccessFile f; // the file itself where you store the values
	private final int OFFSET_VALUE = 0; //where you start placing the data
	private final int START_OF_ENTRIES = 8; //where you begin putting the values for each key
	private final int BYTES = 258; //how many bytes each value can contain

	public ValuesFile(String filename) throws IOException{
		//first check if file exists
		File tf = new File(filename);

		if(!tf.exists()){
			//if it doesnt exist, create a new one
			this.recordCount = 0;
			this.f = new RandomAccessFile(filename, "rwd");
			//update the pointer to where it should start printing
			this.f.seek(OFFSET_VALUE);
			//write how many records there are in the file
			this.f.writeLong(this.recordCount);
		}
		else{
			//if it exists
			this.f = new RandomAccessFile(filename, "rwd");
			//read how many values it contains in the file
			this.f.seek(OFFSET_VALUE);
			this.recordCount = this.f.readLong();
		}
	}

	public String access(Long s) throws IOException{
		//get a value from the file
		//go to where it is in the file based on paremeter
		this.f.seek(OFFSET_VALUE+START_OF_ENTRIES+(s*BYTES));
		//get length of the string
		short length = this.f.readShort();
		byte[] byteArray = new byte[length];
		for(int i=0; i<length; i++){
			byteArray[i] = this.f.readByte();
		}
		//convert the byte array to string before returning it
		return new String(byteArray, "UTF8");
	}

	public void write(String s) throws IOException{
		//first call the update function to write it to the file at the end of all the records
		update(s, recordCount);

		//update the number of records in the file
		recordCount++;
		//write how many new records there are
		this.f.seek(OFFSET_VALUE);
		this.f.writeLong(recordCount);

	}

	public void update(String s, long i) throws IOException{
		//convert the string that you got into bytes and store it in a byte array
		byte[] byteArray = s.getBytes("UTF8");
		//go to where it is in the file
		this.f.seek(START_OF_ENTRIES+(i)*BYTES);
		//write the length of the array first followed by the contents of the byte array itself
		this.f.writeShort(byteArray.length);
		this.f.write(byteArray);
	}

	public long getRecordCount(){
		//return how many records here are in the file
		return recordCount;
	}

	public void close() throws IOException{
		//close the file
		f.close();
	}
}