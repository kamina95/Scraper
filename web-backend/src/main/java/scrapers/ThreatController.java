package scrapers;

public class ThreatController {

    private Scraper[] scrapers;
    //private Thread[] threads;

    public void setScrapers(Scraper[] scrapers) {
        this.scrapers = scrapers;
    }

    public Scraper[] getScrapers() {
        return scrapers;
    }

    public Thread[] getThreads() {
        Thread[] threads = new Thread[scrapers.length];

        for (int i = 0; i < scrapers.length; i++) {
            threads[i] = new Thread(scrapers[i]);
            threads[i].start();
        }
        return threads;
    }

    public void runScrapers() {
        scrapers = new Scraper[]{new Awd()};
        Thread[] threads = new Thread[scrapers.length];

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

        Thread[] threads = new Thread[scrapers.length];

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
}


