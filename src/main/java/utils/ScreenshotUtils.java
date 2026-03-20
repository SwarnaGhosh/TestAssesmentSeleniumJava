package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {
    public static void capture(String testName) {
       WebDriver driver = DriverFactory.getDriver();
        ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
