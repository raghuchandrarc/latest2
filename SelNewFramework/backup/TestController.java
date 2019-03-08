package com.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.internal.BaseTestMethod;

import com.Data.*;
import com.ReportUtils.GetScreenShot;
import com.ReportUtils.ReportUtil;
import com.Test.Keywords;
import com.Utils.Log4j;
import com.Utils.Resources;
import com.Utils.TestUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.testng.annotations.Parameters;

public class TestController extends Resources {

	//String TestSuite = System.getProperty("user.dir") + "\\TestSuite&Testcases\\TestSuite1.xlsx";
	String TestSuites = TestSuite;
	Xls_Reader s = new Xls_Reader(TestSuite);
	static String reuse = System.getProperty("user.dir") + "\\resources\\Reuse.txt";
	protected String mTestCaseName = "";

	@BeforeClass
	public void initBrowser() throws IOException {
		Initialize();

		/*DesiredCapabilities returnCapabilities = DesiredCapabilities.internetExplorer();
		returnCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		returnCapabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
		String ieDriver = System.getProperty("user.dir") + "\\BrowserDrivers\\IEDriverServer32.exe";
		System.setProperty("webdriver.ie.driver", ieDriver);
		dr = new InternetExplorerDriver(returnCapabilities);*/
		ChromeOptions o = new ChromeOptions();
		o.addArguments("disable-extensions");
		o.addArguments("--start-maximized");
		
	String chDriver = System.getProperty("user.dir") + "\\BrowserDrivers\\chromedriver72.exe";
		System.setProperty("webdriver.chrome.driver", chDriver);
		dr = new ChromeDriver(o);
		
		/*String frDriver = System.getProperty("user.dir") + "\\BrowserDrivers\\geckodriver.exe";
		System.setProperty("webdriver.gecko.driver", frDriver);
		dr = new FirefoxDriver();*/
			
		driver = new EventFiringWebDriver(dr);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);

	}
	@BeforeTest
	
	public void TestSuiteList() {
		
	}

	@Test
	public void TestCaseController() throws Exception {
		DOMConfigurator.configure("log4j.xml");

		String startTime = TestUtils.now("dd.MMMM.yyyy hh.mm.ss aaa");
		//ReportUtil.startTesting(
			//	System.getProperty("user.dir") + "//src//Reports//index.html",
				//startTime, "Test", "1.5");
		//ReportUtil.startSuite("Suite1");
		String TCStatus = "Pass";
		String TestResults = System.getProperty("user.dir") + "\\TestResults\\TestRunResults.html";
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(TestResults);

		// create ExtentReports and attach reporter(s)
		ExtentReports extent = new ExtentReports();

		extent.attachReporter(htmlReporter);

		// loop through the test cases
		for (int TC = 2; TC <= SuiteData.getRowCount("TestCases"); TC++) {

			String TestCaseID = SuiteData.getCellData("TestCases", "TCID", TC);
			String RunMode = SuiteData.getCellData("TestCases", "RunMode", TC);
			String Reuse = SuiteData.getCellData("TestCases", "Reuse", TC);
			System.out.println("Reuuse " + Reuse);
			ExtentTest test = extent.createTest(TestCaseID, "");
			this.mTestCaseName = TestCaseID;


			if (RunMode.equals("Y")) {
				String TSStatus = "Pass";

				int rows = TestStepData.getRowCount(TestCaseID);
				if (rows < 2) {
					rows = 2;
				}
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
							// test.log(Status.PASS, "--"+TSID+"--"+Description+"--"+TestDataField+"");
							

							keyword = SuiteData.getCellData(TestCaseID, "Keyword", TS);
							webElement = SuiteData.getCellData(TestCaseID, "WebElement", TS);
							ProceedOnFail = SuiteData.getCellData(TestCaseID, "ProceedOnFail", TS);
							TSID = SuiteData.getCellData(TestCaseID, "TSID", TS);
							Description = SuiteData.getCellData(TestCaseID, "Description", TS);
							TestDataField = SuiteData.getCellData(TestCaseID, "TestDataField", TS);
							String s1 = TestDataField;
							
							Log4j.startTestCase(TestCaseID,keyword,webElement,s1);
							String ReuseVar2 = s.searchString(reuse, s1);
							// test.log(Status.PASS, "--"+TSID+"--"+Description+"--"+TestDataField+"");
							if (ReuseVar2.contains(s1)) {

								TestData = TestStepData.getCellData(TestCaseID, ReuseVar2, TD);
								// test.log(Status.INFO,
								// "--"+TSID+"--"+Description+"--"+TestDataField+"--"+TestData+"");
								Method method = Keywords.class.getMethod(keyword);
								TSStatus = (String) method.invoke(method);
								if (TSStatus.contains("Failed")) {
									// take the screenshot
									String filename = TestCaseID + "[" + (TD - 1) + "]" + TSID + "[" + TestData + "]";
									test.log(Status.FAIL, "--" + TSID + "--" + Description + "--" + TestDataField + ""
											);
									TestUtils.getScreenShot(filename);
									Log4j.error(TestCaseID);

									TCStatus = TSStatus;
									// report error
									ReportUtil.addKeyword(Description, keyword, TSStatus,
											"Screenshot/" + filename + ".jpg");
									 String screenShotPath = GetScreenShot.capture(driver, "screenShotName");
									 test.fail(filename + test.addScreenCaptureFromPath(screenShotPath));

									if (ProceedOnFail.equals("N")) {
										break;
									}
								} else {
									ReportUtil.addKeyword(Description, keyword, TSStatus, null);
									test.log(Status.PASS, "--" + TSID + "--" + Description + "--" + TestDataField + ""
											);
								}

							}

						}
						Log4j.endTestCase(TestCaseID, TSID);
						//ReportUtil.addTestCase(TestCaseID, startTime, TestUtils.now("dd.MMMM.yyyy hh.mm.ss aaa"),
							//	TCStatus);
						// test.log(Status.PASS, "--"+TSID+"--"+Description+"--"+TestDataField+"");

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
							Log4j.startTestCase(TestCaseID,keyword,webElement,TestData);
							// test.log(Status.INFO,
							// "--"+TSID+"--"+Description+"--"+TestDataField+"--"+TestData+"");
							Method method = Keywords.class.getMethod(keyword);
							TSStatus = (String) method.invoke(method);

							if (TSStatus.contains("Failed")) {
								// take the screenshot
								String filename = TestCaseID + "[" + (TD - 1) + "]" + TSID + "[" + TestData + "]";
								TestUtils.getScreenShot(filename);
								test.log(Status.FAIL, "--" + TSID + "--" + Description + "--" + TestDataField + "");

								TCStatus = TSStatus;
								// report error
								ReportUtil.addKeyword(Description, keyword, TSStatus,
										"Screenshot/" + filename + ".jpg");
								
								Log4j.error(TestCaseID);
								String screenShotPath = GetScreenShot.capture(driver, "screenShotName");
								test.fail(filename + test.addScreenCaptureFromPath(screenShotPath));

								if (ProceedOnFail.equals("N")) {
									break;
								}
							} else {
								ReportUtil.addKeyword(Description, keyword, TSStatus, null);
								test.log(Status.PASS,
										"--" + TSID + "--" + Description + "--" + TestDataField + "" + TestData + "");
							}

							// driver.quit();
						}
						//ReportUtil.addTestCase(TestCaseID, startTime, TestUtils.now("dd.MMMM.yyyy hh.mm.ss aaa"),
								//TCStatus);
					}
				}
				// test.log(Status.PASS, "--"+TSID+"--"+Description+"--"+TestDataField+"");
			}

		}

		//ReportUtil.endSuite();
		
		
		extent.flush();
		//ReportUtil.updateEndTime(TestUtils.now("dd.MMMM.yyyy hh.mm.ss aaa"));

	}
	@AfterClass
	public void setResultTestName(ITestResult result) {
		try {
			BaseTestMethod bm = (BaseTestMethod) result.getMethod();
			Field f = bm.getClass().getSuperclass().getDeclaredField("m_methodName");
			f.setAccessible(true);
			//f.set(bm, mTestCaseName);
			System.out.println("Testtt "+mTestCaseName );
			f.set(bm,mTestCaseName);
			Reporter.log("Test Case Name :" + bm.getMethodName());

		} catch (Exception ex) {
			stack(ex);
			Reporter.log("exception " + ex.getMessage());
		}
		
	}
	public static void stack(Exception e) {
		e.printStackTrace();
	}

	@AfterClass
	public void quitBrowser() {
		System.out.println("In quitBrowser---------------------------");

		driver.quit();
	}

}
