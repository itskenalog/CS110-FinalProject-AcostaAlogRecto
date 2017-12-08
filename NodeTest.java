package bTreeDatabase;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NodeTest {
	private static Node n; 
	@Test
	public static void NodeTester(){
        n = new Node();

        assignAllTester();
        assignChildTester();
        assignKeyTester();
        assignOffsetTester();
        makeEmptyNodeTester();
        setLocationTester();
    }

    public static void assignAllTester() {
        long c = 3;
        long k = 10;
        long o = 0;
        int i = 0;
        n.assignAll(c, k, o, i);
        assertEquals(c+" "+k+" "+o, n.accessChild(i)+" "+n.accessKey(i)+" "+n.accessOffset(i));
    }

    public static void assignChildTester() {
        long c = 5;
        int i = 0;
        n.assignChild(c, i);
        assertEquals(c, n.accessChild(i));
    }

    public static void assignKeyTester() {
        int k = 5;
        int i = 0;
        n.assignKey(k, i);
        assertEquals(k, n.accessKey(i));
    }

    public static void assignOffsetTester() {
        long o = 5;
        int i = 0;
        n.assignOffset(o, i);
        assertEquals(o, n.accessOffset(i));
    }

    public static void makeEmptyNodeTester() {
        n.makeEmptyNode();
        assertEquals(-1, n.accessParent());
        for(int i=0; i<n.accessOrder(); i++) {
            assertEquals(-1, n.accessChild(i));
            assertEquals(-1, n.accessKey(i));
            assertEquals(-1, n.accessOffset(i));
        }
        assertEquals(-1, n.accessChild(n.accessOrder()-1));
    }

    public static void setLocationTester() {
        long l = 5;
        n.setLocation(5);
        assertEquals(l, n.getLocation());
    }

}
