package scrapers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Awd myAwd(){
        Awd awd = new Awd();
        return awd;
    }

    @Bean
    public ThreatController myThreatController(){
        ThreatController threatController = new ThreatController();
        Scraper[] scrapers = {new Awd()};
        threatController.setScrapers(scrapers);
        return threatController;
    }
}
