package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    public static String captureScreenshot(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String cleanName = testName.replaceAll("[^a-zA-Z0-9_-]", "_");
        String fileName = cleanName + "_" + timestamp + ".png";
        String screenshotDir = System.getProperty("user.dir") + "/target/screenshots/";
        File destFile = new File(screenshotDir + fileName);

        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return relative path from target/extent-reports/ so reports are portable across machines
        return "../screenshots/" + fileName;
    }
}
