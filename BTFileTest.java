import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.*;


public class BTFileTest 
{
	private static final String ToT = "Data.bt";
	@Test
	public void writeTest() throws IOException
	{
		Node nod = new Node();
		BTFile bf = new BTFile(ToT);
		bf.write(nod);
		long result = 0;
		
		assertEquals(nod, result);
		
	}
	
	public void updateTest() throws IOException
	{
		Node nod = new Node();
		BTFile bf = new BTFile(ToT);
		long location = 0;
		bf.update(nod, location);
		long result = bf.getNumNodes();
		
		assertEquals(nod, result);
		
	}


}
