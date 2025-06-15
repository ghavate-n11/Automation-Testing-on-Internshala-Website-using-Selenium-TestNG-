package automationFramework;// Not necessary but create one package for better understanding

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set; // Import Set for window handles

public class InternshalaTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    // Constants
    private static final String CHROME_DRIVER_PATH = "C:\\Users\\HP\\OneDrive\\Desktop\\Softwares\\chromedriver.exe";
    private static final String BASE_URL = "https://internshala.com";
    private static final String EMAIL = "nilesjghavate11@gmail.com";//Update mail id as per your requirement 
    private static final String PASSWORD = "*******3";//also password change as per your requirement 
    private static final String INTERNSHIP_URL = BASE_URL
            + "/internship/detail/webflow-development-work-from-home-job-internship-at-codeacious-technologies-private-limited1587013074";

    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        js = (JavascriptExecutor) driver;
    }

    @Test(priority = 1)
    public void login() {
        driver.get(BASE_URL + "/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(EMAIL);
        driver.findElement(By.id("password")).sendKeys(PASSWORD);

        System.out.println("⚠️ Please solve CAPTCHA within 8 seconds...");
        sleep(8000);

        WebElement loginBtn = wait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Login')]")));
        loginBtn.click();

        wait.until(ExpectedConditions.urlContains("dashboard"));
        Assert.assertTrue(driver.getCurrentUrl().contains("dashboard"), "❌ Login failed!");
        System.out.println("✅ Logged in successfully.");
    }

    @Test(dataProvider = "iconProvider", priority = 2)
    public void clickSocialIcons(String iconSelector) {
        driver.get(BASE_URL + "/contact"); // Navigate to the contact page each time

        try {
            WebElement icon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(iconSelector)));
            js.executeScript("arguments[0].scrollIntoView(true);", icon);

            WebElement parentLink = icon.findElement(By.xpath("./ancestor::a[1]"));
            String originalWindow = driver.getWindowHandle();

            parentLink.click();

            // Wait for new tab to open
            wait.until(driver -> driver.getWindowHandles().size() > 1);

            Set<String> allWindowHandles = driver.getWindowHandles();
            for (String windowHandle : allWindowHandles) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    System.out.println("✅ Opened: " + driver.getTitle() + " - URL: " + driver.getCurrentUrl());
                    // --- IMPORTANT CHANGE: Removed driver.close(); here ---
                    // The new tab will now remain open.
                    break; // Once we've switched to the new tab, we can break the loop
                }
            }
            // --- IMPORTANT CHANGE: Removed driver.switchTo().window(originalWindow); here
            // ---
            // We are deliberately staying on the newly opened social media tab.
            System.out.println("✅ Clicked social icon: " + iconSelector + ". Staying on the new tab.");
        } catch (Exception e) {
            System.out.println("❌ Error handling social icon [" + iconSelector + "]: " + e.getMessage());
            // It's good practice to switch back to the original window in case of an error
            // to ensure subsequent tests don't fail due to being in the wrong context.
            if (driver.getWindowHandles().size() > 1) {
                String originalWindow = driver.getWindowHandles().iterator().next(); // Get the first handle (original)
                driver.switchTo().window(originalWindow);
            }
        }
    }

    @Test(priority = 3)
    public void closeOtherTabs() {
        // This method will now close all tabs except the first one,
        // which would typically be the original Internshala tab.
        // If clickSocialIcons leaves multiple tabs open, this will close them.
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        if (tabs.size() > 1) { // Only close if there's more than one tab
            for (int i = 1; i < tabs.size(); i++) {
                driver.switchTo().window(tabs.get(i)).close();
            }
        }
        driver.switchTo().window(tabs.get(0));
        System.out.println("✅ Closed all extra tabs.");
    }

    @Test(priority = 4)
    public void navigateToInternships() {
        // Ensure we are on the main Internshala window before navigating.
        // This is crucial if previous tests left us on a social media tab.
        Set<String> allWindowHandles = driver.getWindowHandles();
        if (allWindowHandles.size() > 1) {
            List<String> tabs = new ArrayList<>(allWindowHandles);
            driver.switchTo().window(tabs.get(0)); // Switch to the first (original) tab
        }
        driver.get(BASE_URL); // Navigate to base URL first to ensure the element is present
        try {
            WebElement internshipsBtn = wait
                    .until(ExpectedConditions.elementToBeClickable(By.id("internships_new_superscript")));
            internshipsBtn.click();
            System.out.println("✅ Navigated to internships page.");
        } catch (Exception e) {
            System.out.println("❌ Failed to open internships page: " + e.getMessage());
        }
    }

    @Test(priority = 5)
    public void selectCategory() {
        try {
            WebElement input = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("input.chosen-search-input.default")));
            input.sendKeys("Master Of Computer Application", Keys.ENTER);
            System.out.println("✅ Selected category.");
        } catch (Exception e) {
            System.out.println("❌ Failed to select category: " + e.getMessage());
        }
    }

    @Test(priority = 6)
    public void applyForInternship() {
        try {
            driver.get(INTERNSHIP_URL);
            String applyPage = INTERNSHIP_URL.replace("/internship/detail/", "/student/interstitial/application/");
            driver.get(applyPage);

            WebElement applyBtn = wait
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='application-button']/button")));
            applyBtn.click();

            try {
                // These elements might not always appear, hence the inner try-catch
                wait.until(ExpectedConditions.elementToBeClickable(By.id("search_button"))).click();
                wait.until(ExpectedConditions
                        .elementToBeClickable(By.cssSelector("button.btn.btn-primary.education_incomplete"))).click();
            } catch (Exception ignored) {
                System.out.println(
                        "ℹ️ Already applied or alternate flow (elements like search_button or education_incomplete not found).");
            }

            System.out.println("✅ Internship application triggered.");
        } catch (Exception e) {
            System.out.println("❌ Error during application: " + e.getMessage());
        }
    }

    @Test(priority = 7)
    public void fillForm() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cover_letter")))
                    .sendKeys("I am an enthusiastic coder ready to take on challenges.");

            driver.findElement(By.id("text_1081479")).sendKeys("Yes");
            driver.findElement(By.id("text_1081480")).sendKeys("https://github.com/ghavate-n11");
            driver.findElement(By.id("text_1081481")).sendKeys("No");

            driver.findElement(By.name("submit")).click();
            System.out.println("✅ Application form submitted.");
        } catch (Exception e) {
            System.out.println("❌ Could not submit form: " + e.getMessage());
        }
    }

    @Test(priority = 8)
    public void returnToInternships() {
        try {
            WebElement backBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("backToInternshipsCta")));
            backBtn.click();
            System.out.println("✅ Returned to internships.");
        } catch (Exception e) {
            System.out.println("❌ Return failed: " + e.getMessage());
        }
    }

    @Test(priority = 9)
    public void logout() {
        try {
            // Ensure we are on the main Internshala window before attempting logout
            Set<String> allWindowHandles = driver.getWindowHandles();
            if (allWindowHandles.size() > 1) {
                List<String> tabs = new ArrayList<>(allWindowHandles);
                driver.switchTo().window(tabs.get(0)); // Switch to the first (original) tab
            }

            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.profile_icon_right"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span.glyphicon-menu-down"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout"))).click();
            System.out.println("✅ Logged out successfully.");
        } catch (Exception e) {
            System.out.println("❌ Logout failed: " + e.getMessage());
        }
    }

    @Test(priority = 10)
    public void sampleTest() {
        System.out.println("✅ Sample test executed.");
    }

    @DataProvider(name = "iconProvider")
    public Object[][] provideIcons() {
        return new Object[][] {
                { "i.is-icon-instagram" },
                { "i.is-icon-twitter" },
                { "i.is-icon-youtube" },
                { "i.is-icon-linkedin" },
        };
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("🧹 Browser closed. Test suite completed.");
        }
    }

    // Utility Method
    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
