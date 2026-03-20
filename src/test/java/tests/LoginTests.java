package tests;

import testHelper.TestListeners;
import base.BaseTest;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.DriverFactory;


@Listeners(TestListeners.class)
public class LoginTests extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;
    WebDriver driver;



    @BeforeMethod
    public void setup() {
        super.setup();
        driver = DriverFactory.getDriver();
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
    }

    @Test
    public void validLoginTest(){
        loginPage.login("abc","bcd");
        Assert.assertTrue(homePage.getDashboard());
    }

    @Test
    public void invalidLoginTest(){
        loginPage.login("abc","bcd");
        Assert.assertTrue(loginPage.isErrorDisplayed());
    }

    @Test
    public void xssInjectionTest() {
        String payload = "<script>alert('XSS')</script>";

        loginPage.login(payload, "test");

        String pageSource = driver.getPageSource();

        Assert.assertFalse(pageSource.contains(payload),
                "XSS payload not sanitized");
    }

    @Test
    public void specialCharacterTest() {

        loginPage.login("admin' OR '1'='1","tests");
        Assert.assertTrue(loginPage.isErrorDisplayed());
    }

    @Test
    public void sessionReuseTest() {
        loginPage.login("valid", "valid");

        Cookie session = driver.manage().getCookieNamed("JSESSIONID");

        loginPage.logOut();

        driver.manage().addCookie(session);
        driver.navigate().refresh();

        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Session reuse should not be allowed");
    }

    @Test
    public void cookieSecurityTest() {
        loginPage.login("valid", "valid");

        for (Cookie cookie : driver.manage().getCookies()) {


            Assert.assertTrue(cookie.isHttpOnly(), "HttpOnly not set");
            Assert.assertTrue(cookie.isSecure(), "Secure flag not set");
        }
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();
    }
}
