package scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import webBackend.Brand;
import webBackend.Comparison;
import webBackend.GraphicCard;
import webBackend.GraphicDAO;

import java.io.IOException;

public class Awd implements Scraper {

    private volatile boolean stop = false;
    GraphicDAO graphicDAO = new GraphicDAO();

    public void run() {
        if (stop) return;
        scrapeAll();
        System.out.println(count);
    }

    public void stop() {
        stop = true;
    }


    public Elements returnAllProducts(String pageUrl) {
        Document doc = null;
        try {
            //Download HTML document from website
            doc = Jsoup.connect(pageUrl).get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assert doc != null;
        return doc.select(".product-item-info");
    }

    public void scrapeAll() {
        graphicDAO.init();
        Elements productUrlList = new Elements();
        for (int i = 1; i < 8; i++) { //8
            Elements productsList = returnAllProducts("https://www.awd-it.co.uk/components/graphics-cards/nvidia.html?p=" + i);
            productUrlList.addAll(productsList);
        }
        extractFeatureTable(productUrlList);
        System.out.println("product size: " + productUrlList.size());
    }

    public void extractImg(Document doc) {
        Elements img = doc.select(".fotorama_img");
        System.out.println(img.toString());
    }

    public void extractFeatureTable(Elements products) {


        for (Element product : products) {
            String pageUrl = product.select("a").attr("href");
            String imgUrl = product.select(".product-image-photo").attr("src");
            Element priceSpan = product.select(".price-box span.price-wrapper.price-including-tax span.price").first();

            double price = -1;
            if (priceSpan != null) {
                String priceText = priceSpan.text().replace("£", "").replace(",", "");
                price = Double.parseDouble(priceText);
            }

            Document doc = null;
            try {
                doc = Jsoup.connect(pageUrl).get();
                Element descriptionElement = doc.select(".attr-value > p").first();
                String description = "";
                if(descriptionElement != null) {
                    description = descriptionElement.text();
                }
                Elements tables = doc.select(".specs-table");
                Elements tableFeature = tables.select(".custom-table");

                extractImg(doc);
                for (Element productFeature : tableFeature) {
                    Elements features = productFeature.select(".table-value");
                    createClasses(features, pageUrl, imgUrl, price, description);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    int count = 0;

    public void createClasses(Elements productFeatures, String url, String imgUrl, Double price, String description) {
        GraphicCard graphicCard = new GraphicCard();
        graphicCard.setModel(productFeatures.select("[data-th*=Graphics Card Model]").text().trim());
        graphicCard.setDescription(description);

        Brand brand = new Brand();
        brand.setBrand(productFeatures.select("[data-th*=Brand]").text().trim());
        brand.setImg_url(imgUrl);

        Comparison comparison = new Comparison();
        comparison.setUrl(url);
        comparison.setPrice(price);
        createTables(graphicCard, brand, comparison);
    }

    public void createTables(GraphicCard graphicCard, Brand brand, Comparison comparison){
        if (!brand.getBrand().isEmpty() && !graphicCard.getModel().isEmpty() && !graphicCard.getDescription().isEmpty()
                && !brand.getImg_url().isEmpty() && comparison.getPrice() != -1 && !comparison.getUrl().isEmpty()
                && !graphicCard.getDescription().isEmpty()) {
            graphicDAO.addGraphicCard(graphicCard);
            brand.setId_product(graphicCard);
            graphicDAO.addBrand(brand);
            comparison.setIdCards(brand);
            graphicDAO.addComparison(comparison);
            count++;
            //System.out.println(graphicCard.getDescription());
        }
    }

//    public void createObject(Elements productFeatures, String url, String imgUrl, Double price, String description) {
//
//        GraphicCard graphicCard = new GraphicCard();
//        graphicCard.setModel(productFeatures.select("[data-th*=Graphics Card Model]").text().trim());
//        graphicCard.setDescription(description);
//
//        Brand brand = new Brand();
//        brand.setBrand(productFeatures.select("[data-th*=Brand]").text().trim());
//        brand.setImg_url(imgUrl);
//
//        Comparison comparison = new Comparison();
//        comparison.setUrl(url);
//        comparison.setPrice(price);
//
//        if (!brand.getBrand().isEmpty() && !graphicCard.getModel().isEmpty() && !graphicCard.getDescription().isEmpty()
//                && !brand.getImg_url().isEmpty() && comparison.getPrice() != -1 && !comparison.getUrl().isEmpty()
//                && !graphicCard.getDescription().isEmpty()) {
//            graphicDAO.addGraphicCard(graphicCard);
//            brand.setId_product(graphicCard);
//            graphicDAO.addBrand(brand);
//            comparison.setIdCards(brand);
//            graphicDAO.addComparison(comparison);
//            count++;
//            System.out.println(graphicCard.getDescription());
//        }
//        System.out.println(count);
//    }
}

//        if(graphicCard.getBrand().isEmpty() || graphicCard.getModel().isEmpty() || graphicCard.getPrice() == -1 || graphicCard.getImgUrl().isEmpty() || graphicCard.getUrl().isEmpty()) {
//
//        }else{
//            count++;
//        }

