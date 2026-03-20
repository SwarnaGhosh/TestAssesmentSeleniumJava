package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

    public void login(String user, String pass) {
        username.sendKeys(user);
        password.sendKeys(pass);
        loginBtn.click();
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
}