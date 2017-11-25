import java.util.*;
import java.io.*;
import java.nio.file.*;

public class btdb{
	public static void main(String args[]){
		BTFile bt = new BTFile(args[0]);
		ValuesFile values = new ValuesFile(args[1]);
	}
}