package scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.logging.Logger;

public class AwdScraper extends Scraper {

    // Logger for logging messages
    private static final Logger logger = Logger.getLogger(AwdScraper.class.getName());

    // Method to return all products from a given page URL
    public Elements returnAllProducts(String pageUrl) {
        Document doc = null;
        try {
            // Download HTML document from website using Jsoup
            doc = Jsoup.connect(pageUrl).get();
        } catch (Exception ex) {
            // Log a warning if there's an exception while fetching the page
            logger.warning("Awd scraper not able to extract documentation from the page, the page will not be scraped \n" + ex.getMessage());
        }
        assert doc != null;
        return doc.select(".product-item-info");
    }

    // Method to scrape information from all pages
    public void scrapeAll() {
        Elements productUrlList = new Elements();
        for (int i = 1; i <= 8; i++) { // Scraping 8 pages
            Elements productsList = returnAllProducts("https://www.awd-it.co.uk/components/graphics-cards/nvidia.html?p=" + i);
            productUrlList.addAll(productsList);
        }
        extractFeatureTable(productUrlList);
        System.out.println("product size: " + productUrlList.size());
    }

    // Method to extract feature tables from product URLs
    public void extractFeatureTable(Elements products) {
        for (Element product : products) {
            // Extracting page URL, image URL, and price information
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
                // Fetching the HTML document from the product URL
                doc = Jsoup.connect(pageUrl).get();
                // Extracting product description and feature tables
                Element descriptionElement = doc.select(".attr-value > p").first();
                String description = "";
                if (descriptionElement != null) {
                    description = descriptionElement.text();
                }
                Elements tables = doc.select(".specs-table");
                Elements tableFeature = tables.select(".custom-table");
                for (Element productFeature : tableFeature) {
                    // Extracting individual features and calling the method to create classes
                    Elements features = productFeature.select(".table-value");
                    extractData(features, pageUrl, imgUrl, price, description);
                }
            } catch (IOException e) {
                // Logging info if an element from Awd scraper is not found
                logger.info("Element from Awd scraper not found");
            }
        }
    }

    // Method to extract data (model and brand) from product features
    public void extractData(Elements productFeatures, String url, String imgUrl, Double price, String description) {
        String model = productFeatures.select("[data-th*=Graphics Card Model]").text().trim();
        String brand = productFeatures.select("[data-th*=Brand]").text().trim();
        // Calling the method to create classes with the extracted information
        createClasses(model, brand, url, imgUrl, price, description);
    }
}
