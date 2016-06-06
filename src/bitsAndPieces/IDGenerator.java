package bitsAndPieces;

import java.util.Random;

/**
 *
 * @author catalin.podariu@gmail.com
 */
public class IDGenerator {

    private IDGenerator() {

    }

    public static synchronized IDGenerator valueOf() {
        return new IDGenerator();
    }

    /**
     * Choose a random ID.
     *
     * @return new ID
     */
    public String newID() {
        Random random = new Random();
        // number suffix to be safe
        int idSuffix = random.nextInt(
                primes[random.nextInt(primes.length - 1)]) * 17;

        int id = random.nextInt(ids.length - 1);
        // random name choosen
        return ids[id] + idSuffix;
    }

    private String[] ids = {
        "Audrey", "Hepburn", "Ava", "Gardner", "Bette", "Davis", "Brigitte",
        "Bardot", "Buster", "Keaton", "Cary", "Grant", "Charlie", "Chaplin",
        "Charlton", "Heston", "Clara", "Bow", "Clark", "Gable", "Claudette",
        "Colbert", "Cyd", "Charisse", "Elizabeth", "Taylor", "Errol", "Flynn",
        "Frank", "Sinatra", "Fred", "Astaire", "Gene", "Kelly", "Gene",
        "Tierney", "Ginger", "Rogers", "Grace", "Kelly", "Greer", "Garson",
        "Gregory", "Peck", "Greta", "Garbo", "Humphrey", "Bogart", "Ingrid",
        "Bergman", "James", "Cagney", "James", "Dean", "Jean", "Harlow",
        "Jimmy", "Stewart", "Joan", "Crawford", "John", "Wayne", "Judy",
        "Garland", "Katharine", "Hepburn", "Lana", "Turner", "Lauren", "Bacall",
        "Lillian", "Gish", "Louise", "Brooks", "Mae", "West", "Marilyn",
        "Monroe", "Marlene", "Dietrich", "Marlon", "Brando", "Mickey", "Rooney",
        "Olivia", "Havilland", "Richard", "Burton", "Rita", "Hayworth",
        "Spencer", "Tracey", "Tyrone", "Power", "Veronica", "Lake", "Vivien",
        "Leigh", "Yul", "Brynner"};

    private int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43,
        47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};
}
