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

public class Awd extends Scraper {

    private volatile boolean stop = false;
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
        Elements productUrlList = new Elements();
        for (int i = 1; i <= 8; i++) { //8
            Elements productsList = returnAllProducts("https://www.awd-it.co.uk/components/graphics-cards/nvidia.html?p=" + i);
            productUrlList.addAll(productsList);
        }
        extractFeatureTable(productUrlList);
        System.out.println("product size: " + productUrlList.size());
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
                if (descriptionElement != null) {
                    description = descriptionElement.text();
                }
                Elements tables = doc.select(".specs-table");
                Elements tableFeature = tables.select(".custom-table");
                for (Element productFeature : tableFeature) {
                    Elements features = productFeature.select(".table-value");
                    extractData(features, pageUrl, imgUrl, price, description);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void extractData(Elements productFeatures, String url, String imgUrl, Double price, String description) {
        String model = productFeatures.select("[data-th*=Graphics Card Model]").text().trim();
        String brand = productFeatures.select("[data-th*=Brand]").text().trim();
        createClasses(model, brand, url, imgUrl, price, description);
    }

}
