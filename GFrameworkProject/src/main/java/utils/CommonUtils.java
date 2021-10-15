package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import frameworkController.Constants;
import frameworkController.GController;

public class CommonUtils extends GController {
	String testDataExcelFile = "";

	public CommonUtils() {
		// TODO Auto-generated constructor stub
		testDataExcelFile = prop.getProperty("testDatafilePath");
	}

	public void openApp(String url) {
		try {
			driver.get(url);
			driver.manage().window().maximize();
			logPass("URL Launched : " + url);
		} catch (Exception e) {
			e.printStackTrace();
			logFailure(e.getMessage());
		}

	}

	public WebElement waitforElementToLoad(WebElement element) {
		try {
			Wait wait = new WebDriverWait(driver, Constants.MAXTIMEOUT);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			// TODO: handle exception
			logFailure(e.getMessage());
		}

		return element;
	}
	
	public WebElement waitforInvisibilityOfElement(WebElement element) {
		try {
			Wait wait = new WebDriverWait(driver, Constants.MAXTIMEOUT);
			wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
		} catch (Exception e) {
			// TODO: handle exception
			logFailure(e.getMessage());
		}

		return element;
	}

	public void click(WebElement element, String logRef) {
		try {
			waitforElementToLoad(element);
			element.click();
			logPass("Clicked: " + logRef);
		} catch (Exception e) {
			e.printStackTrace();
			logFailure(e.getMessage());
		}

	}

	public void type(WebElement element, String text, String logRef) {
		try {
			waitforElementToLoad(element);
			element.sendKeys(text);
			logPass("Typed : " + text + " on " + logRef);
		} catch (Exception e) {
			e.printStackTrace();
			logFailure(e.getMessage());
		}

	}

	public String getTextContent(WebElement element, String logRef) {
		String textContent = "";
		try {
			waitforElementToLoad(element);
			textContent = element.getText();
			logPass("Getting Text Content : " + textContent + " on " + logRef);
		} catch (Exception e) {
			e.printStackTrace();
			logFailure(e.getMessage());
		}
		return textContent;

	}

	public String readDataFromExcel(String sheetName, int row, int col) {
		String reqValue = "";
		File file = null;
		FileInputStream inputStream = null;
		Workbook testDataWB = null;
		try {
			file = new File(testDataExcelFile);
			inputStream = new FileInputStream(file);

			testDataWB = new XSSFWorkbook(inputStream);
			Sheet testDataSheet = testDataWB.getSheet(sheetName);

			int rowCount = testDataSheet.getLastRowNum() - testDataSheet.getFirstRowNum();
			Cell reqCell = testDataSheet.getRow(row).getCell(col);

			DataFormatter dFormatter = new DataFormatter();
			reqValue = dFormatter.formatCellValue(reqCell).toString();
			addLog("Getting Value from excel : " + reqValue);

			inputStream.close();

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			logFailure(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logFailure(e.getMessage());
		}

		return reqValue;

	}

	public void writeData2Excel(String sheetName, int row, int col, String data2Write) {
		File file = null;
		FileInputStream inputStream = null;
		Workbook testDataWB = null;

		try {
			file = new File(testDataExcelFile);
			inputStream = new FileInputStream(file);
			testDataWB = new XSSFWorkbook(inputStream);
			
			Sheet sheet = testDataWB.getSheet(sheetName);
			Cell reqCell =sheet.getRow(row).createCell(col);
			reqCell.setCellValue(data2Write);
			FileOutputStream out = new FileOutputStream(file);
			testDataWB.write(out);
			out.close();
			System.out.println("Data Written Successfully - " + data2Write);
			addLog("Writing Value to excel : " + data2Write);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logFailure(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logFailure(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logFailure(e.getMessage());
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
