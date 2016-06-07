package bitsAndPieces;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 
 * @author catalin.podariu@gmail.com
 */
public class Fibonacci {

	private Future<Long> result;

	public static void main(String[] args) {
		int numberToCalculate = 47;
		new Fibonacci().launch(numberToCalculate);
	}

	public void launch(int givenNumber) {

		try (ACExecutorService executor = ACExecutorService.newSingleThread();) {
			for (int currentNumber = 1; currentNumber < givenNumber; currentNumber++) {
				// assign the 'promise of a result' to a future object
				result = (Future<Long>) executor.submit(new Nacci(currentNumber));
				if (currentNumber == 1) {
					System.out.println("## Calculation started! ##");
				}
				long timePassed = System.currentTimeMillis();
				/*
				 * Up to this point everything returns immediately, then waits
				 * for the future "result" to finish and prints the number.
				 */
				loopAndWaitForFeedbackOrResult(timePassed, currentNumber);
			}
		} // using a custom class that implements AutoCloseable and ExecutorService

	}

	private void loopAndWaitForFeedbackOrResult(long timePassed, int currentNumber) {
		while (true) {
			if (result.isDone()) {
				// if done, get and display result
				getResult(result, timePassed, currentNumber);
				break;
			} else {
				try {
					// feedback that it's still working..
					System.out.print(".");
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					System.err.println(ex);
				}
			}
		}
	}

	private void getResult(Future<Long> result, long timePassed, int currentNumber) {
		try {
			// get the 'time' again and subtract the timePassed from it
			long now = System.currentTimeMillis();
			long duration = (now - timePassed) / 1000;

			System.out.println("\nThe result for " + currentNumber + " is: " //
					+ result.get().toString() + "\nCalculation took: " //
					+ duration + " seconds.");
		} catch (InterruptedException | ExecutionException ex) {
			System.err.println(ex);
		}
	}
}

class Nacci implements Callable<Long> {

	private int number;

	public Nacci(int number) {
		this.number = number;
	}

	@Override
	public Long call() {
		// the 'result' is assigned here
		return sequence(number);
	}

	private long sequence(int num) {
		if (num <= 1) {
			return num;
		} else {
			// the essence of all this code
			return sequence(num - 1) + sequence(num - 2);
		}
	}
}