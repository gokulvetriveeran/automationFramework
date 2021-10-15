package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.CommonUtils;

public class Page_Login extends CommonUtils {

	public static WebElement getUserName_txt() {
		return driver.findElement(By.id("username"));
	}

	public static WebElement getPassword_txt() {
		return driver.findElement(By.id("password"));
	}

	public static WebElement getLogin_btn() {
		return driver.findElement(By.xpath("//button[@type='submit']"));
	}
	
	public static WebElement getUserName_em() {
		return driver.findElement(By.xpath("//h4/em[1]"));
	}

	public static WebElement getPassword_em() {
		return driver.findElement(By.xpath("//h4/em[2]"));
	}

	public static WebElement getWelcomeText_h4() {
		return driver.findElement(By.xpath("//h4[@class='subheader']"));
	}

	public static WebElement getLogout_btn() {
		return driver.findElement(By.xpath("//a[@href='/logout']"));
	}

	public static WebElement getErrorMsg() {
		return driver.findElement(By.xpath("//*[@id='flash']"));
	}


}
