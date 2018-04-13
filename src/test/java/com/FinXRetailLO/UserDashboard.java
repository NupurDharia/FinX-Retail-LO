package com.FinXRetailLO;

import java.io.IOException;

import jxl.read.biff.BiffException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.tavant.base.DriverFactory;
import com.tavant.kwutils.CustomStep;
import com.tavant.utils.TwfException;

public class UserDashboard extends CustomStep {

	/**
	 * This is to verify few elements on Home Screen - present or not.
	 * 
	 * @author nupur.dharia
	 * @throws TwfException
	 * @throws BiffException
	 * @throws IOException
	 */

	public void verifyHomePage() throws TwfException, BiffException,
			IOException {

		WebDriver driver = DriverFactory.getDriver();

		getElementByUsing("Home_Announcements").isDisplayed();
		getElementByUsing("Home_DisabledViewAllLink").isDisplayed();

	}

	/**
	 * This is to verify Branch Name Filter in User Dash board
	 * 
	 * @author nupur.dharia
	 * @throws BiffException
	 * @throws IOException
	 * @throws TwfException
	 * @throws Exception
	 */

	public void verifyUserDashboardForBranchNameFilter() throws BiffException,
			IOException, TwfException, Exception {
		WebDriver driver = DriverFactory.getDriver();
		verifyUserDashboard();

		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView(true);",
				driver.findElement(By.xpath("//input[@id='fieldsDropDown']")));
		driver.findElement(By.xpath("//span[text()='Branch Name']"))
				.isDisplayed();

	}

	/**
	 * This is to verify User Dash board
	 * 
	 * @author nupur.dharia
	 * @throws BiffException
	 * @throws IOException
	 * @throws TwfException
	 * @throws Exception
	 */
	public void verifyUserDashboard() throws BiffException, IOException,
			TwfException, Exception {
		Util.clickOnElementWhenElementIsReady(getElementByUsing("MU_HomeIcon"));
		getElementByUsing("User_Dashboard").isDisplayed();
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
