package TestHelper;

import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ScreenshotUtils;

public class TestListeners implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getName();

        System.out.println("Test Failed: " + testName);

        ScreenshotUtils.capture(testName); // 📸 Capture screenshot
    }
}
