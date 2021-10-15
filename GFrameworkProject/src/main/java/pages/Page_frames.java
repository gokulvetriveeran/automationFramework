package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.CommonUtils;

public class Page_frames extends CommonUtils {


	public static WebElement iFrameLink() {
		return driver.findElement(By.partialLinkText("iFrame"));
	}
	
	public static WebElement frameElement() {
		return driver.findElement(By.id("mce_0_ifr"));
	}
	
	
	public static WebElement fileMenu() {
		return driver.findElement(By.xpath("//button/span[text()='File']"));
	}
	
	public static WebElement newDocumentMenuItem() {
		return driver.findElement(By.xpath("//div[text()='New document']"));
	}
	
	public static WebElement boldMenuItem() {
		return driver.findElement(By.xpath("//button[@title='Bold']"));
	}
	
	public static WebElement richTextEditor() {
		return driver.findElement(By.id("tinymce"));
	}



}
