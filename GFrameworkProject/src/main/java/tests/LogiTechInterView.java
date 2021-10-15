package tests;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;

import api.APIController;

import java.awt.event.KeyEvent;

import junit.framework.Assert;
import pages.Page_DragAndDrop;
import pages.Page_DynamicLoading;
import pages.Page_Login;
import pages.Page_MultipleWindow;
import pages.Page_frames;
import pages.Page_herokuApp;
import pages.Page_javaScriptAlert;
import utils.CommonUtils;

import org.apache.bcel.generic.NEW;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LogiTechInterView extends CommonUtils {
	@Test
	public void formAuthentication() {

		String dataSheetName = "FormAuth";
		openApp("http://the-internet.herokuapp.com/");
		click(Page_herokuApp.getFormAuthLink(), "Form Authentication Link");

		String userNameVal = getTextContent(Page_Login.getUserName_em(), "UserName");
		String passwordVal = getTextContent(Page_Login.getPassword_em(), "Password");
		writeData2Excel(dataSheetName, 0, 0, userNameVal);
		writeData2Excel(dataSheetName, 0, 1, passwordVal);
		String unameFromXL = readDataFromExcel(dataSheetName, 0, 0);
		String passwordFromXL = readDataFromExcel(dataSheetName, 0, 1);

		type(Page_Login.getUserName_txt(), unameFromXL, "UserName");
		type(Page_Login.getPassword_txt(), passwordFromXL, "Password");
		click(Page_Login.getLogin_btn(), "Login Button");

		String welcomeText = getTextContent(Page_Login.getWelcomeText_h4(), "Welcome Text");
		Assert.assertEquals("Welcome to the Secure Area. When you are done click logout below.", welcomeText);

		click(Page_Login.getLogout_btn(), "Logout Button");

		type(Page_Login.getUserName_txt(), "Invalid", "UserName");
		type(Page_Login.getPassword_txt(), "Invalid", "Password");
		click(Page_Login.getLogin_btn(), "Login Button");

		String errorMsg = getTextContent(Page_Login.getErrorMsg(), "Error Message");
		errorMsg = errorMsg.substring(0, errorMsg.length() - 1).trim();
		Assert.assertEquals("Your username is invalid!", errorMsg);

	}

	@Test
	public void dynamicLoading() {

		openApp("http://the-internet.herokuapp.com/");
		click(Page_herokuApp.getDynamicLoadingLink(), "Dynamic Loading Link");
		click(Page_DynamicLoading.getExample2Link(), "Example 2: Element rendered after the fact Link");
		click(Page_DynamicLoading.getStart_btn(), "Start button");
		addLog("Wait until the Progress bar to invisible");
		waitforInvisibilityOfElement(Page_DynamicLoading.getProgressbar());
		addLog("Progress Bar Invisible");
		String msgAfterProgressText = getTextContent(Page_DynamicLoading.getHelloAfterProgress(),
				"Text after Loading...");

		Assert.assertEquals("Hello World!", msgAfterProgressText);

	}

	@Test
	public void multipleWindows() {

		openApp("http://the-internet.herokuapp.com/");
		click(Page_herokuApp.getMultipleWindowsLink(), "Multiple Windows Link");
		click(Page_MultipleWindow.getClickhere(), "Click here Link");
		String parentWindow = driver.getWindowHandle();
		ArrayList<String> allwindows = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(allwindows.get(1));
		System.out.println("New Tab URL : " + driver.getCurrentUrl());
		logPass("Child window URL : " + driver.getCurrentUrl());
		driver.close();
		driver.switchTo().window(parentWindow);
		addLog("Child window Closed");
		addLog("Switched to Parent Window");
		System.out.println("New Tab URL : " + driver.getCurrentUrl());
		String parentWindowTitle = driver.getTitle();
		logPass("Parent Window Title : " + parentWindowTitle);

	}

	@Test
	public void dragAndDropTest() {
		openApp("http://the-internet.herokuapp.com/");
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);

		click(Page_herokuApp.draganddropLink(), "Drag and Drop Link");

		Actions mouseAction = new Actions(driver);
		String aHeaderB4Move = getTextContent(Page_DragAndDrop.getColumnA_header(),
				"Header of A Column before Drag and Drop");
		String bHeaderB4Move = getTextContent(Page_DragAndDrop.getColumnB_header(),
				"Header of B Column before Drag and Drop");

		Actions actions = new Actions(driver);

		actions.dragAndDrop(Page_DragAndDrop.getColumnA(), Page_DragAndDrop.getColumnB()).perform();
		
//		int xpos=to.getLocation().getX();
//		int ypos=to.getLocation().getY();
//		Thread.sleep(5000);
//		builder.clickAndHold(to).perform();
//		Thread.sleep(5000);
//		builder.moveByOffset(xpos, ypos).perform();
//		Thread.sleep(5000);
//		builder.release().perform();

		String aHeaderafterMove = getTextContent(Page_DragAndDrop.getColumnA_header(),
				"Header of A Column After Drag and Drop");
		String bHeaderafterMove = getTextContent(Page_DragAndDrop.getColumnB_header(),
				"Header of B Column After Drag and Drop");

		if (aHeaderafterMove.equalsIgnoreCase("b")) {
			logPass("Header Validated Sucessfully - Header of A (" + aHeaderafterMove + ") is B");
		} else {
			logFailure("Header Validated - Header of A (" + aHeaderafterMove + ") is not B");
		}

	}

	@Test
	public void iFrameTest() {
		openApp("http://the-internet.herokuapp.com/");
		click(Page_herokuApp.framesLink(), "Frames Link");
		click(Page_frames.iFrameLink(), "iFrame Link");

		click(Page_frames.fileMenu(), "File Menu");
		click(Page_frames.newDocumentMenuItem(), "New Document");
		click(Page_frames.boldMenuItem(), "Bold");
		driver.switchTo().frame("mce_0_ifr");
		type(Page_frames.richTextEditor(), "Type Something...", "Editor");

	}

	@Test
	public void javaScriptAlerts() {
		openApp("http://the-internet.herokuapp.com/");
		click(Page_herokuApp.javaScriptAlertsLink(), "Java Script Link");
		Page_javaScriptAlert.jsConfirm_btn().click();

		Alert alert = driver.switchTo().alert();
		alert.dismiss();

		String actualResultText = getTextContent(Page_javaScriptAlert.resultText(), "Result Text");

		Assert.assertEquals("You clicked: Cancel", actualResultText);
	}

	@Test
	public void apiTest_positive() throws ParseException {
		APIController apiController = new APIController();
		// GET Method - get Booking
		addLog("Initiating Get Booking Request...");
		ArrayList<String> getResponse = apiController.getRequest("/booking");
		String getStatus = getResponse.get(0);
		String getResponsestr = getResponse.get(1);
		if (Integer.parseInt(getStatus) == 200) {
			logPass("HTTP STATUS : " + getStatus + " OK");
			logPass("GET - Response Body : " + getResponsestr);
		} else {
			logFailure("HTTP STATUS : " + getStatus + " ERROR");
			logFailure("GET - Response Body : " + getResponsestr);
		}

		// POST Method - Create Booking
		String createdBookingid = "";
		addLog("Initiating Create Booking Request...");
		JSONObject postReqObj = new JSONObject();
		postReqObj.put("firstname", "Gokul");
		postReqObj.put("lastname", "Selvamani");
		postReqObj.put("totalprice", 111);
		postReqObj.put("depositpaid", true);
		JSONObject subObject = new JSONObject();
		subObject.put("checkin", "2018-01-01");
		subObject.put("checkout", "2019-01-01");
		postReqObj.put("bookingdates", subObject);
		postReqObj.put("additionalneeds", "Breakfast");

		System.out.println("Request String : " + postReqObj.toString());
		addLog("POST - Request Body :" + postReqObj.toString());
		ArrayList<String> postResponse = apiController.postRequest("/booking", postReqObj.toString());
		String postStatus = postResponse.get(0);
		String postResponsestr = postResponse.get(1);
		JSONParser parser = new JSONParser();
		JSONObject responseJson = (JSONObject) parser.parse(postResponsestr);
		createdBookingid = responseJson.get("bookingid").toString();
		addLog("Created Booking id - " + createdBookingid);
		if (Integer.parseInt(postStatus) == 200) {
			logPass("HTTP STATUS : " + postStatus + " OK");
			logPass("POST - Response Body : " + postResponsestr);
		} else {
			logFailure("HTTP STATUS : " + postStatus + " ERROR");
			logFailure("POST - Response Body : " + postResponsestr);
		}

		// PUT Method
		addLog("Update Booking id " + createdBookingid + " Request...");

		JSONObject putReqObj = new JSONObject();
		putReqObj.put("firstname", "Bagu");
		putReqObj.put("lastname", "SelvamaniAdaiyur");
		putReqObj.put("totalprice", 222);
		putReqObj.put("depositpaid", true);
		JSONObject subPUTObject = new JSONObject();
		subPUTObject.put("checkin", "2018-01-01");
		subPUTObject.put("checkout", "2019-01-01");
		putReqObj.put("bookingdates", subPUTObject);
		putReqObj.put("additionalneeds", "Lunch");

		System.out.println("Request String : " + putReqObj.toString());
		addLog("PUT - Request Body :" + putReqObj.toString());
		ArrayList<String> putResponse = apiController.putRequest("/booking/" + createdBookingid, putReqObj.toString());
		String putStatus = putResponse.get(0);
		String putResponsestr = putResponse.get(1);
		if (Integer.parseInt(putStatus) == 200) {
			logPass("HTTP STATUS : " + putStatus + " OK");
			logPass("PUT - ResponseBody : " + putResponsestr);
		} else {
			logFailure("HTTP STATUS : " + putStatus + " ERROR");
			logFailure("PUT - ResponseBody : " + putResponsestr);
		}

		// DELETE Method
		addLog("Deleting Booking id " + createdBookingid + " Request...");

		ArrayList<String> deleteResponse = apiController.deleteRequest("/booking/" + createdBookingid);
		String delStatus = deleteResponse.get(0);
		String delResponsestr = deleteResponse.get(1);
		if (Integer.parseInt(delStatus) == 201) {
			logPass("HTTP STATUS : " + delStatus + " OK");
			logPass("PUT - ResponseBody : " + delResponsestr);
		} else {
			logFailure("HTTP STATUS : " + delStatus + " ERROR");
			logFailure("PUT - ResponseBody : " + delResponsestr);
		}

	}

	@Test
	public void apiTest_negative() throws ParseException {
		APIController apiController = new APIController();
		// GET Method - get Booking
		addLog("NEGATIVE - Initiating Get Booking Request...");
		ArrayList<String> getResponse = apiController.getRequest("/booking");
		String getStatus = getResponse.get(0);
		String getResponsestr = getResponse.get(1);
		if (Integer.parseInt(getStatus) == 200) {
			logPass("HTTP STATUS : " + getStatus + " OK");
			logPass("GET - Response Body : " + getResponsestr);
		} else {
			logFailure("HTTP STATUS : " + getStatus + " ERROR");
			logFailure("GET - Response Body : " + getResponsestr);
		}
		String expectedbookingid = "111111";
		if (getResponsestr.contains(expectedbookingid)) {
			addLog("PASS : " + expectedbookingid + " Expected Booking id is present in the response");
		} else {
			addLog("FAILED : " + expectedbookingid + " Expected Booking id is NOT present in the response");
		}

		// POST Method - Create Booking
		String createdBookingid = "";
		addLog("NEGATIVE - Initiating Create Booking Request...");
		JSONObject postReqObj = new JSONObject();
		postReqObj.put("firstname", "Gokul");
		postReqObj.put("lastname", "Selvamani");
		postReqObj.put("totalprice", 111);
		postReqObj.put("depositpaid", true);
		JSONObject subObject = new JSONObject();
		subObject.put("checkin", "2018-01-01");
		subObject.put("checkout", "2019-01-01");
		postReqObj.put("bookingdates", subObject);
		postReqObj.put("additionalneeds", "Breakfast");

		System.out.println("Request String : " + postReqObj.toString());
		addLog("POST - Request Body :" + postReqObj.toString());
		ArrayList<String> postResponse = apiController.postRequest("/booking", postReqObj.toString());
		String postStatus = postResponse.get(0);
		String postResponsestr = postResponse.get(1);
		JSONParser parser = new JSONParser();
		JSONObject responseJson = (JSONObject) parser.parse(postResponsestr);
		createdBookingid = responseJson.get("bookingid").toString();
		addLog("Created Booking id - " + createdBookingid);
		if (Integer.parseInt(postStatus) == 200) {
			logPass("HTTP STATUS : " + postStatus + " OK");
			logPass("POST - Response Body : " + postResponsestr);
		} else {
			logFailure("HTTP STATUS : " + postStatus + " ERROR");
			logFailure("POST - Response Body : " + postResponsestr);
		}
		String expectedBookingId = "111111";

		if (postResponsestr.contains(expectedBookingId)) {
			addLog("PASS : " + expectedBookingId + " Expected Booking id is present in the response");
		} else {
			addLog("FAILED : " + expectedBookingId + " Expected Booking id is NOT present in the response");
		}

		// PUT Method
		addLog("NEGATIVE - Update Booking id " + createdBookingid + " Request...");

		JSONObject putReqObj = new JSONObject();
		putReqObj.put("firstname", "Bagu");
		putReqObj.put("lastname", "SelvamaniAdaiyur");
		putReqObj.put("totalprice", 222);
		putReqObj.put("depositpaid", true);
		JSONObject subPUTObject = new JSONObject();
		subPUTObject.put("checkin", "2018-01-01");
		subPUTObject.put("checkout", "2019-01-01");
		putReqObj.put("bookingdates", subPUTObject);
		putReqObj.put("additionalneeds", "Lunch");

		System.out.println("Request String : " + putReqObj.toString());
		addLog("PUT - Request Body :" + putReqObj.toString());
		ArrayList<String> putResponse = apiController.putRequest("/booking/" + createdBookingid, putReqObj.toString());
		String putStatus = putResponse.get(0);
		String putResponsestr = putResponse.get(1);
		if (Integer.parseInt(putStatus) == 200) {
			logPass("HTTP STATUS : " + putStatus + " OK");
			logPass("PUT - ResponseBody : " + putResponsestr);
		} else {
			logFailure("HTTP STATUS : " + putStatus + " ERROR");
			logFailure("PUT - ResponseBody : " + putResponsestr);
		}

		String expectedName = "SIX";

		if (putResponsestr.contains(expectedName)) {
			addLog("PASS : " + expectedName + " Expected Name is present in the response");
		} else {
			addLog("FAILED : " + expectedName + " Expected Name is NOT present in the response");
		}

		// DELETE Method
		addLog("NEGATIVE - Deleting Booking id " + createdBookingid + " Request...");

		ArrayList<String> deleteResponse = apiController.deleteRequest("/booking/" + createdBookingid);
		String delStatus = deleteResponse.get(0);
		String delResponsestr = deleteResponse.get(1);
		if (Integer.parseInt(delStatus) == 201) {
			logPass("HTTP STATUS : " + delStatus + " OK");
			logPass("PUT - ResponseBody : " + delResponsestr);
		} else {
			logFailure("HTTP STATUS : " + delStatus + " ERROR");
			logFailure("PUT - ResponseBody : " + delResponsestr);
		}
		String expectedResponse = "Deleted";

		if (delResponsestr.contains(expectedResponse)) {
			addLog("PASS : " + expectedResponse + " Expected Response is present in the response");
		} else {
			addLog("FAILED : " + expectedResponse + " Expected Response is NOT present in the response");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
