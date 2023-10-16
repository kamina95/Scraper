package webBackend;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import scrapers.ThreadService;
import scrapers.ThreatController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ThreadService.class);
        ThreatController threatController = context.getBean(ThreatController.class);
        threatController.runScrapers();
        //ThreatController threatController = new ThreatController();
        //threatController.runScrapers();
//
//        Awd awd = new Awd();
//        awd.scrapeAll();
    }
}
