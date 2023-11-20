package scrapers;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.List;
import java.util.logging.Logger;

public class AmazonScraper extends Scraper {

    private static final Logger logger = Logger.getLogger(AmazonScraper.class.getName());
    private static WebDriver driver;
    private static final ChromeOptions options = new ChromeOptions();
    private static boolean isIn = false;

    public void connectToAmazon(){

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.amazon.co.uk/");
        sleep(8000);
        try {
            WebElement clickable = driver.findElement(By.id("nav-hamburger-menu"));
            clickable.click();
            isIn = true;
        } catch (Exception e) {
            driver.close();
            sleep((int) (Math.random()*4000));
            logger.info("Restarting Amazon Scraper driver");
        }

    }

    public void scrapeAll() {
        options.setHeadless(false);
        while(!isIn) {
            connectToAmazon();
        }
        sleep(1000);
        accessMenu();
        clickOnMenu();

        for(int pages = 0; pages<1;pages++) {
            List<WebElement> graphicRows = driver.findElements(By.cssSelector("div[data-component-type='s-search-result']"));
//            while (graphicRows.size() == 0) {
//                sleep(1000);
//                System.out.println(graphicRows.size());
//            graphicRows = driver.findElements(By.cssSelector("div[data-component-type='s-search-result]"));
//            }
            System.out.println(graphicRows.size());
            for (int i = 0; i < graphicRows.size(); i++) {
                sleep(800);
                WebElement item = graphicRows.get(i);
                try {
                    if (item.findElement(By.cssSelector("span.a-color-secondary")).getText().equals("Sponsored")) {
                        System.out.println("sponsored");
                    } else {
                        extractInfo(item);
                    }
                    graphicRows = driver.findElements(By.cssSelector("div[data-component-type='s-search-result']"));

                } catch (StaleElementReferenceException e) {
                    graphicRows = driver.findElements(By.cssSelector("div[data-component-type='s-search-result']"));
                    i--;
                } catch (Exception e) {
                    extractInfo(item);
                }
            }
            sleep(1000);
            try {
                WebDriverWait wait = new WebDriverWait(driver, 10); // 10 seconds timeout
                WebElement nextButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a.s-pagination-item.s-pagination-next.s-pagination-button.s-pagination-separator")));
                // Scroll to the next button
                Actions actions = new Actions(driver);
                actions.moveToElement(nextButton).perform();

                // Click the next button
                nextButton.click();
//                System.out.println("it came here");
//
//                driver.findElement(By.cssSelector("a.s-pagination-item.s-pagination-next.s-pagination-button.s-pagination-separator")).click();
            }catch (Exception e){
                logger.info("Element not found on Amazon scraper");
            }
            sleep(1500);
        }
        //Exit driver and close Chrome
        driver.quit();
    }

    public void accessMenu(){
        List<WebElement> hamburgerMenu = driver.findElements(By.cssSelector(".hmenu-item"));
        String idMenu = "";
        for (WebElement item : hamburgerMenu) {
            if (item.getText().equals("Electronics & Computers")) {
                item.click();
                idMenu = item.getAttribute("data-menu-id");
                break;

            }
        }
        sleep(1000);

        List<WebElement> innerMenu = driver.findElements(By.cssSelector("ul[data-menu-id='" + idMenu + "'] > li"));
        for (WebElement item : innerMenu) {
            if (item.getText().equals("Computer Components")) {
                sleep(100);
                Actions actions = new Actions(driver);
                actions.moveToElement(item).click().perform();
                break;
            }
        }

        sleep(1000);
    }

    public void clickOnMenu(){
        try {
            WebElement graphicsCard = driver.findElement(By.cssSelector("a[title='Graphics Cards']"));
            graphicsCard.click();
            sleep(1000);
        } catch (Exception e) {
            List<WebElement> compAcesMenu = driver.findElements(By.cssSelector(".nav-a-content"));
            for (WebElement item : compAcesMenu) {
                if (item.getText().trim().equals("Components")) {
                    item.click();
                    break;
                }
            }
            WebElement graphicsCard = driver.findElement(By.cssSelector("a[title='Graphics Cards']"));
            graphicsCard.click();
        }
        sleep(1000);
        driver.findElement(By.cssSelector("#sp-cc-accept")).click();
        sleep(1000);
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.cssSelector("li[aria-label='NVIDIA'] > span > a > div"))).click().perform();
        sleep(1000);
    }

    public void extractInfo(WebElement product) {
        try {
            String imgUrl = product.findElement(By.cssSelector(".a-section .aok-relative .s-image-fixed-height > img")).getAttribute("src");
            WebElement priceSpan  = product.findElement(By.cssSelector("span.a-price"));
            double price = -1;
            if (priceSpan != null) {
                String priceText = priceSpan.getText().trim().replace("\n", ".").replace("£","").replace("\\s+", "").replace(",", "");
                price = Double.parseDouble(priceText);
            }
            WebElement anchorElement = driver.findElement(By.cssSelector("a.a-link-normal.s-no-outline"));
            String link = anchorElement.getAttribute("href");

            product.findElement(By.cssSelector("img.s-image")).click();
            sleep(2000);
            String description = driver.findElement(By.cssSelector("span#productTitle")).getText();
            WebElement sepecsTable = driver.findElement(By.cssSelector("#productDetails_techSpec_section_1"));
            List<WebElement> rows = sepecsTable.findElements(By.tagName("tr"));
            String brand = null;
            String graphicsCoprocessor = null;
            for (WebElement row : rows) {
                WebElement headingElement = row.findElement(By.tagName("th"));
                String heading = headingElement.getText().trim();

                if ("Brand".equals(heading)) {
                    brand = row.findElement(By.tagName("td")).getText().trim();
                } else if ("Graphics Coprocessor".equals(heading)) {
                    graphicsCoprocessor = row.findElement(By.tagName("td")).getText().trim();
                }

                if (brand != null && graphicsCoprocessor != null) {
                    break;
                }
            }
            System.out.println(price + " " + imgUrl + " " + description + "\n" + brand + " " + graphicsCoprocessor + "\n" + link + "\n");
            createClasses(graphicsCoprocessor, brand, link, imgUrl, price, description);

        } catch (Exception ex) {
            logger.info("Table element not found on Amazon scraper");
        }finally {
            driver.navigate().back();
            driver.navigate().refresh();
            sleep(1000);
        }
    }
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
