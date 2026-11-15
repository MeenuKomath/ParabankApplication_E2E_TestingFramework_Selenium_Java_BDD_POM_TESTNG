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

    // Navigation & Heading Locators
    private final By accountsOverviewLink = By.linkText("Accounts Overview");
    private final By pageHeading = By.xpath("//div[@id='showOverview']/h1");

    // Table Locators
    private final By accountsTable = By.id("accountTable");
    private final By tableHeaders = By.xpath("//table[@id='accountTable']/thead/tr/th");
    private final By accountRows = By.xpath("//table[@id='accountTable']/tbody/tr[td/a]");
    private final By firstAccountLink = By.xpath("(//table[@id='accountTable']/tbody/tr/td/a)[1]");
    private final By totalBalanceCell = By.xpath("//table[@id='accountTable']/tbody/tr[td/b[text()='Total']]/td[2]/b");

    // Left Panel Locators
    private final By welcomeMessage = By.xpath("//div[@id='leftPanel']/p[@class='smallText']");
    private final By accountServicesHeader = By.xpath("//div[@id='leftPanel']/h2[text()='Account Services']");
    private final By accountServicesLinks = By.xpath("//div[@id='leftPanel']/ul/li/a");

    // Account Details Page Locator (after clicking an account link)
    private final By accountDetailsId = By.id("accountId");

    public AccountsOverviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToAccountsOverview() {
        log.info("Navigating to Accounts Overview page");
        wait.until(ExpectedConditions.elementToBeClickable(accountsOverviewLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(accountsTable));
        VideoRecorderUtil.captureStepFrame();
    }

    public String getPageHeading() {
        String heading = wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeading)).getText().trim();
        log.info("Retrieved Accounts Overview heading: {}", heading);
        return heading;
    }

    public boolean isAccountsTableDisplayed() {
        log.info("Verifying Accounts Overview table visibility");
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(accountsTable));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(accountRows));
        VideoRecorderUtil.captureStepFrame();
        return table.isDisplayed() && !driver.findElements(accountRows).isEmpty();
    }

    public List<String> getTableHeaderTexts() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tableHeaders));
        List<String> headers = driver.findElements(tableHeaders)
                .stream()
                .map(el -> el.getText().trim())
                .collect(Collectors.toList());
        log.info("Accounts Overview table headers: {}", headers);
        VideoRecorderUtil.captureStepFrame();
        return headers;
    }

    public double getAccountBalance(String accountNumber) {
        By balanceLocator = By.xpath("//table[@id='accountTable']//a[text()='" + accountNumber + "']/parent::td/following-sibling::td[1]");
        String balanceText = wait.until(ExpectedConditions.visibilityOfElementLocated(balanceLocator)).getText().trim();
        log.info("Balance for account {}: {}", accountNumber, balanceText);
        VideoRecorderUtil.captureStepFrame();
        return parseCurrency(balanceText);
    }

    public double calculateSumOfIndividualBalances() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(accountRows));
        List<WebElement> rows = driver.findElements(accountRows);
        double sum = 0.0;
        for (WebElement row : rows) {
            String balanceText = row.findElement(By.xpath("./td[2]")).getText().trim();
            sum += parseCurrency(balanceText);
        }
        log.info("Calculated sum of individual account balances: {}", sum);
        return sum;
    }

    public double getDisplayedTotalBalance() {
        String totalText = wait.until(ExpectedConditions.visibilityOfElementLocated(totalBalanceCell)).getText().trim();
        double total = parseCurrency(totalText);
        log.info("Displayed Total balance on page: {}", total);
        VideoRecorderUtil.captureStepFrame();
        return total;
    }

    public String clickFirstAccountAndGetNumber() {
        WebElement accountLink = wait.until(ExpectedConditions.elementToBeClickable(firstAccountLink));
        String selectedAccountId = accountLink.getText().trim();
        log.info("Clicking account number link: {}", selectedAccountId);
        accountLink.click();
        VideoRecorderUtil.captureStepFrame();
        return selectedAccountId;
    }

    public String getAccountDetailsPageAccountId() {
        String displayedId = wait.until(ExpectedConditions.visibilityOfElementLocated(accountDetailsId)).getText().trim();
        log.info("Navigated to Account Details for ID: {}", displayedId);
        VideoRecorderUtil.captureStepFrame();
        return displayedId;
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
        List<String> links = driver.findElements(accountServicesLinks)
                .stream()
                .map(element -> element.getText().trim())
                .collect(Collectors.toList());
        log.info("Found Account Services links: {}", links);
        VideoRecorderUtil.captureStepFrame();
        return links;
    }

    private double parseCurrency(String currencyText) {
        return Double.parseDouble(currencyText.replace("$", "").replace(",", "").trim());
    }
}
