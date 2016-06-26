package bitsAndPieces;

/**
 * 
 * @author catalin.podariu@gmail.com
 */
public class StringReferenceTest {

	private final String first = "Bali Shag";
	private final String second = "Bali Shag";
	private final String third = new String(first);

	public static void main(String[] args) {
		new StringReferenceTest().launch();
	}

	public void launch() {

		checkIfStringReferencesAreEqual();
		System.out.println();

		System.out.println(third.intern() == first); // true
		System.out.println(second == third.intern()); // true
		System.out.println("third.intern = " + getReference(third.intern()));

		System.out.println();
		checkIfStringReferencesAreEqual();
	}

	private void checkIfStringReferencesAreEqual() {
		System.out.println((first == second) + "  " //
				+ getReference(first) + ", " + getReference(second)); // true

		System.out.println((third == first) + "  " //
				+ getReference(third) + ", " + getReference(first)); // false

		System.out.println((second == third) + "  " //
				+ getReference(second) + ", " + getReference(third)); // false
	}

	// mimic the 'toString' method
	private String getReference(Object aString) {
		String result = aString.getClass().getName() + "@" //
				+ Integer.toHexString(System.identityHashCode(aString));
		return result;
	}
}
