# ParaBank E2E Test Automation Framework

An end-to-end (E2E) Behavior-Driven Development (BDD) test automation framework built for the **ParaBank** banking web application using **Selenium WebDriver, Java, Cucumber, TestNG, Maven, Apache POI, Extent Reports, JCodec, and Log4j 2**, integrated with **GitHub Actions** for CI/CD execution.

---

## Key Features & Tech Stack
* **Language:** Java 17
* **UI Automation:** Selenium WebDriver 4 (Page Object Model architecture with explicit waits)
* **BDD Framework:** Cucumber JVM (Gherkin feature files, `Background`, `Scenario Outline`, and `DataTable`)
* **Test Execution & Parallelism:** TestNG (`ITestListener`, `testng.xml`, and `ThreadLocal` parallel scenario execution)
* **Build & Dependency Management:** Apache Maven
* **Data-Driven Testing:** Apache POI (Thread-safe `.xlsx` Excel test data reader)
* **Reporting & Evidence:**
    * **Extent Reports (Spark HTML):** Interactive test execution summary with embedded failure screenshots and inline playable video links
    * **JCodec MP4 Video Recorder:** Captures step-by-step browser viewport execution directly into `.mp4` video files
    * **Failure Screenshots:** Automatic `.png` capture on failed scenarios
* **Logging:** Apache Log4j 2 (Timestamped Console and Rolling File logs)
* **CI/CD Pipeline:** GitHub Actions (Automated headless Chrome/Edge execution with downloadable report, screenshot, video, and log artifacts)

---

## Automated Test Coverage (9 Modules)
1. **User Authentication (`Login.feature`):** Data-driven valid and invalid login validation using Excel (`TestData.xlsx`).
2. **Accounts Overview (`AccountsOverview.feature`):** Verifies the account summary table, user welcome banner, and all 8 Account Services navigation links.
3. **Open New Account (`OpenNewAccount.feature`):** Validates `CHECKING` and `SAVINGS` dropdown options, funding account selection, and dynamic generation of new account numbers.
4. **Transfer Funds (`TransferFunds.feature`):** Creates a new account, transfers funds from the primary account, and verifies updated balances on Accounts Overview.
5. **Bill Pay (`BillPay.feature`):** Submits payee details and validates payment confirmation details.
6. **Find Transactions (`FindTransactions.feature`):** Completes a bill payment and searches the transaction history by dollar amount to verify matching records.
7. **Update Contact Info (`UpdateContactInfo.feature`):** Updates customer address and phone details and verifies data persistence after reloading the profile.
8. **Request Loan (`RequestLoan.feature`):** Tests both approved and denied loan applications based on loan amount and down payment rules.
9. **User Logout (`Logout.feature`):** Validates session termination and redirection back to the Customer Login form.

---

## Project Structure

```text
ParabankApplication_E2E_TestingFramework
├── .github/
│   └── workflows/
│       └── ci-tests.yml                 # GitHub Actions CI/CD pipeline
├── src/
│   ├── main/java/
│   │   ├── base/
│   │   │   └── DriverManager.java       # ThreadLocal WebDriver & headless setup
│   │   ├── pages/
│   │   │   ├── AccountsOverviewPage.java
│   │   │   ├── BillPayPage.java
│   │   │   ├── FindTransactionsPage.java
│   │   │   ├── LoginPage.java
│   │   │   ├── LogoutPage.java
│   │   │   ├── OpenNewAccountPage.java
│   │   │   ├── RequestLoanPage.java
│   │   │   ├── TransferFundsPage.java
│   │   │   └── UpdateContactInfoPage.java
│   │   └── utils/
│   │       ├── ConfigReader.java        # Reads config.properties
│   │       ├── ExcelReader.java         # Synchronized Apache POI .xlsx reader
│   │       ├── ExtentReportManager.java # Thread-safe Extent Spark HTML setup
│   │       ├── LoggerUtil.java          # Log4j 2 logger wrapper
│   │       ├── ScenarioContext.java     # ThreadLocal state sharing across steps
│   │       ├── ScreenshotUtil.java      # Captures relative-path failure PNGs
│   │       └── VideoRecorderUtil.java   # JCodec browser viewport MP4 recorder
│   └── test/
│       ├── java/
│       │   ├── hooks/
│       │   │   └── Hooks.java           # @Before, @AfterStep video frames, @After cleanup
│       │   ├── listeners/
│       │   │   └── TestListener.java    # TestNG listener for Extent, screenshots & videos
│       │   ├── runners/
│       │   │   └── TestRunner.java      # Parallel TestNG Cucumber runner class
│       │   └── stepdefinitions/
│       │       ├── AccountsOverviewSteps.java
│       │       ├── BillPaySteps.java
│       │       ├── FindTransactionsSteps.java
│       │       ├── LoginSteps.java
│       │       ├── LogoutSteps.java
│       │       ├── OpenNewAccountSteps.java
│       │       ├── RequestLoanSteps.java
│       │       ├── TransferFundsSteps.java
│       │       └── UpdateContactInfoSteps.java
│       └── resources/
│           ├── config/
│           │   └── config.properties    # Application URL & default browser
│           ├── features/                # Cucumber Gherkin .feature files
│           ├── testdata/
│           │   └── TestData.xlsx        # External Excel test data
│           └── log4j2.xml               # Log4j 2 console & rolling file configuration
├── .gitignore
├── pom.xml                              # Maven dependencies & Surefire configuration
├── testng.xml                           # TestNG suite, thread count & listener setup
└── README.md
```

---

## Prerequisites
* **Java Development Kit (JDK) 17** installed and configured in `PATH`
* **Apache Maven 3.8+** installed
* **Google Chrome** or **Microsoft Edge** installed locally

---

## Configuration
Edit `src/test/resources/config/config.properties` to update the target environment or default browser:

```properties
url=https://parasoft.com
browser=chrome
implicitWait=10
```

To adjust parallel thread concurrency, update `data-provider-thread-count` in `testng.xml`:

```xml
<suite name="ParaBank E2E Automation Suite" data-provider-thread-count="3" verbose="1">
```

---

## How to Run Tests Locally

### 1. Using Maven CLI
* **Run the full E2E suite (`testng.xml`):**
  ```bash
  mvn clean test
  ```
* **Run with Microsoft Edge:**
  ```bash
  mvn clean test -Dbrowser=edge
  ```
* **Run in headless mode:**
  ```bash
  mvn clean test -Dbrowser=chrome -Dheadless=true
  ```
* **Run a specific module using Cucumber tags:**
  ```bash
  mvn clean test -Dcucumber.filter.tags="@TransferFunds"
  mvn clean test -Dcucumber.filter.tags="@BillPay"
  mvn clean test -Dcucumber.filter.tags="@RequestLoan"
  ```

### 2. Using IntelliJ IDEA
* Right-click **`testng.xml`** (or `src/test/java/runners/TestRunner.java`) and select **Run**.

---

## Reports, Videos, Screenshots & Logs
All execution artifacts are generated inside the `target/` directory after each test run:
* **Extent Spark HTML Report:** `target/extent-reports/ParaBank_Report_<timestamp>.html` (includes failure screenshots and clickable/embedded `.mp4` video links)
* **Cucumber Reports:** `target/cucumber-reports/cucumber.html` and `cucumber.json`
* **Recorded Execution Videos:** `target/videos/<ScenarioName>_<timestamp>.mp4`
* **Failure Screenshots:** `target/screenshots/<ScenarioName>_<timestamp>.png`
* **Execution Logs:** `target/logs/automation.log`

---

## CI/CD with GitHub Actions
The workflow defined in `.github/workflows/ci-tests.yml` runs automatically on every `push` or `pull_request` to `main`/`master`. You can also trigger it manually from the **Actions** tab (`ParaBank Selenium E2E Tests > Run workflow`) by selecting your preferred **browser** (`chrome` or `edge`) and **Cucumber tag filter** (e.g., `@Smoke`).

Once a workflow run finishes, download the **`parabank-test-reports-<run_number>`** ZIP artifact from the run summary page to view the Extent HTML report, `.mp4` videos, screenshots, and logs.
