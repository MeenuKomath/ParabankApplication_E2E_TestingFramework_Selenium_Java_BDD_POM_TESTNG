package pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LoggerUtil;
import utils.VideoRecorderUtil;

import java.time.Duration;

public class LogoutPage {
    private static final Logger log = LoggerUtil.getLogger(LogoutPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Navigation & Login Form Verification Locators
    private final By logoutLink = By.linkText("Log Out");
    private final By customerLoginHeading = By.xpath("//div[@id='leftPanel']/h2[text()='Customer Login']");
    private final By usernameField = By.name("username");
    private final By loginButton = By.xpath("//input[@value='Log In']");

    public LogoutPage(WebDriver driver) {

        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickLogoutLink() {

        log.info("Clicking Log Out link");
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        VideoRecorderUtil.captureStepFrame();
    }

    public boolean isLoginPanelDisplayed() {

        log.info("Verifying Customer Login panel is visible after logout");
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(customerLoginHeading));
        boolean usernameVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).isDisplayed();
        boolean buttonVisible = driver.findElement(loginButton).isDisplayed();
        VideoRecorderUtil.captureStepFrame();
        return heading.isDisplayed() && usernameVisible && buttonVisible;
    }

    public boolean isLogoutLinkHidden() {

        return driver.findElements(logoutLink).isEmpty();
    }
}
