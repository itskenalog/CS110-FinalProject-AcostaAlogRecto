//check if we need to import something

public class Node{
	private Node parent;
	private int[] keys;
	private long[] offset;
	private Node[] children;
	private final long BT_ORDER = 5;

	public Node(Node p){
		this.parent = p;
		this.keys = new int[BT_ORDER-1];
		this.offset = new long[BT_ORDER-1];
		this.children = new Node[BT_ORDER];
	}
}