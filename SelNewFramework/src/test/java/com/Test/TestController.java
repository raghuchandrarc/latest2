package com.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.Utils.GetScreenShot;
import com.Utils.Log4j;
import com.Utils.Resources;
import com.Utils.Xls_Reader;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class TestController extends Resources {

	String TestSuites = TestSuite;
	Xls_Reader s = new Xls_Reader(TestSuite);
	protected String mTestCaseName = "";
	String TestCaseName;
	public static final String FIREFOX = "firefox";
	public static final String IE = "IE";
	public static final String CHROME = "chrome";

	@BeforeClass
	public void initBrowser() throws IOException {
		Initialize();
		selectBrowser(BROWSER_NAME);

	}

	/**
	 * Select the browser on which you want to execute tests
	 **/
	private void selectBrowser(String browserName) {

		switch (browserName) {

		case FIREFOX:

			firefoxProfile();
			break;

		case IE:
			ie();
			break;

		case CHROME:
			chrome();
			break;

		default:
			firefoxProfile();
			break;
		}

	}

	private void chrome() {

		System.setProperty("webdriver.chrome.driver", chromeDriver);

		dr = new ChromeDriver();
		driver = new EventFiringWebDriver(dr);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
	}

	private void ie() {
		DesiredCapabilities returnCapabilities = DesiredCapabilities.internetExplorer();
		returnCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		returnCapabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
		System.setProperty("webdriver.ie.driver", IEDriver);

		dr = new InternetExplorerDriver(returnCapabilities);
		driver = new EventFiringWebDriver(dr);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);

	}

	/**
	 * Firefox profile will help in automatic download of files
	 */
	private void firefoxProfile() {
		System.setProperty("webdriver.gecko.driver", FirefoxDriver);

		FirefoxOptions options = new FirefoxOptions();
		options.addPreference("--log", "trace");
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("moz:firefoxOptions", options);

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList", 1);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document");

		dr = new FirefoxDriver(capabilities);
		driver = new EventFiringWebDriver(dr);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);

	}

	@Test
	public void TestCaseController() throws Exception {
		DOMConfigurator.configure("log4j.xml");

		String TCStatus = "Pass";

		// create ExtentReports and attach reporter(s)
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(TestResults);
		ExtentReports extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		// loop through the test cases
		for (int TC = 2; TC <= SuiteData.getRowCount("TestCases"); TC++) {

			String TestCaseID = SuiteData.getCellData("TestCases", "TCID", TC);
			String RunMode = SuiteData.getCellData("TestCases", "RunMode", TC);
			String Reuse = SuiteData.getCellData("TestCases", "Reuse", TC);
			System.out.println("Reuse TestCases " + Reuse);
			ExtentTest test = extent.createTest(TestCaseID, "");
			this.mTestCaseName = TestCaseID;

			if (RunMode.equals("Y")) {
				String TSStatus = "Pass";

				int rows = TestStepData.getRowCount(TestCaseID);

				System.out.println("First " + TestCaseID);
				String ReuseTestCase = "Reuse_";
				if (TestCaseID.startsWith(ReuseTestCase)) {
					String[] arrSplit = Reuse.split("\\|");
					for (int i = 0; i < arrSplit.length; i++) {
						// System.out.println(arrSplit[i]);
						String ReuseVar2 = arrSplit[i];
						s.writeReuse(arrSplit, reuse);
					}

					// loop through test data
					for (int TD = 2; TD <= rows; TD++) {

						// loop through the test steps
						System.out.println("SuiteData.getRowCount(TestCaseID)" + SuiteData.getRowCount(TestCaseID));

						for (int TS = 2; TS <= SuiteData.getRowCount(TestCaseID); TS++) {

							keyword = SuiteData.getCellData(TestCaseID, "Keyword", TS);
							webElement = SuiteData.getCellData(TestCaseID, "WebElement", TS);
							ProceedOnFail = SuiteData.getCellData(TestCaseID, "ProceedOnFail", TS);
							TSID = SuiteData.getCellData(TestCaseID, "TSID", TS);
							Description = SuiteData.getCellData(TestCaseID, "Description", TS);
							TestDataField = SuiteData.getCellData(TestCaseID, "TestDataField", TS);
							String s1 = TestDataField;

							Log4j.startTestCase(TestCaseID, keyword, webElement, s1);
							String ReuseVar2 = s.searchString(reuse, s1);
							if (ReuseVar2.contains(s1)) {

								TestData = TestStepData.getCellData(TestCaseID, ReuseVar2, TD);
								Method method = Keywords.class.getMethod(keyword);
								TSStatus = (String) method.invoke(method);
								if (TSStatus.contains("Failed")) {
									// take the screenshot
									String filename = TestCaseID + "[" + (TD - 1) + "]" + TSID + "[" + TestData + "]";
									//test.log(Status.FAIL, "--" + TSID + "--" + Description + "--" + TestDataField + "");
									Log4j.error(TestCaseID);

									TCStatus = TSStatus;
									String screenShot = GetScreenShot.capture(driver, filename);
									//test.fail(filename + test.addScreenCaptureFromPath(screenShot));
									test.log(Status.FAIL, "--" + TSID + "--" + Description + "--" + TestDataField + "");
									test.addScreenCaptureFromPath(screenShot);

									if (ProceedOnFail.equals("N")) {
										break;
									}
								} else {
									test.log(Status.PASS, "--" + TSID + "--" + Description + "--" + TestDataField + "");
								}

							}

						}
						Log4j.endTestCase(TestCaseID, TSID);

					}

				}

				else {
					// loop through test data
					for (int TD = 2; TD <= rows; TD++) {

						// loop through the test steps
						System.out.println("SuiteData.getRowCount(TestCaseID)" + SuiteData.getRowCount(TestCaseID));

						for (int TS = 2; TS <= SuiteData.getRowCount(TestCaseID); TS++) {

							keyword = SuiteData.getCellData(TestCaseID, "Keyword", TS);
							webElement = SuiteData.getCellData(TestCaseID, "WebElement", TS);
							ProceedOnFail = SuiteData.getCellData(TestCaseID, "ProceedOnFail", TS);
							TSID = SuiteData.getCellData(TestCaseID, "TSID", TS);
							Description = SuiteData.getCellData(TestCaseID, "Description", TS);
							TestDataField = SuiteData.getCellData(TestCaseID, "TestDataField", TS);

							TestData = TestStepData.getCellData(TestCaseID, TestDataField, TD);
							Log4j.startTestCase(TestCaseID, keyword, webElement, TestData);
							Method method = Keywords.class.getMethod(keyword);
							TSStatus = (String) method.invoke(method);

							if (TSStatus.contains("Failed")) {
								// take the screenshot
								String filename = TestCaseID + "[" + (TD - 1) + "]" + TSID + "[" + TestData + "]";
								//test.log(Status.FAIL, "--" + TSID + "--" + Description + "--" + TestDataField + "");

								TCStatus = TSStatus;

								Log4j.error(TestCaseID);
								String screenShot = GetScreenShot.capture(driver, filename);
								//test.fail(filename + test.addScreenCaptureFromPath(screenShot));
								test.log(Status.FAIL, "--" + TSID + "--" + Description + "--" + TestDataField + "");
								test.addScreenCaptureFromPath(screenShot);

								if (ProceedOnFail.equals("N")) {
									break;
								}
							} else {
								test.log(Status.PASS,
										"--" + TSID + "--" + Description + "--" + TestDataField + "" + TestData + "");
							}

						}
					}
				}
			}
			System.out.println("Testtt " + mTestCaseName);

		}

		extent.flush();

	}

	public static void stack(Exception e) {
		e.printStackTrace();
	}

	@AfterClass
	/*
	 * public void setResultTestName(ITestResult result) { try { BaseTestMethod bm =
	 * (BaseTestMethod) result.getMethod(); Field f =
	 * bm.getClass().getSuperclass().getDeclaredField("m_methodName");
	 * f.setAccessible(true); // f.set(bm, mTestCaseName);
	 * //System.out.println("Testtt " + mTestCaseName); f.set(bm, TestCaseName);
	 * Reporter.log("Test Case Name :" + bm.getMethodName());
	 * 
	 * } catch (Exception ex) { stack(ex); Reporter.log("exception " +
	 * ex.getMessage()); }
	 * 
	 * }
	 */
	public void quitBrowser() {
		System.out.println("In quitBrowser---------------------------");

		driver.quit();
	}

}
