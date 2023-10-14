package webBackend;

import scrapers.Awd;
import scrapers.ThreatController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        ThreatController threatController = new ThreatController();
        threatController.runScrapers();
//
//        Awd awd = new Awd();
//        awd.scrapeAll();
    }
}
