package com.FinXRetailLO;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.tavant.base.DriverFactory;
import com.tavant.kwutils.CustomStep;
import com.tavant.kwutils.Step;

public class Pipeline extends CustomStep {
	static WebDriver driver;

	public static String tableBodyElement = "//div[@class='table-responsive']/table/tbody/tr";
	public static String tableHeaderElement = "//div[@class='table-responsive']/table/thead/tr/th";
	public static String columnElement = "//div[@class='table-responsive']/table/thead/tr/th[%s]";

	/**
	 * This method is to fetch column values of 1st row in a pipeline
	 * 
	 * @author srinivas.g
	 * @since 03-April-2018
	 * @throws Exception
	 */
	public static HashMap<String, String> fetchColumnValueFromTable()
			throws Exception {

		driver = DriverFactory.getDriver();
		HashMap<String, String> columnMap = new HashMap<String, String>();
		try {
			int rowCount = driver.findElements(By.xpath(tableBodyElement))
					.size();
			int colCount = driver.findElements(By.xpath(tableHeaderElement))
					.size();

			for (int colIndex = 1; colIndex <= colCount; colIndex++) {
				String displayedColName = driver.findElement(
						By.xpath(columnElement.replace("%s",
								String.valueOf(colIndex)))).getText();

				String rowValue = fetchRowData(1, colIndex);
				if (rowValue.isEmpty()){
					System.out.println("ENTERED");
						rowValue =  "-";
				}
				System.out.println("Pipeline Row Value - " + rowValue +".");
				columnMap.put(displayedColName, rowValue);
			}
		} catch (Exception e) {
			addExceptionToReport("Zero rows are displayed in Pipeline page",
					"", "");
		}
		
		return columnMap;

	}

	/**
	 * fetchLastRowData
	 * 
	 * @param columnIndex
	 * @return
	 * @author brokerteam
	 * @throws Exception
	 */
	public static String fetchRowData(int rowIndex, int columnIndex)
			throws Exception {
		String tableContentObject = "//div[@class='table-responsive']/table/tbody/tr[%s]/td[%c]";
		String displayedColumnValue = "";
		driver = DriverFactory.getDriver();

		String revisedTableBodyObject = tableContentObject.replace("%s",
				String.valueOf(rowIndex));
		displayedColumnValue = driver.findElement(
				By.xpath(revisedTableBodyObject.replace("%c",
						String.valueOf(columnIndex)))).getText();

		if (!(rowIndex > 0)) {
			addExceptionToReport("Check the Rows Present in the table",
					"Rows are:" + rowIndex, "Rows should be present");
		}

		return displayedColumnValue;

	}

	/**
	 * This method is to navigate to Active Pipeline screen
	 * 
	 * @author brokerteam
	 * @throws Exception
	 */
	public void navigateToActivePipelinePage() throws Exception {
		WebDriver driver = DriverFactory.getDriver();

		try {

			Util.clickOnElementWhenElementIsReady(getElementByUsing("Pipeline_Link"));

		} catch (Exception e) {
		}

		if (getElementByUsing("Pipeline_Active").isDisplayed()
				&& getElementByUsing("Pipeline_Active").isEnabled()) {

			try {
				Util.clickOnElementWhenElementIsReady(getElementByUsing("Pipeline_Active"));

			} catch (Exception e) {
			}
		}
		waitForPageToLoad();
	}

	/**
	 * This is to calculate Loan Summary for Active Pipeline
	 * 
	 * @throws Exception
	 */

	public void calculateLoanAmount() throws Exception {
		String[] loanSummaryValues;
		loanSummaryValues = Util.getSumOfColoumnValues("Loan Amount");
		String loanCountDisplayed = getElementByUsing(
				"Pipeline_ActiveLoanCount").getText();
		String totalLoanAmount = getElementByUsing("Pipeline_ActiveLoanAmount")
				.getText();
		System.out.println("Caluclated Values - " + loanSummaryValues);
		System.out.println("Loan Count = " + loanCountDisplayed);
		System.out.println("Total Loan Amount = " + totalLoanAmount);
		try {

			if ((!loanCountDisplayed.equalsIgnoreCase(loanSummaryValues[0]))
					&& (!totalLoanAmount.equalsIgnoreCase(loanSummaryValues[1]))) {
				addExceptionToReport("Total Loans or Loan Amount is incorrect",
						"Total # of Loans should be " + loanSummaryValues[0]
								+ " and total Loan Amount should be "
								+ loanSummaryValues[1],
						"Values displayed on Loan Summary screen are: Total Loans - "
								+ loanCountDisplayed
								+ " and Total Loan Amount - " + totalLoanAmount);
			}
		} catch (Exception e) {
			addExceptionToReport("Total Loans or Loan Amount is incorrect",
					"Total # of Loans should be " + loanSummaryValues[0]
							+ " and total Loan Amount should be "
							+ loanSummaryValues[1],
					"Values displayed on Loan Summary screen are: Total Loans - "
							+ loanCountDisplayed + " and Total Loan Amount - "
							+ totalLoanAmount);
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
