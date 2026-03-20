package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.DriverFactory;

public class BaseTest {



    public void setup() {
        DriverFactory.initDriver();
    }

    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
