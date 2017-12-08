package bTreeDatabase;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import org.junit.jupiter.api.Test;

public class ValuesFileTest {
	private static final String xd = "Data.values";
	@Test
	
	public void writeTest() throws IOException{
		ValuesFile vf = new ValuesFile(xd);
		vf.write("Hello");
		long location = 0;
		String result = vf.access(location);
		assertEquals("Hello", result);
	}
	
	public void updateTest() throws IOException{
		ValuesFile vf = new ValuesFile(xd);
		vf.update("Hi", 0);
		long location = 0;
		String result = vf.access(location);
		assertEquals("Hi", result);
	}
}
