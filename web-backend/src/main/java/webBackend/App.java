package webBackend;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import scrapers.AppConfig;
import scrapers.ThreatController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ThreatController threatController = context.getBean(ThreatController.class);
        threatController.runScrapers();
        //ThreatController threatController = new ThreatController();
        //threatController.runScrapers();
//
//        Awd awd = new Awd();
//        awd.scrapeAll();
    }
}
