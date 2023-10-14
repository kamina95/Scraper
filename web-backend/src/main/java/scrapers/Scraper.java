package scrapers;

import org.jsoup.select.Elements;

public interface Scraper extends Runnable{
    public void run();
    public void stop();

}

