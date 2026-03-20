package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ScreenshotUtils {
    public static Path capture(String testName) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            System.out.println("Driver is null. Screenshot skipped.");
            return null;
        }

        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destination = Paths.get("screenshots", testName + ".png");
            Files.createDirectories(destination.getParent());
            Files.copy(src.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            return destination;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
