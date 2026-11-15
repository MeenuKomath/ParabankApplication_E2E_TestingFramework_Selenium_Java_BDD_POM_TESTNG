package listeners;

import base.DriverManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import io.cucumber.testng.PickleWrapper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentReportManager;
import utils.ScreenshotUtil;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtil;

public class TestListener implements ITestListener {

    private static final Logger log = LoggerUtil.getLogger(TestListener.class);
    private static final ExtentReports extent = ExtentReportManager.getInstance();

    @Override
    public void onTestStart(ITestResult result) {
        String scenarioName = getScenarioName(result);
        ExtentTest test = extent.createTest(scenarioName);
        ExtentReportManager.setTest(test);
        ExtentReportManager.getTest().log(Status.INFO, "Starting Scenario: " + scenarioName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "Scenario Passed: " + getScenarioName(result));
        DriverManager.quitDriver();
        ExtentReportManager.unload();
        log.info("PASSED: {}", getScenarioName(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String scenarioName = getScenarioName(result);
        ExtentReportManager.getTest().log(Status.FAIL, "Scenario Failed: " + scenarioName);
        ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable());

        if (DriverManager.getDriver() != null) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(DriverManager.getDriver(), scenarioName);
            ExtentReportManager.getTest().fail(
                    "Failure Screenshot",
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()
            );
        }

        DriverManager.quitDriver();
        ExtentReportManager.unload();
        log.error("FAILED: {}", getScenarioName(result), result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().log(Status.SKIP, "Scenario Skipped: " + getScenarioName(result));
        DriverManager.quitDriver();
        ExtentReportManager.unload();
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    private String getScenarioName(ITestResult result) {
        Object[] params = result.getParameters();
        if (params != null && params.length > 0 && params[0] instanceof PickleWrapper) {
            return ((PickleWrapper) params[0]).getPickle().getName();
        }
        return result.getMethod().getMethodName();
    }
}
