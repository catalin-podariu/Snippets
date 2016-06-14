package factories;

/**
 * 
 * @author catalin.podariu@gmail.com
 */
public class God {

	public static void main(String[] args) {
		new God().launch();
	}

	public void launch() {
		/**
		 * Most readable object creation known to java. And me.
		 */
		Tree tinyTree = TreeFactory.createTree()
								.withColor("Green")
								.withBranches(300)
								.withShape("Weird Shape, that's for sure..")
								.build();
		tinyTree.tellTheWorld();

		System.out.println("Verify: \n"
				+ "branches " + tinyTree.getBranchNumber()
				+ "\ncolor " + tinyTree.getColor()
				+ "\nshape " + tinyTree.getShape());
	}
}

class Tree {

	private TreeFactory factory;

	// constructor gets just the factory reference
	public Tree(TreeFactory factory) {
		this.factory = factory;
	}

	public void tellTheWorld() {
		System.out.println("Immutable Crown created!");
	}

	/**
	 * Create just getters! So 'this' is immutable.
	 */
	protected String getColor() {
		return factory.color;
	}

	protected String getShape() {
		return factory.shape;
	}

	protected int getBranchNumber() {
		return factory.branchNumber;
	}
}

/*
 * This creates the object.
 */
class TreeFactory {

	protected String color;
	protected String shape;
	protected int branchNumber;

	// private default constructor!
	private TreeFactory() {
	}

	// this is the static factory that makes it all glow.
	public static TreeFactory createTree() {
		return new TreeFactory();
	}

	public TreeFactory withColor(String newColor) {
		color = newColor;
		return this;
	}

	public TreeFactory withShape(String newShape) {
		shape = newShape;
		return this;
	}

	public TreeFactory withBranches(int newNumber) {
		branchNumber = newNumber;
		return this;
	}

	public Tree build() {
		return new Tree(this);
	}
}