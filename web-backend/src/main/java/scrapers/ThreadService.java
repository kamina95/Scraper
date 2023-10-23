package scrapers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import webBackend.GraphicDAO;

@Configuration
public class ThreadService {

    @Bean
    public Awd myAwd(){
        Awd awd = new Awd();
        awd.setHibernate(myHibernateSession());
        return awd;
    }

    @Bean
    public NovatechScraper myNovatech(){
        NovatechScraper novatechScraper = new NovatechScraper();
        novatechScraper.setHibernate(myHibernateSession());
        return novatechScraper;
    }

    @Bean
    public AmazonScraper myAmazonScraper(){
        AmazonScraper amazonScraper = new AmazonScraper();
        amazonScraper.setHibernate(myHibernateSession());
        return amazonScraper;
    }


    @Bean
    public ThreatController myThreatController(){
        ThreatController threatController = new ThreatController();
        Scraper[] scrapers = {myAwd(), myNovatech(), myAmazonScraper()};
        threatController.setScrapers(scrapers);
        return threatController;
    }

    @Bean
    public GraphicDAO myHibernateSession(){
        GraphicDAO graphicDAO = new GraphicDAO();
        graphicDAO.init();
        return graphicDAO;
    }
}
