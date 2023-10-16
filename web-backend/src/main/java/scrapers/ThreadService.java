package scrapers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadService {

    @Bean
    public Awd myAwd(){
        Awd awd = new Awd();
        return awd;
    }

    @Bean
    public ThreatController myThreatController(){
        ThreatController threatController = new ThreatController();
        Scraper[] scrapers = {myAwd()};
        threatController.setScrapers(scrapers);
        return threatController;
    }
}
