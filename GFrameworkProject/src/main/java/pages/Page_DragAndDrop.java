package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.CommonUtils;

public class Page_DragAndDrop extends CommonUtils {

	public static WebElement getColumnA() {
		return driver.findElement(By.id("column-a"));
	}

	public static WebElement getColumnB() {
		return driver.findElement(By.id("column-b"));
	}
	
	public static WebElement getColumnA_header() {
		return driver.findElement(By.xpath("//*[@id='column-a']/header"));
	}
	public static WebElement getColumnB_header() {
		return driver.findElement(By.xpath("//*[@id='column-b']/header"));
	}
	
	

}
