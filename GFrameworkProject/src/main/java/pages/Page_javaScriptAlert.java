package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.CommonUtils;

public class Page_javaScriptAlert extends CommonUtils {

	public static WebElement jsConfirm_btn() {
		return driver.findElement(By.xpath("//button[@onclick='jsConfirm()']"));
	}
	
	public static WebElement resultText() {
		return driver.findElement(By.id("result"));
	}
}
