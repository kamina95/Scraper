package scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class OverclockersScraper extends Scraper {

    public Elements returnAllProducts(String pageUrl) {
        Document doc = null;
        try {
            doc = Jsoup.connect(pageUrl).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doc.select("ck-product-box");
    }

    @Override
    public void scrapeAll() {

        Elements productUrlList = new Elements();
        for (int i = 1; i <= 8; i++) { //8
            Elements productsList = returnAllProducts("https://www.overclockers.co.uk/pc-components/graphics-cards/nvidia-graphics-cards?page=" + i);
            productUrlList.addAll(productsList);
        }
        extractElements(productUrlList);
//        extractFeatureTable(productUrlList);
        System.out.println("product size: " + productUrlList.size());
    }

    public void extractElements(Elements elements) {
        for (Element element : elements) {
            String pageUrl = "https://www.overclockers.co.uk" + element.select("a").attr("href");
            String imgUrl = element.select("img.w-100.h-auto").attr("src");
            String description = element.select("img.w-100.h-auto").attr("alt");
            String brand = description.split(" ")[0];
            Element priceSpan = element.select("span.price__amount").first();

            double price = -1;
            if (priceSpan != null) {
                String priceText = priceSpan.text().replace("£", "").replace(",", "");
                price = Double.parseDouble(priceText);
            }
            Document doc = null;
            String model = "";
            try {
                doc = Jsoup.connect(pageUrl).get();

                Element anchor = doc.select(".breadcrumb .breadcrumb-item:last-of-type a").first();

                if (anchor != null) {
                    String extractedText = anchor.text();
                    model = extractedText.replace(" Graphics Cards", "");
                    //System.out.println(extractedText);
                } else {
                    System.out.println("Element not found!");
                }

            } catch (IOException e) {
                System.out.println("Error on the scrape of overclokers");
            }
//            (String model, String brandName, String url, String imgUrl, Double price, String description)
            createClasses(model, brand,  pageUrl, imgUrl, price, description);
//            System.out.println(imgUrl + "\n" + description + "\n" + price + "\n" + pageUrl + "\n" + model + "\n" + brand + "\n");
        }
    }
}

