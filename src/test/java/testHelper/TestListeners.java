package testHelper;

import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestListeners implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
    String testName = result.getName();
    System.out.println("Test Failed: " + testName);
    Path screenshotPath = ScreenshotUtils.capture(testName);
    try {
        byte[] data = Files.readAllBytes(screenshotPath);
        Allure.addAttachment("Screenshot-" + testName, "image/png", new ByteArrayInputStream(data), "png");
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
}
