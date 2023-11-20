package scrapers;

/**
 * Controller class for managing multiple Scrapers concurrently.
 */
public class ThreatController {

    // Array to store Scraper instances
    private Scraper[] scrapers;

    /**
     * Sets the array of Scrapers.
     *
     * @param scrapers The array of Scrapers to be set.
     */
    public void setScrapers(Scraper[] scrapers) {
        this.scrapers = scrapers;
    }

    /**
     * Gets the array of Scrapers.
     *
     * @return The array of Scrapers.
     */
    public Scraper[] getScrapers() {
        return scrapers;
    }

    /**
     * Creates and starts threads for each Scraper in the array.
     *
     * @return An array of started threads.
     */
    public Thread[] getThreads() {
        Thread[] threads = new Thread[scrapers.length];

        for (int i = 0; i < scrapers.length; i++) {
            threads[i] = new Thread(scrapers[i]);
            threads[i].start();
        }
        return threads;
    }

    /**
     * Runs all Scrapers concurrently by creating and starting threads.
     */
    public void runScrapers() {
        Thread[] threads = getThreads();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Stops all Scrapers by calling the stop method and waiting for threads to finish.
     */
    public void stopScrapers() {
        // Stop all Scrapers
        for (Scraper scraper : scrapers) {
            scraper.stop();
        }

        // Create and start new threads for stopped Scrapers
        Thread[] threads = getThreads();

        // Wait for the newly started threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
