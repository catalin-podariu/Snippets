package bitsAndPieces;

/**
 * 
 * @author catalin.podariu@gmail.com
 */
public class StringReferenceTest {

	String first = "Bali Shag";
	String second = "Bali Shag";
	String third = new String(first);

	public static void main(String[] args) {
		new StringReferenceTest().launch();
	}

	public void launch() {

		checkIfStringReferencesAreEqual();
		System.out.println();

		System.out.println(third.intern() == first); // true
		System.out.println(second == third.intern()); // true

		System.out.println();
		checkIfStringReferencesAreEqual();
	}

	private void checkIfStringReferencesAreEqual() {
		System.out.println((first == second) + "" //
				+ first.hashCode() + ", " + second.hashCode()); // true

		System.out.println((third == first) + "" //
				+ third.hashCode() + ", " + first.hashCode()); // false

		System.out.println((second == third) + "" //
				+ second.hashCode() + ", " + third.hashCode()); // false
	}
}
