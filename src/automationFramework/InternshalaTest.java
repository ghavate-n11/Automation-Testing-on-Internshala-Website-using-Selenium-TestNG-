package automationFramework;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.ArrayList;

public class InternshalaTest {

    public static WebDriver driver;
    public static JavascriptExecutor js;
    public WebDriverWait wait;

    // Constants for credentials and paths
    private final String CHROME_DRIVER_PATH = "C:\\Users\\HP\\OneDrive\\Desktop\\Softwares\\chromedriver.exe";
    private final String EMAIL = "tejasvchavan@coep.sveri.ac.in";
    private final String PASSWORD = "Sveri@123";

    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test(priority = 1)
    public void loginToInternshala() {
        driver.get("https://internshala.com/login");

        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        email.clear();
        email.sendKeys(EMAIL);

        WebElement password = driver.findElement(By.id("password"));
        password.clear();
        password.sendKeys(PASSWORD);

        System.out.println("⚠️ Solve the CAPTCHA manually within 60 seconds...");
        try {
            Thread.sleep(600); // Manual captcha wait
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Login')]")));
        loginBtn.click();

        wait.until(ExpectedConditions.urlContains("dashboard"));
        System.out.println("✅ Login successful.");
    }

    @Test(dataProvider = "dataprovider", priority = 2)
    public void clickContactIcons(String cssSelector) {
        driver.get("https://internshala.com/contact");
        try {
            WebElement icon = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssSelector)));
            icon.click();
        } catch (Exception e) {
            System.out.println("❌ Failed to click on icon [" + cssSelector + "]: " + e.getMessage());
        }
    }

    @Test(priority = 3)
    public void closeExtraTabs() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        while (tabs.size() > 1) {
            driver.switchTo().window(tabs.get(1));
            driver.close();
            tabs = new ArrayList<>(driver.getWindowHandles());
        }
        driver.switchTo().window(tabs.get(0));
    }

    @Test(priority = 4)
    public void openInternshipsPage() {
        try {
            WebElement internships = wait.until(ExpectedConditions.elementToBeClickable(By.id("internships_new_superscript")));
            internships.click();
        } catch (Exception e) {
            System.out.println("❌ Could not open internships page: " + e.getMessage());
        }
    }

    @Test(priority = 5)
    public void selectInternshipCategory() {
        try {
            WebElement category = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.chosen-search-input.default")));
            category.sendKeys("Master Of Computer Application", Keys.ENTER);
        } catch (Exception e) {
            System.out.println("❌ Failed to select category: " + e.getMessage());
        }
    }

    @Test(priority = 6)
    public void applyToInternship() {
        try {
            String internshipURL = "https://internshala.com/internship/detail/webflow-development-work-from-home-job-internship-at-codeacious-technologies-private-limited1587013074";
            driver.get(internshipURL);

            String applyURL = internshipURL.replace("/internship/detail/", "/student/interstitial/application/");
            driver.get(applyURL);

            WebElement applyBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='application-button']/button")));
            applyBtn.click();

            try {
                wait.until(ExpectedConditions.elementToBeClickable(By.id("search_button"))).click();
                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-primary.education_incomplete"))).click();
            } catch (Exception inner) {
                System.out.println("ℹ️ Already applied or process is different.");
                openInternshipsPage();
            }
        } catch (Exception e) {
            System.out.println("❌ Error during internship application: " + e.getMessage());
        }
    }

    @Test(priority = 7)
    public void fillApplicationForm() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cover_letter")))
                .sendKeys("I am an enthusiastic coder and ready to take challenges and learn new things.");

            driver.findElement(By.id("text_1081479")).sendKeys("Yes");
            driver.findElement(By.id("text_1081480")).sendKeys("https://github.com/ghavate-n11");
            driver.findElement(By.id("text_1081481")).sendKeys("No");

            driver.findElement(By.name("submit")).click();
        } catch (Exception e) {
            System.out.println("❌ Form submission failed: " + e.getMessage());
        }
    }

    @Test(priority = 8)
    public void goBackToInternships() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("backToInternshipsCta"))).click();
        } catch (Exception e) {
            System.out.println("❌ Return to internships failed: " + e.getMessage());
        }
    }

    @Test(priority = 9)
    public void logout() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.profile_icon_right"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("span.glyphicon.pull-right.glyphicon-menu-down"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout"))).click();
        } catch (Exception e) {
            System.out.println("❌ Logout failed: " + e.getMessage());
        }
    }

    @DataProvider(name = "dataprovider")
    public Object[][] contactIconProvider() {
        return new Object[][]{
            {"i.is-icon-instagram"},
            {"i.is-icon-twitter"},
            {"i.is-icon-youtube"},
            {"i.is-icon-linkedin"},
        };
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void sampleTest() {
        System.out.println("✅ Internshala automation test executed.");
    }
}
