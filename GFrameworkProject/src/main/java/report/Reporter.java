package report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import frameworkController.Constants;

public class Reporter {
	public static WebDriver driver;
	protected ExtentReports report;
	protected ExtentTest test;
	protected ExtentHtmlReporter htmlReporter;
	protected Properties prop;

	public Reporter() {
		// TODO Auto-generated constructor stub
		prop = new Properties();
		try {
			prop.load(new FileInputStream(Constants.propFileName));
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addLog(String log) {

		try {
			test.log(Status.INFO, MarkupHelper.createLabel(log, ExtentColor.BLUE));
			String screenShotName = captureScreen();
			test.addScreenCaptureFromPath("screenshots/" + screenShotName, log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void logPass(String log) {

		try {
			test.log(Status.PASS, MarkupHelper.createLabel(log, ExtentColor.GREEN));
			String screenShotName = captureScreen();
			test.addScreenCaptureFromPath("screenshots/" + screenShotName, log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void logFailure(String log) {

		try {
			test.log(Status.FAIL, MarkupHelper.createLabel("FAILED : " + log, ExtentColor.RED));
			String screenShotName = captureScreen();
			test.addScreenCaptureFromPath("screenshots/" + screenShotName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String captureScreen() throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String screenshotName = "screenshot" + dateName + ".png";
		String destination = prop.getProperty("screenshotPath") + screenshotName;
		File finalDestination = new File(destination);
		FileHandler.copy(source, finalDestination);
		return screenshotName;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
