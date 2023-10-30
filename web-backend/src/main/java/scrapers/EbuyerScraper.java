package scrapers;

import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class EbuyerScraper extends Scraper {

    private static WebDriver driver;
    private static final ChromeOptions options = new ChromeOptions();
    private static int count = 0;

    @Override
    public void scrapeAll() {
        options.setHeadless(true);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        for (int i = 1; i < 10; i++) {
            try {
                driver.get("https://www.ebuyer.com/store/Components/cat/Graphics-Cards-Nvidia?page=" + i);
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.scrollBy(0,2000)");
                sleep(1000);
                try {
                    clickCookie();
                }catch(Exception ignored){
                }
                sleep(2000);
                productsInfo();
                sleep(7000);
//                WebElement nextPageButton = driver.findElement(By.cssSelector(".pagination__item.next-page a"));
//                nextPageButton.click();
            } catch (Exception e) {
                //break;
            }

        }
        driver.close();
        //System.out.println(count);
    }

    public void productsInfo() {
        WebElement grid = driver.findElement(By.cssSelector(".grid-view.js-taxonomy-view.is-active"));
        List<WebElement> products = grid.findElements(By.cssSelector(".grid-item.js-listing-product"));
        for (WebElement product : products) {
            try {
                String url = product.findElement(By.tagName("a")).getAttribute("href");
                String imgUrl = product.findElement(By.tagName("img")).getAttribute("src");
                String description = product.findElement(By.cssSelector(".grid-item__title")).getText();
                String brand = description.split(" ")[0];
                String priceSpan = product.findElement(By.cssSelector(".price")).getText().split(" ")[1];
                double price = -1;
                if (priceSpan != null) price = Double.parseDouble(priceSpan);

                String model = product.findElement(By.cssSelector(".grid-item__ksp li:first-child")).getText();
                createClasses(model, brand, url, imgUrl, price, description);
                count++;
                //System.out.println(url + "\n" + imgUrl + "\n" + description + "\n" + brand + "\n" + price + "\n" + model + "\n");
            }catch(Exception ignore){}
        }
    }

    public void clickCookie() {
        driver.findElement(By.cssSelector(".glyphicon.glyphicon-ok")).click();
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


//    @Override
//    public void scrapeAll() {
//        driver = new ChromeDriver(options);
//        driver.manage().window().maximize();
//        driver.get("https://www.ebuyer.com/store/Components/cat/Graphics-Cards-Nvidia");
//        sleep(1000);
//        clickCookie();
//        while(true) {
//            try {
//                sleep(3000);
//                productsInfo();
//                sleep(6000);
//                WebElement nextPageButton = driver.findElement(By.cssSelector(".pagination__item.next-page a"));
//                nextPageButton.click();
//            }catch(Exception e){
//                break;
//            }
//        }
//        System.out.println(count);
//        driver.close();
//
//    }

