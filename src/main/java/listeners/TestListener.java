package listeners;

import base.DriverManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import io.cucumber.testng.PickleWrapper;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentReportManager;
import utils.LoggerUtil;
import utils.ScreenshotUtil;
import utils.VideoRecorderUtil;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

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
        String scenarioName = getScenarioName(result);
        log.info("PASSED: {}", scenarioName);
        ExtentReportManager.getTest().log(Status.PASS, "Scenario Passed: " + scenarioName);

        // Changed to true so passed test videos are kept and embedded in Extent Report
        File videoFile = VideoRecorderUtil.stopRecording(true);
        attachVideoToReport(videoFile, "Execution Video", scenarioName);

        DriverManager.quitDriver();
        ExtentReportManager.unload();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String scenarioName = getScenarioName(result);
        log.error("FAILED: {}", scenarioName, result.getThrowable());
        ExtentReportManager.getTest().log(Status.FAIL, "Scenario Failed: " + scenarioName);
        ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable());

        if (DriverManager.getDriver() != null) {
            String screenshotPath = ScreenshotUtil.captureScreenshot(DriverManager.getDriver(), scenarioName);
            ExtentReportManager.getTest().fail(
                    "Failure Screenshot",
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()
            );
        }

        // Stop recording before closing browser and attach clickable video to Extent Report
        File videoFile = VideoRecorderUtil.stopRecording(true);
        attachVideoToReport(videoFile, "Failure Execution Video", scenarioName);

        DriverManager.quitDriver();
        ExtentReportManager.unload();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String scenarioName = getScenarioName(result);
        log.warn("SKIPPED: {}", scenarioName);
        ExtentReportManager.getTest().log(Status.SKIP, "Scenario Skipped: " + scenarioName);

        VideoRecorderUtil.stopRecording(false);
        DriverManager.quitDriver();
        ExtentReportManager.unload();
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    private void attachVideoToReport(File videoFile, String label, String scenarioName) {
        // Fallback: locate the latest matching .mp4 in target/videos/ if ThreadLocal returned null
        if (videoFile == null || !videoFile.exists()) {
            File videoDir = new File(System.getProperty("user.dir") + "/target/videos/");
            String cleanPrefix = scenarioName.replaceAll("[^a-zA-Z0-9_-]", "_");
            File[] matchingFiles = videoDir.listFiles((dir, name) -> name.startsWith(cleanPrefix) && name.endsWith(".mp4"));
            if (matchingFiles != null && matchingFiles.length > 0) {
                Arrays.sort(matchingFiles, Comparator.comparingLong(File::lastModified).reversed());
                videoFile = matchingFiles[0];
            }
        }

        if (videoFile != null && videoFile.exists()) {
            // Relative path from target/extent-reports/ to target/videos/
            String relativeVideoPath = "../videos/" + videoFile.getName();

            String videoHtml = "<div style='margin-top:8px;'>"
                    + "<a href='" + relativeVideoPath + "' target='_blank' "
                    + "style='color:#4ea8de; font-weight:bold; text-decoration:underline;'>"
                    + "&#9658; Open " + label + " (" + videoFile.getName() + ")"
                    + "</a><br/>"
                    + "<video width='480' height='270' controls style='margin-top:6px; border:1px solid #444;'>"
                    + "<source src='" + relativeVideoPath + "' type='video/mp4'>"
                    + "Your browser does not support the video tag."
                    + "</video>"
                    + "</div>";

            ExtentReportManager.getTest().info(videoHtml);
            log.info("Attached video to Extent Report: {}", relativeVideoPath);
        } else {
            log.warn("No video file found to attach for scenario: {}", scenarioName);
        }
    }

    private String getScenarioName(ITestResult result) {
        Object[] params = result.getParameters();
        if (params != null && params.length > 0 && params[0] instanceof PickleWrapper) {
            return ((PickleWrapper) params[0]).getPickle().getName();
        }
        return result.getMethod().getMethodName();
    }
}
