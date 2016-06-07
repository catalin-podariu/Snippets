package bitsAndPieces;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * @author catalin.podariu@gmail.com
 */
public class Fibonacci {

	public static void main(String[] args) {
		int numberToCalculate = 48;
		new Fibonacci().launch(numberToCalculate);
	}

	public void launch(int givenNumber) {
		try (ACExecutorService executor = ACExecutorService.newSingleThread();) {
			Future<Long> result = (Future<Long>) executor.submit(new Nacci(givenNumber));
			System.out.println("## Calculation started! ##");
			int secondsPassed = 0;
			while (true) {
				if (result.isDone()) {
					getResult(result, secondsPassed);
					break;
				} else {
					secondsPassed = feedback(secondsPassed);
				}
			}
		}

	}

	private void getResult(Future<Long> result, int secondsPassed) {
		try {
			System.out.println("\nThe result is: " //
					+ result.get().toString() + "\nCalculation took: " //
					+ secondsPassed + " seconds.");
		} catch (InterruptedException | ExecutionException ex) {
			System.err.println(ex);
		}
	}

	private int feedback(int secondsPassed) {
		try {
			if (secondsPassed == 0) {
				System.out.print("Waiting");
				secondsPassed++;
			} else if (secondsPassed > 0) {
				System.out.print(".");
				secondsPassed++;
				if (secondsPassed % 10 == 0) {
					System.out.print("\n" + secondsPassed //
							+ " seconds have passed. \nStill waiting...");
				}
			}
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			System.err.println(ex);
		}
		return secondsPassed;
	}
}

class Nacci implements Callable<Long> {

	private int number;

	public Nacci(int number) {
		this.number = number;
	}

	@Override
	public Long call() {
		return sequence(number);
	}

	private long sequence(int num) {
		if (num <= 1) {
			return num;
		} else {
			// the essence of all this
			return sequence(num - 1) + sequence(num - 2);
		}
	}
}