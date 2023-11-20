package webBackend;

import org.junit.jupiter.api.Test;
import scrapers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ScraperTest {
    @Test
    public void test_AwdScrape() {
        AwdScraper awdScraper = new AwdScraper();
        GraphicDAO graphicDAO = mock(GraphicDAO.class);
        awdScraper.setHibernate(graphicDAO);
        awdScraper.scrapeAll();
        verify(graphicDAO, atLeast(1)).saveAndMerge(any(Comparison.class));
    }

    @Test
    public void test_AmazonScrape() {
        AmazonScraper amazonScraper = new AmazonScraper();
        GraphicDAO graphicDAO = mock(GraphicDAO.class);
        amazonScraper.setHibernate(graphicDAO);
        amazonScraper.scrapeAll();
        verify(graphicDAO, atLeast(1)).saveAndMerge(any(Comparison.class));
    }

    @Test
    public void test_EbuyerScrape() {
        EbuyerScraper ebuyerScraper = new EbuyerScraper();
        GraphicDAO graphicDAO = mock(GraphicDAO.class);
        ebuyerScraper.setHibernate(graphicDAO);
        ebuyerScraper.scrapeAll();
        verify(graphicDAO, atLeast(1)).saveAndMerge(any(Comparison.class));
    }
    @Test
    public void test_NovaTechScrape() {
        NovatechScraper novatechScraper = new NovatechScraper();
        GraphicDAO graphicDAO = mock(GraphicDAO.class);
        novatechScraper.setHibernate(graphicDAO);
        novatechScraper.scrapeAll();
        verify(graphicDAO, atLeast(1)).saveAndMerge(any(Comparison.class));
    }

    @Test
    public void test_OverClockersScrape() {
        OverclockersScraper overclockersScraper = new OverclockersScraper();
        GraphicDAO graphicDAO = mock(GraphicDAO.class);
        overclockersScraper.setHibernate(graphicDAO);
        overclockersScraper.scrapeAll();
        verify(graphicDAO, atLeast(1)).saveAndMerge(any(Comparison.class));
    }

}
