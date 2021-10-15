package api;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import frameworkController.GController;

import org.json.*;

public class APIController extends GController {
	static String endpointURL = "";
	static String authUserName = "";
	static String authpassword = "";

	public APIController() {
		
		endpointURL = (String) prop.get("endpointURL");
		authUserName=(String) prop.get("authUserName");
		authpassword=(String) prop.get("authPassword");
		
	}

	public static ArrayList<String> httpRequest(String reqType, String pathURL, String reqBody) {
		HttpGet httpget = null;
		HttpPost httpPost = null;
		ArrayList<String> responseList = new ArrayList<String>();

		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

			switch (reqType) {
			case "GET": {
				responseList = getRequest(pathURL);
				break;
			}
			case "POST": {
				responseList = postRequest(pathURL, reqBody);
				break;
			}
			case "PUT": {
				responseList = putRequest(pathURL, reqBody);
				break;
			}
			case "DELETE": {
				responseList = deleteRequest(pathURL);
				break;
			}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseList;
	}

	public static ArrayList<String> getRequest(String pathURL) {
		ArrayList<String> getResponseList = new ArrayList<String>();
		String responseBody = "";
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpGet httpget = new HttpGet(endpointURL + pathURL);
			httpget.setHeader("Accept", "application/json");
			httpget.setHeader("Content-type", "application/json");

			System.out.println("Executing GET request " + httpget.getRequestLine());

			ResponseHandler<String> responseHandler = response -> {
				int status = response.getStatusLine().getStatusCode();
				getResponseList.add(String.valueOf(status));
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Error : " + status);
				}
			};
			responseBody = httpclient.execute(httpget, responseHandler);

			System.out.println("Response : " + responseBody);
			getResponseList.add(responseBody);	

		} catch (Exception e) {
			e.printStackTrace();

		}
		return getResponseList;
	}

	public static ArrayList<String> postRequest(String pathURL, String reqBody) {
		ArrayList<String> postResponseList = new ArrayList<String>();
		String responseBody = "";
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(endpointURL + pathURL);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			StringEntity stringEntity = new StringEntity(reqBody);
			httpPost.setEntity(stringEntity);

			System.out.println("Executing request " + httpPost.getRequestLine());

			// Create a custom response handler
			ResponseHandler<String> responseHandler = response -> {
				int status = response.getStatusLine().getStatusCode();
				postResponseList.add(String.valueOf(status));
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Error : " + status);
				}
			};
			responseBody = httpclient.execute(httpPost, responseHandler);

			System.out.println("Response : " + responseBody);
			postResponseList.add(responseBody);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return postResponseList;
	}

	public static ArrayList<String> putRequest(String pathURL, String reqBody) {
		ArrayList<String> putResponseList = new ArrayList<String>();
		String responseBody = "";
		String authToken = createAuthToken("/auth",authUserName,authpassword);
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpPut httpPut = new HttpPut(endpointURL + pathURL);
			httpPut.setHeader("Accept", "application/json");
			httpPut.setHeader("Content-type", "application/json");
			httpPut.setHeader("Cookie", "token=" + authToken);

			StringEntity stringEntity = new StringEntity(reqBody);
			httpPut.setEntity(stringEntity);

			System.out.println("Executing request " + httpPut.getRequestLine());

			ResponseHandler<String> responseHandler = response -> {
				int status = response.getStatusLine().getStatusCode();
				putResponseList.add(String.valueOf(status));
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Error : " + status);
				}
			};
			responseBody = httpclient.execute(httpPut, responseHandler);

			System.out.println("Response : " + responseBody);
			putResponseList.add(responseBody);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return putResponseList;
	}

	public static ArrayList<String> deleteRequest(String pathURL) {
		ArrayList<String> deleteResponseList = new ArrayList<String>();
		String responseBody = "";
		String authToken = createAuthToken("/auth",authUserName,authpassword);
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpDelete httpDelete = new HttpDelete(endpointURL + pathURL);
			httpDelete.setHeader("Accept", "application/json");
			httpDelete.setHeader("Content-type", "application/json");
			httpDelete.setHeader("Cookie", "token=" + authToken);


			System.out.println("Executing request " + httpDelete.getRequestLine());

			ResponseHandler<String> responseHandler = response -> {
				int status = response.getStatusLine().getStatusCode();
				deleteResponseList.add(String.valueOf(status));
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Error : " + status);
				}
			};
			responseBody = httpclient.execute(httpDelete, responseHandler);

			System.out.println("Response : " + responseBody);
			deleteResponseList.add(responseBody);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return deleteResponseList;
	}

	public static String createAuthToken(String pathURL, String userName, String password) {
		String authToken = "";
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(endpointURL + pathURL);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			JSONObject reqBody=new JSONObject();
			reqBody.put("username", userName);
			reqBody.put("password", password);
			
			StringEntity stringEntity = new StringEntity(reqBody.toString());
			httpPost.setEntity(stringEntity);

			System.out.println("Executing request " + httpPost.getRequestLine());

			ResponseHandler<String> responseHandler = response -> {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Error : " + status);
				}
			};
			String responseBody = httpclient.execute(httpPost, responseHandler);
			JSONParser parser = new JSONParser();
			JSONObject responseJson = (JSONObject) parser.parse(responseBody);
			authToken = responseJson.get("token").toString();
			System.out.println("token " + authToken);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return authToken;
	}

	public static void main(String args[]) {
//		createAuthToken();
//		deleteBooking();
//		getBooking();
	}

}
