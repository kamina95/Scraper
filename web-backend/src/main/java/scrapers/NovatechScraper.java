package scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.logging.Logger;

public class NovatechScraper extends Scraper {

    // Logger for logging messages
    private static final Logger logger = Logger.getLogger(NovatechScraper.class.getName());

    // Base URL for constructing absolute URLs
    private final String ABSOLUTE_URL = "https://www.novatech.co.uk";

    // Document to store the HTML content
    public Document doc;

    // Method to return all products from a given page URL
    public Elements returnAllProducts(String pageUrl) {
        doc = null;
        try {
            // Download HTML document from the website
            doc = Jsoup.connect(pageUrl).get();
        } catch (Exception ex) {
            // Log a warning if there's an exception while fetching the page
            logger.warning("Novatech scraper not able to extract documentation from the page, the page will not be scraped \n" + ex.getMessage());
        }
        assert doc != null;
        return doc.select(".col-xs-12 > div.search-box-results");
    }

    // Method to scrape information from all pages
    public void scrapeAll() {
        Elements productUrlList = new Elements();
        for (int i = 1; i < 5; i++) {
            // Scraping products from Novatech website
            Elements productsList = returnAllProducts("https://www.novatech.co.uk/products/components/nvidiageforcegraphicscards/?pg=" + i);
            productUrlList.addAll(productsList);
        }
        System.out.println(productUrlList.size());
        // Extracting features from the product URLs
        extractFeatures(productUrlList);
    }

    // Method to extract features from product elements
    public void extractFeatures(Elements products) {
        for (Element product : products) {
            // Extracting relative URL, brand, description, and page URL
            String relativeUrl = product.select(".search-box-title > h2 > a").attr("href");
            String[] titleArr = product.select(".search-box-title > h2 > a").text().trim().split(" ", 2);
            String brand = titleArr[0];
            String description = product.select(".search-box-details").text();
            String pageUrl = ABSOLUTE_URL + relativeUrl;
            System.out.println("link --> " + pageUrl);

            // Extracting price information
            Element priceElement = product.select(".newspec-price-listing").first();
            double price = -1;
            if (priceElement != null) {
                String priceText = priceElement.text().replace("£", "").replace(" inc vat", "");
                price = Double.parseDouble(priceText);
            }

            try {
                // Download HTML document from the product URL
                doc = Jsoup.connect(pageUrl).get();
            } catch (Exception ex) {
                // Log a warning if there's an exception while fetching the product page
                logger.warning("Novatech scraper not able to extract documentation from the page \n" + ex.getMessage());
            }

            // Extracting image URL from the product page
            String imgUrldoc = doc.select(".imageblock-inner >img").attr("src");

            // Extracting model information from the product page
            String model = "";
            // Traverse all rows in the table
            for (Element row : doc.select(".spec-table.table tr")) {
                // Find the td with the text "Graphics Engine"
                Element tdName = row.select(".name").first();
                if (tdName != null && "Graphics Engine".equals(tdName.text())) {
                    // Extract the value from the next td element
                    Element tdValue = row.select(".value").first();
                    model = tdValue.text().replace("(Turing Architecture)","").replace("(Pascal)","")
                            .replace(" (Ada Lovelace Architecture)", "").replace("(Ampere)","").replace(" (Ampere Architecture)","").replace(" (Kepler)","").trim();
                    break;
                }
            }

            // Calling the method to create classes with the extracted information
            createClasses(model, brand, pageUrl, imgUrldoc, price, description);
        }
    }
}
