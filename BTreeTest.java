package bTreeDatabase;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BTreeTest {
	private static final String px = "Data.bt";
	private static final String xd = "Data.values";
	public static BTree bee3;
	

	@Test
	
	public void searchTest() throws IOException{
		BTFile bt = new BTFile(px);
		ValuesFile values = new ValuesFile(xd);
		BTree bee3 = new BTree(bt, values);
		int k = 123;
		String s = "Hello";
		Node n = bt.read(bt.getRootNode());
		bee3.insert(k, s);
		Node result = bee3.search(k, n);
		assertEquals (s, values.access(result.accessOffset(0)));
	}
	
	public void splitTest() {
		BTFile bt = new BTFile(px);
		ValuesFile values = new ValuesFile(xd);
		BTree bee3 = new BTree(bt, values);
		int[] k = new int[bt.BT_ORDER];
		String[] s = new String[bt.BT_ORDER];
		for(int i=0; i<bt.BT_ORDER; i++) {
			k[i] = i;
			s[i] = i+"";
			bee3.insert(k[i], s[i]);
			//automatically splits after doing this
		}
		//check if right node is right
		Node tempr = bt.read(0)
		for(int i=0; i<bt.BT_ORDER/2; i++) {
			assertEquals(k[(bt.BT_ORDER/2)+i+1], tempr.accessKey(i));
		}
		//check if left node is right
		Node templ = bt.read(2)
		for(int i=0; i<bt.BT_ORDER/2; i++) {
			assertEquals(k[i], templ.accessKey(i));
		}
		//check if parent is right
		Node tempp = bt.read(1);
		assertEquals(k[bt.BT_ORDER/2], tempp.accessKey(0));
	}
	
	public void insertTest() {
		BTFile bt = new BTFile(px);
		ValuesFile values = new ValuesFile(xd);
		BTree bee3 = new BTree(bt, values);
		int k = 123;
		String s = "Hello";
		bee3.insert(k, s);
		Node n = bt.read(0);
		Node result = bee3.search(123, n);
		assertEquals (k, result.accessKey(0));
	}
	
	public void updateTest() {
		BTFile bt = new BTFile(px);
		ValuesFile values = new ValuesFile(xd);
		BTree bee3 = new BTree(bt, values);
		int k = 123;
		String s1 = "Hello";
		String s2 = "Hi";
		bee3.insert(k, s1);
		bee3.updateFile(k, s2);
		Node n = bt.read(bt.getRootNode());
		Node result = bee3.search(k, n);
		assertEquals (s1, values.access(result.accessOffset(0)));
	}
	
	public void selectTest() {
		BTFile bt = new BTFile(px);
		ValuesFile values = new ValuesFile(xd);
		BTree bee3 = new BTree(bt, values);
		int k = 123;
		String s1 = "Hello";
		bee3.insert(k, s1);
		String result = bee3.select(k);
		assertEquals (k+" ==> "+s, result);
	}
}