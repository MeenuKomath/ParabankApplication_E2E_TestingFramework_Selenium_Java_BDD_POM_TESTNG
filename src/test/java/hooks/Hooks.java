package hooks;

import base.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.ConfigReader;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtil;


public class Hooks {

    private static final Logger log = LoggerUtil.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {
        log.info("Starting Scenario: {}", scenario.getName());
        DriverManager.initDriver(ConfigReader.getBrowser());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && DriverManager.getDriver() != null) {
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }

    }
}
