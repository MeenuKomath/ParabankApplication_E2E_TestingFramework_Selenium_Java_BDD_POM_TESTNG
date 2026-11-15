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
import java.util.List;
import java.util.stream.Collectors;

public class AccountsOverviewPage {
    private static final Logger log = LoggerUtil.getLogger(AccountsOverviewPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // 1. Accounts Overview Table Locator
    private final By accountsTable = By.id("accountTable");
    private final By accountRows = By.xpath("//table[@id='accountTable']/tbody/tr");

    // 2. Welcome Message Locator (e.g., "Welcome John Smith")
    private final By welcomeMessage = By.xpath("//div[@id='leftPanel']/p[@class='smallText']");

    // 3. Account Services Subheading & Navigation Links
    private final By accountServicesHeader = By.xpath("//div[@id='leftPanel']/h2[text()='Account Services']");
    private final By accountServicesLinks = By.xpath("//div[@id='leftPanel']/ul/li/a");

    public AccountsOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isAccountsTableDisplayed() {
        log.info("Checking if Accounts Overview table is visible");
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(accountsTable));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(accountRows));
        VideoRecorderUtil.captureStepFrame();
        return table.isDisplayed() && !driver.findElements(accountRows).isEmpty();
    }

    public String getWelcomeMessageText() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeMessage)).getText().trim();
        log.info("Retrieved welcome message: {}", text);
        VideoRecorderUtil.captureStepFrame();
        return text;
    }

    public boolean isAccountServicesHeaderDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(accountServicesHeader)).isDisplayed();
    }

    public List<String> getAccountServicesLinkTexts() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(accountServicesLinks));
        List<String> actualLinks = driver.findElements(accountServicesLinks)
                .stream()
                .map(element -> element.getText().trim())
                .collect(Collectors.toList());
        log.info("Found Account Services links: {}", actualLinks);
        VideoRecorderUtil.captureStepFrame();
        return actualLinks;
    }
    // Add these locators and methods to AccountsOverviewPage.java:
    private final By accountsOverviewLink = By.linkText("Accounts Overview");

    public void navigateToAccountsOverview() {
        log.info("Navigating to Accounts Overview page");
        wait.until(ExpectedConditions.elementToBeClickable(accountsOverviewLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountsTable));
        VideoRecorderUtil.captureStepFrame();
    }

    public double getAccountBalance(String accountNumber) {
        By balanceLocator = By.xpath("//table[@id='accountTable']//a[text()='" + accountNumber + "']/parent::td/following-sibling::td[1]");
        String balanceText = wait.until(ExpectedConditions.visibilityOfElementLocated(balanceLocator)).getText().trim();
        log.info("Balance for account {}: {}", accountNumber, balanceText);
        VideoRecorderUtil.captureStepFrame();
        return Double.parseDouble(balanceText.replace("$", "").replace(",", ""));
    }

}
