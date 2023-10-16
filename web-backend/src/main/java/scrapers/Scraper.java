package scrapers;

import webBackend.Brand;
import webBackend.Comparison;
import webBackend.GraphicCard;

public interface Scraper extends Runnable{
    public void run();
    public void stop();
    public void createTables(GraphicCard graphicCard, Brand brand, Comparison comparison);

}