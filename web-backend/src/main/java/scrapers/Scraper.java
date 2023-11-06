package scrapers;

import org.hibernate.Session;
import webBackend.Brand;
import webBackend.Comparison;
import webBackend.GraphicCard;
import webBackend.GraphicDAO;

import java.util.List;


public abstract class Scraper implements Runnable{

    public GraphicDAO graphicDAO;
    private volatile boolean stop = false;
    public static int count = 0;
    public void run() {
        if (stop) return;
        scrapeAll();
        System.out.println(count);
    }

    public void stop() {
        stop = true;
    }

    public abstract void scrapeAll();

    public void setHibernate(GraphicDAO graphicDAO){
        this.graphicDAO = graphicDAO;
    }

    public void createClasses(String model, String brandName, String url, String imgUrl, Double price, String description){
        model = model.replace("NVIDIA", "").replace("RTX","").replace("GeForce","").replace("Nvidia","").trim();
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

    public void createTables(GraphicCard graphicCard, Brand brand, Comparison comparison) {
        if (!brand.getBrand().isEmpty() && !graphicCard.getModel().isEmpty() && !graphicCard.getDescription().isEmpty()
                && !brand.getImg_url().isEmpty() && comparison.getPrice() != -1 && !comparison.getUrl().isEmpty()
                && !graphicCard.getDescription().isEmpty()) {
            //graphicDAO.addGraphicCard(graphicCard);
            brand.setGraphicCard(graphicCard);
            //graphicDAO.addBrand(brand);
            comparison.setBrand(brand);
            //graphicDAO.addComparison(comparison);
            graphicDAO.saveAndMerge(comparison);
            count++;
        }
    }

}