package bitsAndPieces;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Simple IP Scanner, just for 192.168.[your mask].*
 *
 * @author catalin.podariu@gmail.com
 */
public class IPScanner {

	private String[] hosts;
	private ArrayList<String> aliveServers;
	private ArrayList<Future<String>> results;

	private final int INTERRUPTED_ACTION = -3;
	private final int LARGE_NUMBER_INPUT = -2;
	private final int NEGATIVE_NUMBER_INPUT = -1;

	private final int DEFAULT_THREADS_NUMBER = 50;
	private final int MAX_THREADS_NUMBER = 64;

	private IPScanner() {
	}

	public static IPScanner valueOf() {
		return new IPScanner();
	}

	public String[] init(int pingTimeout) {
		String temp = "";
		String[] localhost = null;
		try {
			temp = InetAddress.getLocalHost().toString();
			// split the IP address into individual octets
			localhost = temp.substring(temp.indexOf("/") + 1).split("[^\\d]");
		} catch (UnknownHostException ex) {
			System.err.println(ex);
		}
		// the 3rd octet
		int status = scan(Integer.valueOf(localhost[2]), pingTimeout, DEFAULT_THREADS_NUMBER);
		if (status != INTERRUPTED_ACTION) {
			if (aliveServers.isEmpty()) {
				hosts = new String[1];
				hosts[0] = "No alive servers..";
			} else {
				hosts = new String[aliveServers.size()];
				for (String ipNumber : aliveServers) {
					hosts[0] = temp.substring(0, temp.indexOf("/"));
					hosts[1] = ipNumber;
					hosts[2] = "n/a";
				}
			}
		} else {
			hosts[0] = "Scan error..";
		}
		return hosts;
	}

	public int scan(int ipMask, int timeout, int nThreads) {
		if (ipMask > 254) {
			return NEGATIVE_NUMBER_INPUT;
		}
		if (ipMask < 0) {
			return LARGE_NUMBER_INPUT;
		}
		aliveServers = new ArrayList<>();

		if (nThreads > MAX_THREADS_NUMBER) {
			nThreads = MAX_THREADS_NUMBER;
		}
		try (ACExecutorService exec = ACExecutorService.newFixedPool(nThreads)) {
			results = new ArrayList<>();
			Future<String> status;
			// create an ArrayList of Future objects
			for (int i = 0; i < 255; i++) {
				status = exec.submit(new Scanner("192.168." + ipMask + "." + i, timeout));
				results.add(status);
			}
		}
		return queryResultsForStatus();
	}

	private int queryResultsForStatus() {
		for (Future<String> item : results) {
			try {
				if (item.get().toString().endsWith("alive")) {
					/*
					 * if the item is alive, add to aliveServers strip the
					 * " is alive" from the end of String
					 */
					aliveServers.add(item.get().toString().substring(0, item.get().toString().indexOf(" ")));
				}
			} catch (InterruptedException | ExecutionException ex) {
				System.err.println(ex);
				return INTERRUPTED_ACTION;
			}
		}
		return 0;
	}

	class Scanner implements Callable<String> {

		private String ipAsString;
		private int timeout;

		public Scanner(String ipAsString, int timeout) {
			this.ipAsString = ipAsString;
			this.timeout = timeout;
		}

		@Override
		public String call() {
			try {
				return returnAliveOrDeadStatus();
			} catch (SocketTimeoutException ex) {
				System.err.println("Timed out: " + ipAsString);
				return "Timed out: " + ipAsString;
			} catch (IOException ex) {
				System.out.println(ex);
				return ex.toString();
			}
		}

		private String returnAliveOrDeadStatus() throws UnknownHostException, IOException {

			InetAddress inet = InetAddress.getByName(ipAsString);
			// System.out.println("Ping request to IP: " + ipAsString);
			if (inet.isReachable(timeout)) {
				return ipAsString + " is alive";
			} else {
				return ipAsString + " is dead";
			}
		}
	}
}
