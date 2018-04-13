package com.FinXRetailLO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.FindElement;

import com.tavant.base.DriverFactory;
import com.tavant.kwutils.CustomStep;

public class LoanSummary extends CustomStep {

	public static String lslabel = "//label[contains(.,'%s')]";
	public static String lsValue = "//*[@id='%s']";
	static String loanNumberLink = "//hyperLink/a[contains(.,'%s')]";

	/**
	 * This method is to verify Loan Summary values with Pipeline
	 * 
	 * @author nupur.dharia
	 * @since 03-Apr-2018
	 * @throws exception
	 */
	public void clickLoanNoNCompareLSvalueswithPipeline() throws Exception {
		WebDriver driver = DriverFactory.getDriver();
		String lsFieldId;

		// Call method to create a map for pipeline columns names and fields ids
		// in Loan Summary screen
		HashMap<String, String> columnNIds = columnNFieldIDsMapping();

		// Call method to store 1st row values of Pipeline in a map for Columns
		// as Keys

		HashMap<String, String> pipelineValues = Pipeline
				.fetchColumnValueFromTable();

		// Click on first Loan # link displayed
		driver.findElement(
				By.xpath(loanNumberLink.replace("%s",
						pipelineValues.get("Loan No")))).click();
		waitForPageToLoad();
		Thread.sleep(10000);

		// Call method to store Loan Summary field values corresponding to their
		// ids as Key
		HashMap<String, String> lsValues = valuesFromLoanSummaryInMap();

		try {

			for (String key : pipelineValues.keySet()) {
				System.out.println("Key - " + key);
				System.out.println("Pipeline value - "
						+ pipelineValues.get(key));

				lsFieldId = columnNIds.get(key);

				System.out.println("Loan Summary Value - "
						+ lsValues.get(lsFieldId));
				if (columnNIds.containsKey(key)) {
					if (!pipelineValues.get(key).equalsIgnoreCase(
							lsValues.get(lsFieldId))) {
						addExceptionToReport(
								"Loan Summary value does not match with Pipeline for field : "
										+ key, lsValues.get(key),
								pipelineValues.get(key));
					}

				}
			}

		} catch (Exception e) {
			addExceptionToReport(
					"Pipeline and Loan Summary data does not match", "", "");
		}

	}

	/**
	 * This is to create a map for Columns Names of Pipeline and Loan Summary
	 * Field Ids
	 * 
	 * @return column names from Pipeline and object ids of Loan Summary fields
	 * 
	 */
	public HashMap<String, String> columnNFieldIDsMapping() throws Exception {
		String str = step.getDataValue("PipelineColumnsNLSFieldIDsMapping");
		String[] columnNIdsmapping = str.split(";");

		HashMap<String, String> columnNIds = new HashMap<String, String>();
		for (int i = 0; i < columnNIdsmapping.length; i++) {
			String[] arrValues = columnNIdsmapping[i].split(":");
			columnNIds.put(arrValues[0], arrValues[1]);
		}

		return columnNIds;
	}

	/**
	 * This method is to store values from Loan Summary screen and returns it in
	 * map
	 * 
	 * @author nupur.dharia
	 * @return map containing field labels as keys and field values as values
	 * @since 03-Apr-2018
	 * @updated 05-Apr-2018
	 * @throws Exception
	 */

	public HashMap<String, String> valuesFromLoanSummaryInMap()
			throws Exception {
		WebDriver driver = DriverFactory.getDriver();
		HashMap<String, String> pipelineNlsFieldMapping = new HashMap<String, String>();

		HashMap<String, String> loanSummaryFields = new HashMap<String, String>();

		// Get ids of fields present in Loan Summary page from Test Data sheet
		String loanSummary = step.getDataValue("LoanSummaryFieldIDs");
		String[] loanSummaryFieldIDs = loanSummary.split(";");

		// Get field value mapping for Pipeline and Loan Summary pages
		String pipelineNLSFieldValues = step
				.getDataValue("LSNPipelineFieldValuesMapping");
		String[] pipelineNLSFieldValuesMapping = pipelineNLSFieldValues
				.split(";");

		for (String value : pipelineNLSFieldValuesMapping) {
			String[] values = value.split(":");
			pipelineNlsFieldMapping.put(values[0], values[1]); // Loan Summary
																// Values as
																// keys and
																// Pipeline
																// values as
																// Value

		}

		int count;
		for (count = 0; count < loanSummaryFieldIDs.length; count++) {
			loanSummaryFields.put(loanSummaryFieldIDs[count], driver
					.findElement(By.id(loanSummaryFieldIDs[count])).getText());

			// Replacing Loan Summary Value with the one displayed in Pipeline
			for (String LSValue : pipelineNlsFieldMapping.keySet()) {

				if (loanSummaryFields.get(loanSummaryFieldIDs[count])
						.equalsIgnoreCase(LSValue))
					loanSummaryFields.put(
							loanSummaryFieldIDs[count],
							loanSummaryFields.get(loanSummaryFieldIDs[count])
									.replace(
											LSValue,
											pipelineNlsFieldMapping
													.get(LSValue)));

			}

		}

		return loanSummaryFields;

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
