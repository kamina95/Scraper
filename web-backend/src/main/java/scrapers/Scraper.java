package scrapers;

import org.hibernate.Session;
import webBackend.Brand;
import webBackend.Comparison;
import webBackend.GraphicCard;
import webBackend.GraphicDAO;

import java.util.List;

/**
 * Abstract class representing a web scraper for graphic cards.
 */
public abstract class Scraper implements Runnable {

    /**
     * Data Access Object for interacting with the database.
     */
    public GraphicDAO graphicDAO;

    /**
     * Flag to indicate whether the scraping process should stop.
     */
    private volatile boolean stop = false;

    /**
     * Counter to keep track of the number of graphic cards scraped.
     */
    public static int count = 0;

    /**
     * Implementation of the Runnable interface to initiate the scraping process.
     */
    public void run() {
        if (stop) return;
        scrapeAll();
        System.out.println(count);
    }

    /**
     * Method to stop the scraping process.
     */
    public void stop() {
        stop = true;
    }

    /**
     * Abstract method to be implemented by subclasses for scraping graphic card information.
     */
    public abstract void scrapeAll();

    /**
     * Sets the Hibernate GraphicDAO for database interactions.
     *
     * @param graphicDAO The GraphicDAO object to be set.
     */
    public void setHibernate(GraphicDAO graphicDAO) {
        this.graphicDAO = graphicDAO;
    }

    /**
     * Creates and populates GraphicCard, Brand, and Comparison objects and saves them to the database.
     *
     * @param model       The model of the graphic card.
     * @param brandName   The brand name of the graphic card.
     * @param url         The URL of the graphic card.
     * @param imgUrl      The URL of the graphic card image.
     * @param price       The price of the graphic card.
     * @param description The description of the graphic card.
     */
    public void createClasses(String model, String brandName, String url, String imgUrl, Double price, String description) {
        model = model.replace("NVIDIA", "").replace("RTX", "").replace("GeForce", "").replace("Nvidia", "").trim();
        GraphicCard graphicCard = new GraphicCard();
        graphicCard.setModel(model);
        graphicCard.setDescription(description);

        Brand brand = new Brand();
        brand.setBrand(brandName);
        brand.setImg_url(imgUrl);

        Comparison comparison = new Comparison();
        comparison.setUrl(url);
        comparison.setPrice(price);
        createTables(graphicCard, brand, comparison);
    }

    /**
     * Checks if the required fields are not empty and saves the objects to the database.
     *
     * @param graphicCard The GraphicCard object.
     * @param brand       The Brand object.
     * @param comparison  The Comparison object.
     */
    public void createTables(GraphicCard graphicCard, Brand brand, Comparison comparison) {
        if (!brand.getBrand().isEmpty() && !graphicCard.getModel().isEmpty() && !graphicCard.getDescription().isEmpty()
                && !brand.getImg_url().isEmpty() && comparison.getPrice() != -1 && !comparison.getUrl().isEmpty()
                && !graphicCard.getDescription().isEmpty()) {
            brand.setGraphicCard(graphicCard);
            comparison.setBrand(brand);
            graphicDAO.saveAndMerge(comparison);
            count++;
        }
    }
}
