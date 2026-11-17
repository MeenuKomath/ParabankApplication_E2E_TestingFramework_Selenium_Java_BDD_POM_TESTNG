package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static ExtentReports getInstance() {

        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
            String reportPath = System.getProperty("user.dir") + "/target/extent-reports/ParaBank_Report_" + timestamp + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("ParaBank Automation Report");
            sparkReporter.config().setReportName("ParaBank E2E Test Execution");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Application", "ParaBank");
            extent.setSystemInfo("Framework", "Selenium + Cucumber + TestNG");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
        }
        return extent;
    }

    public static void setTest(ExtentTest test) {

        extentTest.set(test);
    }

    public static ExtentTest getTest() {

        return extentTest.get();
    }

    public static void unload() {

        extentTest.remove();
    }
}
