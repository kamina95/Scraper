package scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.logging.Logger;

public class OverclockersScraper extends Scraper {

    // Logger for logging messages
    private static final Logger logger = Logger.getLogger(OverclockersScraper.class.getName());

    // Method to return all products from a given page URL
    public Elements returnAllProducts(String pageUrl) {
        Document doc = null;
        try {
            // Download HTML document from the website
            doc = Jsoup.connect(pageUrl).get();
        } catch (IOException ex) {
            // Log a warning if there's an exception while fetching the page
            logger.warning("Overclockers scraper not able to extract documentation from the page, the page will not be scraped \n" + ex.getMessage());
        }
        return doc.select("ck-product-box");
    }

    // Method to scrape information from all pages
    @Override
    public void scrapeAll() {

        // List to store product elements
        Elements productUrlList = new Elements();
        for (int i = 1; i <= 8; i++) { // Looping through 8 pages
            // Scraping products from Overclockers website
            Elements productsList = returnAllProducts("https://www.overclockers.co.uk/pc-components/graphics-cards/nvidia-graphics-cards?page=" + i);
            productUrlList.addAll(productsList);
        }

        // Extracting elements from the product URLs
        extractElements(productUrlList);
        System.out.println("product size: " + productUrlList.size());
    }

    // Method to extract information from product elements
    public void extractElements(Elements elements) {
        for (Element element : elements) {
            // Extracting page URL, image URL, description, and brand
            String pageUrl = "https://www.overclockers.co.uk" + element.select("a").attr("href");
            String imgUrl = element.select("img.w-100.h-auto").attr("src");
            String description = element.select("img.w-100.h-auto").attr("alt");
            String brand = description.split(" ")[0];

            // Extracting price information
            Element priceSpan = element.select("span.price__amount").first();
            double price = -1;
            if (priceSpan != null) {
                String priceText = priceSpan.text().replace("£", "").replace(",", "");
                price = Double.parseDouble(priceText);
            }

            Document doc = null;
            String model = "";
            try {
                // Download HTML document from the product URL
                doc = Jsoup.connect(pageUrl).get();

                // Extracting model information from the breadcrumb
                Element anchor = doc.select(".breadcrumb .breadcrumb-item:last-of-type a").first();

                if (anchor != null) {
                    String extractedText = anchor.text();
                    model = extractedText.replace(" Graphics Cards", "");
                } else {
                    System.out.println("Element not found!");
                }

            } catch (IOException e) {
                // Logging info if an element from Overclocker scraper is not found
                logger.info("Element from Overclocker scraper not found");
            }

            // Calling the method to create classes with the extracted information
            createClasses(model, brand,  pageUrl, imgUrl, price, description);
        }
    }
}
