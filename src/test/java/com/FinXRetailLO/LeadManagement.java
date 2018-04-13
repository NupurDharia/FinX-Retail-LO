package com.FinXRetailLO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.read.biff.BiffException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebDriver;

import com.tavant.base.DriverFactory;
import com.tavant.kwutils.CustomStep;
import com.tavant.utils.TwfException;

public class LeadManagement extends CustomStep {
	WebDriver driver;

	/**
	 * This is for navigating to Lead Management Pipeline
	 * 
	 * @author nupur.dharia
	 * @throws Exception
	 */

	public void navigateToLeadManagement() throws Exception {
		driver = DriverFactory.getDriver();

		try {

			Util.clickOnElementWhenElementIsReady(getElementByUsing("Lead_Link"));
			waitForPageToLoad();
			if (!getElementByUsing("LeadManagementHeader").isDisplayed()) {
				addExceptionToReport(
						"There was a loading issue in Lead Management Pipeline.",
						"", "");
			}
		} catch (Exception e) {
		}
	}

	/**
	 * This is to connect to DB, sort the collections in Descending order and
	 * get the most recent entry in Leads then click on it
	 * 
	 * @author nupur.dharia
	 * @param args
	 * @throws Exception
	 */
	public void clickOnMostRecentlyCreatedLeadID(String args) throws Exception {
		driver = DriverFactory.getDriver();
		String[] dbDetails = args.split(";");

		Map map = new HashMap<String, String>();
		map = DBUtils.sortDocsInDescNreturnTopMostResult(dbDetails[0],
				dbDetails[1], dbDetails[2]);
		try {
			if (map.containsKey("leadId")) {

				String leadID = String.valueOf(map.get("leadId"));
				System.out.println(leadID);
				getElementByUsing("LM_LeadNoORBorrName").sendKeys(leadID);
				getElementByUsing("LM_Go").click();
				Thread.sleep(5000);
				Util.clickOnElementWhenElementIsReady(driver.findElement(By
						.xpath("//a[contains(text(),'" + map.get("leadId")
								+ "')]")));

				if (!getElementByUsing(
						"//h2[contains(text()," + map.get("leadId") + ")]")
						.isDisplayed()) {
					addExceptionToReport(
							"There was a loading issue in Lead Details Screen",
							"", "");

				}

			}
		} catch (Exception e) {
			System.out.println(e);
			addExceptionToReport("Issue in finding lead ID", "", "");
		}

	}

	/**
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws BiffException
	 * @throws TwfException
	 * 
	 * 
	 */
	public void verifyLeadDetailsPage(String args) throws BiffException,
			InvalidFormatException, IOException, TwfException {
		String[] dbDetails = args.split(";");
		String leadDetailsScreenValue;
		
		
		Map<String, String> map = new HashMap<String, String>();
		map = DBUtils.sortDocsInDescNreturnTopMostResult(dbDetails[0],
				dbDetails[1], dbDetails[2]);
		
		
		for (String key : map.keySet()) {
			if (!key.equalsIgnoreCase("followUpDate")
					&& !key.equalsIgnoreCase("createdDate")
					&& !key.equalsIgnoreCase("srcCreatedDate")
					&& !key.equalsIgnoreCase("_id")) {
				leadDetailsScreenValue = driver
						.findElement(By.id(map.get(key))).getText();
				System.out.println("Column Values - " + key);
				System.out.println("DB Value - " + map.get(key));
				System.out.println("Lead Details Value - "
						+ leadDetailsScreenValue);
				if (!map.get(key).equals(leadDetailsScreenValue))
					addExceptionToReport(
							"Lead Details Screen values does not match with DB",
							leadDetailsScreenValue, map.get(key));

			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkPage() {
		// TODO Auto-generated method stub

	}

}
