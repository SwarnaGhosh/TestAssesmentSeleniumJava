package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.ConfigReader;

public class LoginPage {

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "username")
    WebElement username;

    @FindBy(id = "password")
    WebElement password;

    @FindBy(id = "loginBtn")
    WebElement loginBtn;

    @FindBy(id = "errorMsg")
    WebElement errorMsg;

    @FindBy(id = "logOut")
    WebElement logOut;

    @FindBy (id = "captchaEnable")
    WebElement captcha;

    @FindBy (id = "mfaEnabledPage")
    WebElement mfaEnabledPage;

    @FindBy (id = "botBlockMessageVisible")
    WebElement botBlockMessageVisible;

    @FindBy(id = "otp")
    WebElement otpInput;

    @FindBy(id = "mfaSubmit")
    WebElement mfaSubmitBtn;

    public void login(String user, String pass) {
        username.sendKeys(user);
        password.sendKeys(pass);
        loginBtn.click();
    }

    public String getOtpFromTestApi() {
        // In a real test environment this should query a test backend or OTP stub
        return ConfigReader.getProperty("mfa.otp", "123456");
    }

    public void submitMfa(String otp) {
        otpInput.sendKeys(otp);
        mfaSubmitBtn.click();
    }

    public boolean isErrorDisplayed() {
        return errorMsg.isDisplayed();
    }

    public void logOut() {
        logOut.click();
    }

    public boolean isLoginPageDisplayed() {
        return loginBtn.isDisplayed();
    }

    public boolean isCaptchaDisplayed() {
        return captcha.isDisplayed();
    }

    public boolean isMfaPageDisplayed() {
        return mfaEnabledPage.isDisplayed();
    }

    public boolean isBotBlockMessageVisible(){
        return botBlockMessageVisible.isDisplayed();
    }
}