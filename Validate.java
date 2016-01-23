package Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validate an IP address, or a port number or a user/server name.
 *
 * @author catalin.podariu@gmail.com
 */
public class Validate {

    private Pattern ipPattern;
    private Matcher matchIP;

    private Pattern serverPattern;
    private Matcher matchServer;

    private Pattern portPattern;
    private Matcher matchPort;

    private Validate() {
    }

    public static Validate valueOf() {
        return new Validate();
    }

    private static final String ipAddress
            = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static final String serverName
            = "[-_a-zA-Z0-9]+{4,10}";

    private static final String portNumber
            = "[0-9]+{4}";

    /**
     * Validate IP address with Regex.
     * <br>
     * 1.1.1.1 or 255.255.255.255 or 20.1.198.12 etc.
     *
     * @param ipAddress ip address for validation
     * @return true valid ip address, false invalid ip address
     */
    public synchronized boolean validateIP(final String ipAddress) {
        ipPattern = Pattern.compile(Validate.ipAddress);
        matchIP = ipPattern.matcher(ipAddress);
        return matchIP.matches();
    }

    /**
     * Validate serverName with Regex.
     * <br>
     * Name must be at least 4 and max. 10 chars, alphanumerics + '-' and '_'
     *
     * @param serverName name of the server for validation
     * @return true valid name, false invalid name
     */
    public synchronized boolean validateServerName(final String serverName) {
        serverPattern = Pattern.compile(Validate.serverName);
        matchServer = serverPattern.matcher(serverName);
        return matchServer.matches();
    }

    /**
     * Validate portNumber with Regex.
     * <br>
     * Maximum 4 numbers between 0 and 9.
     *
     * @param portNumber name of the server for validation
     * @return true valid name, false invalid name
     */
    public synchronized boolean validatePortNumber(final String portNumber) {
        portPattern = Pattern.compile(Validate.portNumber);
        matchPort = portPattern.matcher(portNumber);
        return matchPort.matches();
    }
}
