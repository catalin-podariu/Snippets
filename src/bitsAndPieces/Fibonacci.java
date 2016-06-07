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
			for (int fib = 1; fib < givenNumber; fib++) {
				result = (Future<Long>) executor.submit(new Nacci(fib));
				if (fib == 1) {
					System.out.println("## Calculation started! ##");
				}
				long timePassed = System.currentTimeMillis();
				while (true) {
					if (result.isDone()) {
						getResult(result, timePassed);
						break;
					} else {
						waitOneSecond();
					}
				}
			}
		}

	}

	private void getResult(Future<Long> result, long timePassed) {
		try {
			long now = System.currentTimeMillis();
			double duration = (now - timePassed) / 1000;

			System.out.println("\nThe result is: " //
					+ result.get().toString() + "\nCalculation took: " //
					+ duration + " seconds.");
		} catch (InterruptedException | ExecutionException ex) {
			System.err.println(ex);
		}
	}

	private void waitOneSecond() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
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