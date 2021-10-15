package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.CommonUtils;

public class Page_DynamicLoading extends CommonUtils {

	
	public static WebElement getExample2Link() {
		return driver.findElement(By.partialLinkText("Example 2: Element rendered after the fact"));
	}
	
	public static WebElement getStart_btn() {
		return driver.findElement(By.xpath("//div[@id='start']/button"));
	}
	
	public static WebElement getProgressbar() {
		return driver.findElement(By.xpath("//div[@id='loading']"));
	}
	
	public static WebElement getHelloAfterProgress() {
		return driver.findElement(By.xpath("//div[@id='finish']/h4"));
	}
	


}
