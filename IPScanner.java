package Utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Simple IP Scanner, just searches 192.168.[your mask].*
 *
 * @author catalin.podariu@gmail.com
 */
public class IPScanner {

    private IPScanner() {
    }

    public static IPScanner valueOf() {
        return new IPScanner();
    }

    public String[] backupScan(int pingTimeout) {
        String temp = "";
        String[] localhost = null;
        try {
            // get local IP address 
            temp = InetAddress.getLocalHost().toString();
            // split the IP address into individual octets
            localhost = temp.substring(temp.indexOf("/") + 1).split("[^\\d]");
        } catch (UnknownHostException ex) {
            System.err.println(ex);
        }
        // the 3rd octet of the IP address
        int status = scanner(Integer.valueOf(localhost[2]), pingTimeout, 50);
        // if there thread is not interrupted 
        if (status != -3) {
            if (aliveServers.isEmpty()) {
                hosts = new String[1];
                hosts[0] = "No alive servers..";
            } else {
                hosts = new String[aliveServers.size()];
                for (String aliveIPAddress : aliveServers) {
                    hosts[0] = temp.substring(0, temp.indexOf("/"));
                    hosts[1] = aliveIPAddress;
                    hosts[2] = "n/a";
                }
            }
        } else {
            hosts[0] = "Scan error..";
        }
        return hosts;
    }

    /**
     * Simple IP Scanner, just searches 192.168.[your mask].*
     *
     * @param mask gets that from local host IP address
     * @param timeout time-out to wait until connect (in milliseconds).
     * @param nThreads number of threads to run simultaneously. If there are 50
     * threads, it will take ~10 seconds to scan 250 ip addresses
     * @return 0 for OK; -1 & -2 for wrong numbers received (large or negative)
     * and -3 for InterruptedException.
     */
    public int scanner(int mask, int timeout, int nThreads) {
        if (mask > 254) {
            // can't have large numbers.
            return -1;
        }
        if (mask < 0) {
            // can't have negative numbers.
            return -2;
        }
        aliveServers = new ArrayList();
        // launch a maximum of 64 threads at once
        if (nThreads >= 64) {
            nThreads = 64;
        }
        exec = Executors.newFixedThreadPool(nThreads);
        results = new ArrayList();
        Future status;
        // create an ArrayList of Future objects
        for (int i = 0; i < 255; i++) {
            status = exec.submit(
                    new ScannerThread("192.168." + mask + "." + i, timeout));
            results.add(status);
        }
        // cleanup
        exec.shutdown();
        // query every object in the results[] for status
        for (Future item : results) {
            try {
                if (item.get().toString().endsWith("alive")) {
                    /*
                     if the item is alive, add to aliveServers strip 
                     the " is alive" from the end of String
                     */
                    aliveServers.add(item.get().toString().substring(
                            0,
                            item.get().toString().indexOf(" ")
                    ));
                }
            } catch (InterruptedException | ExecutionException ex) {
                System.err.println(ex);
                return -3;
            }
        }
        return 0;
    }

    class ScannerThread implements Callable {

        private String ipAsString;
        private int timeout;

        /**
         * Thread that will test one IP address.
         *
         * @param ipAsString
         * @param timeout
         */
        public ScannerThread(String ipAsString, int timeout) {
            this.ipAsString = ipAsString;
            this.timeout = timeout;
        }

        @Override
        public String call() {
            try {
                InetAddress inet = InetAddress.getByName(ipAsString);
//            System.out.println("Ping request to IP: " + ipAsString);
                if (inet.isReachable(timeout)) {
                    return ipAsString + " is alive";
                } else {
                    return ipAsString + " is dead";
                }
            } catch (SocketTimeoutException ex) {
                System.err.println("Timed out: " + ipAsString);
                return "Timed out: " + ipAsString;
            } catch (IOException ex) {
                System.out.println(ex);
                return ex.toString();
            }
        }
    }

    private String[] hosts;
    private ArrayList<String> aliveServers;
    private ArrayList<Future> results;
    private ExecutorService exec;

}
