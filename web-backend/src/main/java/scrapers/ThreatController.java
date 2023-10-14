package scrapers;

import scrapers.Awd;

public class ThreatController {
    private Scraper[] scrapers;
    private Thread[] threads;
    public void runScrapers() {
        scrapers = new Scraper[]{new Awd()};
        threads = new Thread[scrapers.length];

        // Start each scraper in its own thread
        for (int i = 0; i < scrapers.length; i++) {
            threads[i] = new Thread(scrapers[i]);
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void stopScrapers() {
        for (Scraper scraper : scrapers) {
                scraper.stop();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}


