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
    public OverclockersScraper myOverclockersScraper(){
        OverclockersScraper overclockersScraper = new OverclockersScraper();
        overclockersScraper.setHibernate(myHibernateSession());
        return overclockersScraper;
    }

    @Bean
    public EbuyerScraper myEbuyerScraper(){
        EbuyerScraper ebuyerScraper = new EbuyerScraper();
        ebuyerScraper.setHibernate(myHibernateSession());
        return ebuyerScraper;
    }

    @Bean
    public ThreatController myThreatController(){
        ThreatController threatController = new ThreatController();
        Scraper[] scrapers = {myEbuyerScraper(), myAwd(), myNovatech(), myAmazonScraper(), myOverclockersScraper()};
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
