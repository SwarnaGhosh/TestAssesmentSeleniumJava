package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class DriverFactory {

    public static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

    public static void initDriver() {
        String browser = ConfigReader.getProperty("browser").toLowerCase();

        switch (browser) {
            case "chrome":
                driver.set(new ChromeDriver());
                break;

            case "firefox":
                driver.set(new FirefoxDriver());
                break;

            case "edge":
                driver.set(new EdgeDriver());
                break;

            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        driver.get().get(ConfigReader.getProperty("baseUrl"));
    }
    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver(){

        // closing the browser
        driver.get().quit();
        driver.remove();
    }


}
