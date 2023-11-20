package webBackend;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import scrapers.AmazonScraper;
import scrapers.ThreadService;
import scrapers.ThreatController;

public class App {
    public static void main( String[] args ) {
//        ApplicationContext context = new AnnotationConfigApplicationContext(ThreadService.class);
//        ThreatController threatController = context.getBean(ThreatController.class);
//        threatController.runScrapers();
        AmazonScraper amazonScraper = new AmazonScraper();
        amazonScraper.scrapeAll();
    }
}
