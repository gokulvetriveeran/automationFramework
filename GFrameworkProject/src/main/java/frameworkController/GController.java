package frameworkController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import report.Reporter;

public class GController extends Reporter {

	public String browser = "";

	protected GController() {
		super();
	}

	public String getBrowser() {
		return prop.getProperty("browserName").toString();
	}

	@BeforeMethod
	public void startMethod(Method m) {
		String driverPath = "";
		this.browser = getBrowser();
		if (this.browser.equalsIgnoreCase("chrome")) {
			driverPath = prop.getProperty("chromeDriverPath").toString();
			System.setProperty("webdriver.chrome.driver", driverPath);
			this.driver = new ChromeDriver();

		} else if (this.browser.equalsIgnoreCase("ie")) {
			driverPath = prop.getProperty("ieDriverPath").toString();
			System.setProperty("webdriver.ie.driver", driverPath);
			this.driver = new InternetExplorerDriver();

		} else if (this.browser.equalsIgnoreCase("firefox")) {
			driverPath = prop.getProperty("firefoxDriverPath").toString();
//		System.setProperty("webdriver.gecko.driver", driverPath);
			this.driver = new FirefoxDriver();
		}
		super.test = report.createTest(m.getName());
		System.out.println("DRIVER PATH : " + driverPath);

	}

	@BeforeSuite
	public void setUp() {
		String reportFilePath = prop.getProperty("htmlReportPath");
		htmlReporter = new ExtentHtmlReporter(reportFilePath);
		report = new ExtentReports();
		report.attachReporter(htmlReporter);
		System.out.println("Report Initiated=======>");

		report.setSystemInfo("OS", prop.getProperty("OS"));
		report.setSystemInfo("Browser", prop.getProperty("browserName"));

		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setDocumentTitle("LogiTech - interview");
		htmlReporter.config().setReportName("LogiTech Test Automation Report");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.STANDARD);
	}

	@AfterMethod
	public void getResult(ITestResult result) {
		try {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - FAILED",
					ExtentColor.RED));
			String screenShotName = captureScreen();
			test.addScreenCaptureFromPath("screenshots/" + screenShotName, "FAILED");
			test.fail(result.getThrowable().getMessage());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " - PASSED", ExtentColor.GREEN));
		} else {
			test.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " - SKIPPED", ExtentColor.ORANGE));
			test.skip(result.getThrowable().getMessage());
		}
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally
		{
			report.flush();
			this.driver.close();
		}
	}

	@AfterSuite
	public void tearDown() {
//		report.flush();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
