package com.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.Utils.Xls_Reader;

import net.sourceforge.htmlunit.corejs.javascript.tools.debugger.Main;

public class Resources {

	public static WebDriver dr;
	public static EventFiringWebDriver driver;
	public static Properties Repository = new Properties();
	public static Xls_Reader SuiteData;
	public static Xls_Reader TestStepData;

	public static String keyword;
	public static String webElement;
	public static String TestDataField;
	public static String TestData;
	public static String ProceedOnFail;
	public static String TSID;
	public static String Description;

	public static File f;
	public static FileInputStream FI;
	public static String TestSuite = System.getProperty("user.dir") + "\\TestSuite&Testcases\\TestSuite1.xlsx";
	public static String InputData = System.getProperty("user.dir") + "\\TestSuite&Testcases\\TestSuite1Data.xlsx";
	public static String objectRepository = System.getProperty("user.dir") + "\\ObjectRepository\\object.properties";
	// public static String objectRepository2=System.getProperty("user.dir") +"\\ObjectRepository\\object.properties";
	public static String reuse = System.getProperty("user.dir") + "\\resources\\Reuse.txt";
	public static String TestResults = System.getProperty("user.dir") + "\\TestResults\\TestRunResults.html";
	public static String chromeDriver = System.getProperty("user.dir") + "\\BrowserDrivers\\chromedriver72.exe";
	public static String IEDriver = System.getProperty("user.dir") + "\\BrowserDrivers\\IEDriverServer32.exe";
	public static String FirefoxDriver = System.getProperty("user.dir") + "\\BrowserDrivers\\geckodriver.exe";
	//public static String BROWSER_NAME = "IE";
	//public static String BROWSER_NAME = "firefox";
	public static String BROWSER_NAME = "chrome";

	public static void Initialize() throws IOException {

		TestStepData = new Xls_Reader(InputData);
		SuiteData = new Xls_Reader(TestSuite);

		f = new File(objectRepository);

		FI = new FileInputStream(f);
		Repository.load(FI);

		/*
		 * f = new File(objectRepository2); FI = new FileInputStream(f);
		 * Repository.load(FI);
		 */
	}

	public static void main(String[] args) throws IOException {
		Initialize();
	}

}
