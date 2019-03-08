package com.Utils;

/**
 * The TestListener class is used to help in generating html Report
 *
 * 
 */
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

/**
 *
 */
public class TestListener implements ITestListener {
	WebDriver driver = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.ITestListener#onTestFailure(org.testng.ITestResult)
	 * capture the screenshot of a page on failure
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		//driver = Driver.getDriver();
		// Take base64Screenshot screenshot.
		String base64Screenshot = "data:image/png;base64,"
				+ ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

		//ExtentTestManager.getTest().log(LogStatus.FAIL, "Test Failed",
				//ExtentTestManager.getTest().addBase64ScreenShot(base64Screenshot));
		
		printTestResults(result);
		String methodName = result.getName().toString().trim();

	}

	public static String capture(WebDriver driver, String TestCaseId) throws IOException {

		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		// String dest = System.getProperty("user.dir") +
		// "/Reports/ErrorScreenshots/" + TestCaseId +".png";
		String dest = System.getProperty("user.dir") + "/Reports/ErrorScreenshots/" + TestCaseId + ".png";
		File destination = new File(dest);
		FileUtils.copyFile(source, destination);

		return dest;
	}

	

	@Override
	public void onFinish(ITestContext arg0) {
		Reporter.log("About to end executing Test " + arg0.getName(), true);

	}

	@Override
	public void onStart(ITestContext iTestContext) {
		Reporter.log("About to begin executing Test " + iTestContext.getName(), true);
		iTestContext.setAttribute("WebDriver", this.driver);

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
		Reporter.log("About to end executing Test " + iTestResult.getName(), true);

	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		Reporter.log("About to end executing Test " + arg0.getName(), true);
	}

	@Override
	public void onTestStart(ITestResult iTestResult) {
		Reporter.log("About to start executing Test " + iTestResult.getName(), true);
		//ExtentTestManager.startTest(iTestResult.getMethod().getMethodName(), iTestResult.getName());
	}

	@Override
	public void onTestSuccess(ITestResult iTestResult) {

		printTestResults(iTestResult);

		/*ExtentTestManager.getTest().log(LogStatus.PASS,
				"Test  " + iTestResult.getMethod().getMethodName() + "    is passed", "");*/

	}

	private void printTestResults(ITestResult result) {

		if (result.getParameters().length != 0) {

			String params = null;

			for (Object parameter : result.getParameters()) {

				params += parameter.toString() + ",";

			}

		}

		String status = null;

		switch (result.getStatus()) {

		case ITestResult.SUCCESS:

			status = "Pass";

			break;

		case ITestResult.FAILURE:

			status = "Failed";

			break;

		case ITestResult.SKIP:

			status = "Skipped";

		}

		Reporter.log("Test Status: " + status, true);
		Reporter.log("<=========================End Of TestStep=========================>");
		Reporter.log("																		");

	}

}