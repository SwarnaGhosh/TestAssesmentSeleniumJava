package tests;

import io.qameta.allure.Description;
import testHelper.TestListeners;
import base.BaseTest;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;
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

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        String validUser = ConfigReader.getProperty("valid.username");
        String validPass = ConfigReader.getProperty("valid.password");
        return new Object[][]{
                {validUser, validPass, true},
                {ConfigReader.getProperty("invalid.username"), ConfigReader.getProperty("invalid.password"), false},
                {"admin' OR '1'='1", "tests", false},
                {"<script>alert('XSS')</script>", "test", false}
        };
    }

    @DataProvider(name = "securityChallengeData")
    public Object[][] securityChallengeData() {
        return new Object[][]{
                {"testCaptchaUser", "testCaptchaPass", "captcha"},
                {"mfaUser", "mfaPassword", "mfa"},
                {"fake", "fake", "bot"}
        };
    }

    private void verifyLoginOutcome(String user, String pass, boolean shouldSucceed) {
        loginPage.login(user, pass);
        if (shouldSucceed) {
            Assert.assertTrue(homePage.getDashboard(), "Dashboard should load for valid credentials");
        } else {
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Error must display for invalid credentials");
        }
    }

    @Description("Data-driven login flow (valid/invalid)")
    @Test(dataProvider = "loginData", testName = "loginFlowDataDriven")
    public void loginFlowDataDriven(String user, String password, boolean shouldSucceed) {
        verifyLoginOutcome(user, password, shouldSucceed);
    }

    @Description("Invalid credential but not triggering security error message")
    @Test(dataProvider = "loginData", testName = "invalidCredentialsWithoutSecurityChallenge")
    public void invalidCredentialsWithoutSecurityChallenge(String user, String password, boolean shouldSucceed) {
        verifyLoginOutcome(user, password, shouldSucceed);
        String msg = loginPage.getErrorMessage();
        Assert.assertNotNull(msg, "Error message should be present");
        Assert.assertFalse(msg.trim().isEmpty(), "Error message should not be empty");

        // Ensure message is generic/user-friendly
        String lower = msg.toLowerCase();
        Assert.assertTrue(lower.contains("invalid") || lower.contains("incorrect") || lower.contains("failed"),
                "Error message should be generic and user-friendly");

        // Forbidden substrings that would reveal internal/security details
        String[] forbidden = new String[]{
                "stacktrace", "exception", "java.lang", "sql", "jdbc",
                "password", "token", "session", "trace", "at "
        };
        for (String f : forbidden) {
            Assert.assertFalse(lower.contains(f), "Error message must not reveal internal info: " + f);
        }
    }

    @Description("CAPTCHA/MFA/Bot-detection challenge path")
    @Test(dataProvider = "securityChallengeData", testName = "securityChallengesDataDriven")
    public void securityChallenges(String user, String password, String challengeType) {
        loginPage.login(user, password);

        switch (challengeType.toLowerCase()) {
            case "captcha" -> Assert.assertTrue(loginPage.isCaptchaDisplayed(), "CAPTCHA flow must be detected");
            case "mfa" -> {
                Assert.assertTrue(loginPage.isMfaPageDisplayed(), "MFA challenge should appear");
                String otp = loginPage.getOtpFromTestApi();
                loginPage.submitMfa(otp);
                Assert.assertTrue(homePage.getDashboard(), "After MFA should reach dashboard");
            }
            case "bot" -> Assert.assertTrue(loginPage.isBotBlockMessageVisible() || loginPage.isCaptchaDisplayed(),
                    "Bot-detection response expected after repeated invalid attempts");
            default -> throw new IllegalArgumentException("Unknown challenge type: " + challengeType);
        }
    }

    @Description("XSS Injection Tests")
    @Test(testName = "xssInjectionTest")
    public void xssInjectionTest() {
        String payload = "<script>alert('XSS')</script>";

        loginPage.login(payload, "test");

        String pageSource = driver.getPageSource();

        Assert.assertFalse(pageSource.contains(payload),
                "XSS payload not sanitized");
    }

    @Description("Login with Special Character Tests")
    @Test(testName = "specialCharacterTest")
    public void specialCharacterTest() {

        loginPage.login("admin' OR '1'='1","tests");
        Assert.assertTrue(loginPage.isErrorDisplayed());
    }

    @Description("Session reuse Test")
    @Test(testName = "sessionReuseTest")
    public void sessionReuseTest() {
        loginPage.login("valid", "valid");

        Cookie session = driver.manage().getCookieNamed("JSESSIONID");

        loginPage.logOut();

        driver.manage().addCookie(session);
        driver.navigate().refresh();

        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Session reuse should not be allowed");
    }

    @Description("Cookies Tests")
    @Test(testName = "cookieSecurityTest")
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
