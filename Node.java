//check if we need to import something

public class Node{
	private Node parent; //parent node 
	private int[] keys; //array for the keys
	private long[] offset; //position
	private Node[] children; //array for children
	private final long BT_ORDER = 5; //order of the trees

	public Node(Node p){
		this.parent = p;
		this.keys = new int[BT_ORDER-1];
		this.offset = new long[BT_ORDER-1];
		this.children = new Node[BT_ORDER];
	}

	public int accessKey(long k){
		//get the value of the kth key in the array and return it
		return this.keys[k];
	}

	public Node accessChild(long c){
		//get the cth child in array then return it
		return this.children[c];
	}

	public Node accessParent(){
		return this.parent;
	}
}