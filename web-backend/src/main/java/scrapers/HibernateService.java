package scrapers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import webBackend.GraphicDAO;

@Configuration
public class HibernateService {
    @Bean
    public GraphicDAO myHibernateSession(){
        GraphicDAO graphicDAO = new GraphicDAO();
        graphicDAO.init();
        return graphicDAO;
    }
}
