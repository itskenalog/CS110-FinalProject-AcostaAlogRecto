//check if we need to import something

public class Node{
	private long parent; //parent node 
	private long location; //place in the BTfile;
	private int[] keys; //array for the keys
	private long[] offset; //position
	private long[] children; //array for children
	private final int BT_ORDER = 5; //order of the trees

	public Node(){
		this.keys = new int[BT_ORDER];
		this.offset = new long[BT_ORDER];
		this.children = new long[BT_ORDER+1];	
		makeEmptyNode();
	}

	public void makeEmptyNode(){
		//assign everything to -1
		this.parent = -1;

		for(int i=0; i<BT_ORDER+1; i++){
			this.children[i]=-1;
		}

		for(int i=0; i<BT_ORDER; i++){
			this.keys[i]=-1;
		}

		for(int i=0; i<BT_ORDER; i++){
			this.offset[i]=-1;
		}
	}

	public void assignChild(long k, int i){
		//assign child  to the respective index
		this.children[i] = k;
	}

	public void assignOffset(long k, int i){
		//assign offset to respective index
		this.offset[i] = k;
	}

	public void assignKey(int k, int i){
		//assign key to respective index
		this.keys[i] = k;
	}

	public void assignAll(long c, long k, long o, int i){
		this.children[i] = c;
		this.keys[i] = (int)k;
		this.offset[i] = o;
	}

	public void assignParent(long k){
		//assign parent the integer
		this.parent = k;
	}

	public int accessKey(int i){
		//get the value of the kth key in the array and return it
		return this.keys[i];
	}

	public long accessChild(int i){
		//get the cth child in array then return it
		return this.children[i];
	}

	public long accessOffset(int i){
		return this.offset[i];
	}

	public long accessParent(){
		return this.parent;
	}

	public int accessOrder(){
		//returns order of the node and the tree
		return this.BT_ORDER;
	}

	public void setLocation(long l){
		location = l;
	}

	public long getLocation(){
		return location;
	}

}