package scrapers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.logging.Logger;

public class EbuyerScraper extends Scraper {

    // Logger for logging messages
    private static final Logger logger = Logger.getLogger(EbuyerScraper.class.getName());

    // WebDriver instance for Chrome browser
    private static WebDriver driver;

    // Chrome options for configuring WebDriver
    private static final ChromeOptions options = new ChromeOptions();

    // Counter for counting the number of products scraped
    private static int count = 0;

    // Method to scrape information from all pages on Ebuyer website
    @Override
    public void scrapeAll() {
        // Configuring ChromeOptions to run in headless mode
        options.setHeadless(true);
        // Initializing ChromeDriver with configured options
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // Looping through pages (1 to 9) to scrape product information
        for (int i = 1; i < 10; i++) {
            try {
                // Navigating to the Ebuyer page for Nvidia graphics cards
                driver.get("https://www.ebuyer.com/store/Components/cat/Graphics-Cards-Nvidia?page=" + i);

                // Scrolling down the page using JavaScriptExecutor
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.scrollBy(0,2000)");
                sleep(1000);

                // Handling the cookie consent by clicking on the "OK" button if present
                try {
                    clickCookie();
                } catch (Exception ignored) {
                }

                // Waiting for additional page content to load
                sleep(2000);

                // Extracting product information from the current page
                productsInfo();

                // Waiting for some time before moving to the next page
                sleep(7000);
            } catch (Exception e) {
                // Logging a warning if there's an exception while scraping the page
                logger.warning("Ebuyer scraper not able to extract documentation from the page, retrying\n" + e.getMessage());
            }
        }

        // Closing the WebDriver instance
        driver.close();
        // Printing the total count of products scraped
        System.out.println(count);
    }

    // Method to extract product information from the grid view
    public void productsInfo() {
        WebElement grid = driver.findElement(By.cssSelector(".grid-view.js-taxonomy-view.is-active"));
        List<WebElement> products = grid.findElements(By.cssSelector(".grid-item.js-listing-product"));
        for (WebElement product : products) {
            try {
                // Extracting URL, image URL, description, brand, price, and model information
                String url = product.findElement(By.tagName("a")).getAttribute("href");
                String imgUrl = product.findElement(By.tagName("img")).getAttribute("src");
                String description = product.findElement(By.cssSelector(".grid-item__title")).getText();
                String brand = description.split(" ")[0];
                String priceSpan = product.findElement(By.cssSelector(".price")).getText().split(" ")[1];
                double price = -1;
                if (priceSpan != null) price = Double.parseDouble(priceSpan);

                String model = product.findElement(By.cssSelector(".grid-item__ksp li:first-child")).getText();

                // Calling the method to create classes with the extracted information
                createClasses(model, brand, url, imgUrl, price, description);
                count++;
            } catch (Exception e) {
                // Logging info if an element from Ebuyer scraper is not found
                logger.info("Element from Ebuyer scraper not found");
            }
        }
    }

    // Method to handle clicking the cookie consent button
    public void clickCookie() {
        driver.findElement(By.cssSelector(".glyphicon.glyphicon-ok")).click();
    }

    // Method to introduce a delay in execution
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
