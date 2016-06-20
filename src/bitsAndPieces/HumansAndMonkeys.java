package bitsAndPieces;

/**
 * Usage of default methods, lambdas and generics.
 * 
 * @author catalin.podariu@gmail.com
 */
public class HumansAndMonkeys {

	public static void main(String[] args) {
		new HumansAndMonkeys().launch();
	}

	public void launch() {
		createBob();

		createLarry();
	}

	private void createBob() {
		Animal protoBob = new Human("Bob");
		protoBob.init();
		Human bob = (Human) protoBob;

		bob.askQuestions(); // implemented from Wisdom
		bob.beCurious(); // implemented from HumanNature

		bob.seekKnowledge("Bob"); // default in Wisdom
		bob.walkAround("Bob"); // default in Wisdom

		bob.sleepLate("Bob"); // default in HumanNature
		bob.dislikeVeggies("Bob"); // default in HumanNature
	}

	private void createLarry() {
		Chimp larry = new Chimp(new HumanNature() {
			@Override
			public void beCurious() {
				System.out.println("Chimps are always curious!");

				this.dislikeVeggies("Larry"); // default in HumanNature
				this.sleepLate("Larry"); // default in HumanNature
			}
		});
		larry.init();
		HumanNature naturalChimp = larry.getHumanNature();
		naturalChimp.beCurious(); // implemented from HumanNature
	}
}

class Animal {
	private Wisdom wisdom;
	private Chimp chimp;

	public Animal() {
	}

	public Animal(Wisdom wisdom) {
		this.wisdom = wisdom;
	}

	public Animal(Chimp chimp) {
		this.chimp = chimp;
	}

	public void init() {
		System.out.println("\nAnimal created.\n");
	}

	public Wisdom getWisdom() {
		return wisdom != null ? wisdom : null;
	}

	public Chimp getChimp() {
		return chimp;
	}
}

class Human extends Animal implements Wisdom {
	public String name;

	public Human(String name) {
		this.name = name;
	}

	@Override
	public void askQuestions() {
		System.out.println(name + ": What is the meaning of life?");
	}

	@Override
	public void beCurious() {
		System.out.println(name + ": What does this button do?!");
	}
}

class Chimp extends Animal {
	public String name;
	private HumanNature humanNature;

	public Chimp(HumanNature humanNature) {
		this.humanNature = humanNature;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HumanNature getHumanNature() {
		return humanNature != null ? humanNature : null;
	}
}

interface Wisdom extends HumanNature {
	public void askQuestions();

	default public void seekKnowledge(String name) {
		System.out.println(name + ": Can a matchbox? No! But a tin can!");
	}

	default public void walkAround(String name) {
		System.out.println(name + ": The natural inclination of all creatures:\n" //
				+ "\tJust walking around.. kinda' checking stuff out.");
	}
}

interface HumanNature {

	public void beCurious();

	default public void sleepLate(String name) {
		System.out.println(name + ": Five more minutes, mom!");
	}

	default public void dislikeVeggies(String name) {
		System.out.println(name + ": No broccoli please!");
	}
}