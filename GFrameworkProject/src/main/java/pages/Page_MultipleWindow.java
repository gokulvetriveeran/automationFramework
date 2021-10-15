package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.CommonUtils;

public class Page_MultipleWindow extends CommonUtils {

	public static WebElement getClickhere() {
		return driver.findElement(By.partialLinkText("Click Here"));
	}

	
}
