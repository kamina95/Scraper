package scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import webBackend.GraphicCardsAnnotation;
import webBackend.GraphicDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Awd implements Scraper {

    private volatile boolean stop = false;

    public void run() {
        if (stop) return;
        scrapeAll();
        System.out.println(count);
    }

    public void stop() {
        stop = true;
    }

    //GraphicDAO graphicDAO = new GraphicDAO();
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
//        graphicDAO.init();
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
                Elements tables = doc.select(".specs-table");
                Elements tableFeature = tables.select(".custom-table");

                extractImg(doc);
                for (Element productFeature : tableFeature) {
                    Elements features = productFeature.select(".table-value");
                    createObject(features, pageUrl, imgUrl, price);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    int count = 0;


    public void createObject(Elements productFeatures, String url, String imgUrl, Double price) {
        GraphicCardsAnnotation graphicCardsAnnotation = new GraphicCardsAnnotation();

        graphicCardsAnnotation.setBrand(productFeatures.select("[data-th*=Brand]").text().trim());
        graphicCardsAnnotation.setModel(productFeatures.select("[data-th*=Graphics Card Model]").text().trim());
        graphicCardsAnnotation.setUrl(url);
        graphicCardsAnnotation.setImgUrl(imgUrl);
        graphicCardsAnnotation.setPrice(price);

        System.out.println(graphicCardsAnnotation);
        if(graphicCardsAnnotation.getBrand().isEmpty() || graphicCardsAnnotation.getModel().isEmpty() || graphicCardsAnnotation.getPrice() == -1 || graphicCardsAnnotation.getImgUrl().isEmpty() || graphicCardsAnnotation.getUrl().isEmpty()) {

        }else{
            count++;
        }

//        if(!graphicCardsAnnotation.getBrand().isEmpty() && !graphicCardsAnnotation.getModel().isEmpty() && !graphicCardsAnnotation.getMemorySize().isEmpty() && !graphicCardsAnnotation.getDisplays().isEmpty()  && !graphicCardsAnnotation.getResolution().isEmpty()) {
//            graphicDAO.addGraphicCard(graphicCardsAnnotation);
//        }
    }
}
