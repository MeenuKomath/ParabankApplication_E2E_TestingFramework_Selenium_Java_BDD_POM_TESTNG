package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtil;
import utils.VideoRecorderUtil;

import java.time.Duration;

public class LoginPage {

        private static final Logger log = LoggerUtil.getLogger(LoginPage.class);
        private final WebDriver driver;
        private final WebDriverWait wait;

        // ParaBank Login Form Locators
        private final By usernameField = By.name("username");
        private final By passwordField = By.name("password");
        private final By loginButton = By.xpath("//input[@value='Log In']");

        // Post-Login & Error Locators
        private final By pageHeading = By.xpath("//div[@id='rightPanel']//h1");
        private final By logoutLink = By.linkText("Log Out");

        public LoginPage(WebDriver driver) {
            this.driver = driver;
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        }

        public void enterUsername(String username) {
            log.info("Entering username: {}", username);
            wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).clear();
            driver.findElement(usernameField).sendKeys(username);
            VideoRecorderUtil.captureStepFrame();
        }

        public void enterPassword(String password) {
            log.info("Entering password: ********");
            driver.findElement(passwordField).clear();
            driver.findElement(passwordField).sendKeys(password);
            VideoRecorderUtil.captureStepFrame();
        }

        public void clickLoginButton() {

            driver.findElement(loginButton).click();
            VideoRecorderUtil.captureStepFrame();
        }

    public String getPageHeadingText() {
        String heading = wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading)).getText().trim();
        log.info("Retrieved page heading: {}", heading);
        return heading;
    }

        public boolean isLogoutDisplayed() {
            return !driver.findElements(logoutLink).isEmpty();
        }
    }


