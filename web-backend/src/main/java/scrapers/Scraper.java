package scrapers;

import webBackend.Brand;
import webBackend.Comparison;
import webBackend.GraphicCard;

public abstract class Scraper implements Runnable{
    public abstract void run();
    public abstract void stop();
    public abstract void createTables(GraphicCard graphicCard, Brand brand, Comparison comparison);

//    public void createClasses(String model, String brandName, String url, String imgUrl, Double price, String description) {
//        GraphicCard graphicCard = new GraphicCard();
//        graphicCard.setModel(model);
//        graphicCard.setDescription(description);
//
//        Brand brand = new Brand();
//        brand.setBrand(brandName);
//        brand.setImg_url(imgUrl);
//
//        Comparison comparison = new Comparison();
//        comparison.setUrl(url);
//        comparison.setPrice(price);
//        createTables(graphicCard, brand, comparison);
//    }
//
//    public void createTables(GraphicCard graphicCard, Brand brand, Comparison comparison){
//        if (!brand.getBrand().isEmpty() && !graphicCard.getModel().isEmpty() && !graphicCard.getDescription().isEmpty()
//                && !brand.getImg_url().isEmpty() && comparison.getPrice() != -1 && !comparison.getUrl().isEmpty()
//                && !graphicCard.getDescription().isEmpty()) {
//            graphicDAO.addGraphicCard(graphicCard);
//            brand.setId_product(graphicCard);
//            graphicDAO.addBrand(brand);
//            comparison.setIdCards(brand);
//            graphicDAO.addComparison(comparison);
//            count++;
//        }
//    }

}