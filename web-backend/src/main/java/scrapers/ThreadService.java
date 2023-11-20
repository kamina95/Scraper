package scrapers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import webBackend.GraphicDAO;

/**
 * Configuration class for defining Spring beans related to web scrapers and data access.
 */
@Configuration
public class ThreadService {

    /**
     * Bean definition for AwdScraper.
     *
     * @return A new instance of AwdScraper with a configured GraphicDAO.
     */
    @Bean
    public AwdScraper myAwd() {
        AwdScraper awdScraper = new AwdScraper();
        awdScraper.setHibernate(myGraphicDAO());
        return awdScraper;
    }

    /**
     * Bean definition for NovatechScraper.
     *
     * @return A new instance of NovatechScraper with a configured GraphicDAO.
     */
    @Bean
    public NovatechScraper myNovatech() {
        NovatechScraper novatechScraper = new NovatechScraper();
        novatechScraper.setHibernate(myGraphicDAO());
        return novatechScraper;
    }

    /**
     * Bean definition for AmazonScraper.
     *
     * @return A new instance of AmazonScraper with a configured GraphicDAO.
     */
    @Bean
    public AmazonScraper myAmazonScraper() {
        AmazonScraper amazonScraper = new AmazonScraper();
        amazonScraper.setHibernate(myGraphicDAO());
        return amazonScraper;
    }

    /**
     * Bean definition for OverclockersScraper.
     *
     * @return A new instance of OverclockersScraper with a configured GraphicDAO.
     */
    @Bean
    public OverclockersScraper myOverclockersScraper() {
        OverclockersScraper overclockersScraper = new OverclockersScraper();
        overclockersScraper.setHibernate(myGraphicDAO());
        return overclockersScraper;
    }

    /**
     * Bean definition for EbuyerScraper.
     *
     * @return A new instance of EbuyerScraper with a configured GraphicDAO.
     */
    @Bean
    public EbuyerScraper myEbuyerScraper() {
        EbuyerScraper ebuyerScraper = new EbuyerScraper();
        ebuyerScraper.setHibernate(myGraphicDAO());
        return ebuyerScraper;
    }

    /**
     * Bean definition for ThreatController.
     *
     * @return A new instance of ThreatController with an array of configured scrapers and a configured GraphicDAO.
     */
    @Bean
    public ThreatController myThreatController() {
        ThreatController threatController = new ThreatController();
        Scraper[] scrapers = {myEbuyerScraper(), myAwd(), myNovatech(), myAmazonScraper(), myOverclockersScraper()};
        threatController.setScrapers(scrapers);
        return threatController;
    }

    /**
     * Bean definition for GraphicDAO.
     *
     * @return A new instance of GraphicDAO with initialization.
     */
    @Bean
    public GraphicDAO myGraphicDAO() {
        GraphicDAO graphicDAO = new GraphicDAO();
        graphicDAO.init();
        return graphicDAO;
    }
}
