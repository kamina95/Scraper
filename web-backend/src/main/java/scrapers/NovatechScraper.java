package scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NovatechScraper extends Scraper{

    private final String ABSOLUTE_URL = "https://www.novatech.co.uk";
    public Document doc;

    public Elements returnAllProducts(String pageUrl) {
        doc = null;
        try {
            //Download HTML document from website
            doc = Jsoup.connect(pageUrl).get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assert doc != null;
        return doc.select(".col-xs-12 > div.search-box-results");
    }

    public void scrapeAll() {
        Elements productUrlList = new Elements();
        for (int i = 1; i < 5; i++) { //4 pages
            Elements productsList = returnAllProducts("https://www.novatech.co.uk/products/components/nvidiageforcegraphicscards/?pg=" + i);
            productUrlList.addAll(productsList);
        }
        System.out.println(productUrlList.size());
        extractFeatures(productUrlList);
    }


    public void extractFeatures(Elements products) {

        for (Element product : products) {
            String relativeUrl = product.select(".search-box-title > h2 > a").attr("href");
            String[] titleArr = product.select(" .search-box-title > h2 > a").text().trim().split(" ", 2);
            String brand = titleArr[0];
            String description = product.select(".search-box-details").text();
            String pageUrl = ABSOLUTE_URL + relativeUrl;
            System.out.println("link --> " +pageUrl);
            Element priceElement = product.select(".newspec-price-listing").first();
            double price = -1;
            if (priceElement != null) {
                String priceText = priceElement.text().replace("£", "").replace(" inc vat", "");
                price = Double.parseDouble(priceText);
            }

            try {
                //Download HTML document from website
                doc = Jsoup.connect(pageUrl).get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            String imgUrldoc = doc.select(".imageblock-inner >img").attr("src");

            String  model = "";
            // Traverse all rows in the table
            for (Element row : doc.select(".spec-table.table tr")) {
                // Find the td with the text "Graphics Engine"
                Element tdName = row.select(".name").first();
                if (tdName != null && "Graphics Engine".equals(tdName.text())) {
                    // Extract the value from the next td element
                    Element tdValue = row.select(".value").first();
                    model = tdValue.text().replace("(Turing Architecture)","").replace("(Pascal)","")
                            .replace(" (Ada Lovelace Architecture)", "").replace("(Ampere)","").replace(" (Ampere Architecture)","").replace(" (Kepler)","").trim();; // This will print "NVIDIA GeForce RTX 4080 (Ada Lovelace Architecture)"
                    break;
                }
            }

            System.out.println(price + " " + model + "\n" + description + "\n" + imgUrldoc + "\n" + model);

            createClasses(model, brand, pageUrl, imgUrldoc, price, description);
        }
    }

}
