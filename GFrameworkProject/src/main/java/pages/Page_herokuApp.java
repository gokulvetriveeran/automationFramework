package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.CommonUtils;

public class Page_herokuApp extends CommonUtils {

	public static WebElement getFormAuthLink() {
		return driver.findElement(By.partialLinkText("Form Authentication"));
	}

	public static WebElement getDynamicLoadingLink() {
		return driver.findElement(By.partialLinkText("Dynamic Loading"));
	}
	
	public static WebElement getMultipleWindowsLink() {
		return driver.findElement(By.partialLinkText("Multiple Windows"));
	}
	
	public static WebElement draganddropLink() {
		return driver.findElement(By.partialLinkText("Drag and Drop"));
	}
	
	public static WebElement framesLink() {
		return driver.findElement(By.partialLinkText("Frames"));
	}
	
	public static WebElement javaScriptAlertsLink() {
		return driver.findElement(By.partialLinkText("JavaScript Alerts"));
	}

}
