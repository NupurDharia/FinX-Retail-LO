package com.FinXRetailLO;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//import com.BrokerPortal.MainParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.tavant.base.DriverFactory;
import com.tavant.kwutils.CustomStep;
import com.thoughtworks.selenium.webdriven.commands.IsElementPresent;

public class Util extends CustomStep {
	static WebDriver driver;
	static String colClick = "//th[contains(.,'%s')]";

	/**
	 * it will check if element is ready for click
	 * 
	 * @author From Broker Team
	 * 
	 */
	public static WebElement clickOnElementWhenElementIsReady(
			final WebElement locator) throws Exception {
		WebDriver driver = DriverFactory.getDriver();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(600, TimeUnit.SECONDS)
				.pollingEvery(400, TimeUnit.MILLISECONDS)
				.ignoring(Exception.class);

		WebElement element = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {

				locator.click();
				return locator;
			}
		});

		return element;
	};

	private static boolean isPageIsDisplayed(int pageNumber) {
		String obj = "//div[@class='paginator']//a[text()='%s']";
		try {
			if (driver.findElement(
					By.xpath(obj.replace("%s", String.valueOf(pageNumber))))
					.isDisplayed()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Method Name: Gives the Sum of column values
	 * 
	 * @param column_name
	 * @param
	 * @return strLoansValues
	 */

	public static String[] getSumOfColoumnValues(String column_name)
			throws Exception {

		driver = DriverFactory.getDriver();
		Float sumAllResult = (float) 0.0;
		int loanCount = 0;

		// System.out.println("column_name " + column_name);
		Util.clickOnElementWhenElementIsReady(driver.findElement(By
				.xpath(colClick.replace("%s", String.valueOf(column_name)))));
		Thread.sleep(5000);
		// System.out.println("Done");
		List<String> actual = new ArrayList<String>();
		List<String> compare = new ArrayList<String>();
		// div[@class='paginator']/span[@class='active']/a[contains(.,'184')]
		String activeElement = "//div[@class='paginator']/span[@class='active']/a[contains(.,'%s')]";
		int pageNumber = 2;
		while (isPageIsDisplayed(pageNumber)) {
			System.out.println("pagenUMBER>>>" + pageNumber);
			driver.findElement(
					By.xpath("//div[@class='paginator']//a[text()="+ pageNumber + "]")).click();
			waitForPageToLoad();
			actual = verifyTableColumnValues(column_name, "Test");
			pageNumber = pageNumber + 1;
		}

		
		System.out.println("actual--> " + actual);
		compare.addAll(actual);
		System.out.println("compare--> " + compare);
		loanCount = compare.size();
		String totalLoanAmount = calculateSumOfLoanValues(compare);
		totalLoanAmount = "$" + totalLoanAmount.replace(" ", "");
		System.out.println("Loan Sum--> " + totalLoanAmount);
		String[] strLoansValues = { String.valueOf(loanCount), totalLoanAmount };

		return strLoansValues;

	}

	/**
	 * Method Name: Gives the Sum of column values
	 * 
	 * @param column_name
	 * @param
	 * @return strLoansValues
	 */

	public static String[] getSumOfColoumnValuesOld(String column_name)
			throws Exception {

		driver = DriverFactory.getDriver();
		Float sumAllResult = (float) 0.0;
		int loanCount = 0;

		// System.out.println("column_name " + column_name);
		Util.clickOnElementWhenElementIsReady(driver.findElement(By
				.xpath(colClick.replace("%s", String.valueOf(column_name)))));
		Thread.sleep(5000);
		// System.out.println("Done");
		List<String> actual = new ArrayList<String>();
		List<String> compare = new ArrayList<String>();
		// div[@class='paginator']/span[@class='active']/a[contains(.,'184')]
		String activeElement = "//div[@class='paginator']/span[@class='active']/a[contains(.,'%s')]";

		while (driver.findElements(By.xpath(".//*[@class='paginator']")).size() > 0) {

			actual = verifyTableColumnValues(column_name, "Test");
			// System.out.println("actual--> " + actual);
			compare.addAll(actual);
			Util.clickOnElementWhenElementIsReady(driver.findElement(By
					.xpath(".//*[@class='paginator']")));

		}
		actual = verifyTableColumnValues(column_name, "Test");
		System.out.println("actual--> " + actual);
		compare.addAll(actual);
		System.out.println("compare--> " + compare);
		loanCount = compare.size();
		String totalLoanAmount = calculateSumOfLoanValues(compare);
		totalLoanAmount = "$" + totalLoanAmount.replace(" ", "");
		System.out.println("Loan Sum--> " + totalLoanAmount);
		String[] strLoansValues = { String.valueOf(loanCount), totalLoanAmount };

		return strLoansValues;

	}

	public static List<String> verifyTableColumnValues(
			String expectedColumnName, String expectedColumnValue)
			throws Exception {
		String tableHeaderObject = "//div[@class='table-responsive']/table/thead/tr/th";
		String columnObject = "//div[@class='table-responsive']/table/thead/tr/th[%s]";
		String tableBodyObject = "//div[@class='table-responsive']/table/tbody/tr";
		List<String> columnValuesList = new ArrayList<String>();
		HashMap<String, Integer> columnMap = new HashMap<String, Integer>();

		WebDriver driver = DriverFactory.getDriver();
		int columnCount = driver.findElements(By.xpath(tableHeaderObject))
				.size();

		for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
			String displayedColName = driver.findElement(
					By.xpath(columnObject.replace("%s",
							String.valueOf(colIndex)))).getText();
			if (displayedColName.length() > 0) {

				columnMap.put(displayedColName, colIndex);
			}
		}

		columnValuesList = fetchAllColumnValues(tableBodyObject,
				columnMap.get(expectedColumnName));
		// compareColumnValues(columnValuesList,expectedColumnName);
		return columnValuesList;
	}

	public static String calculateSumOfLoanValues(List<String> loanList) {

		Iterator<String> loan = loanList.iterator();
		DecimalFormat df = new DecimalFormat("#.00");
		Long total = 0L;
		while (loan.hasNext()) {
			total = total
					+ Long.parseLong(loan.next().replaceAll(",", "")
							.replace("$", "").trim());
		}
		return convertLongTokMBT(total);
	}

	public static List<String> fetchAllColumnValues(String tableBodyObject,
			int columnIndex) throws Exception {
		String tableContentObject = "//div[@class='table-responsive']/table/tbody/tr[%s]/td[%c]";
		List<String> columnValuesList = new ArrayList<String>();
		driver = DriverFactory.getDriver();

		int rowCount = driver.findElements(By.xpath(tableBodyObject)).size();

		for (int rowIndex = 1; rowIndex <= rowCount; rowIndex++) {
			String revisedTableBodyObject = tableContentObject.replace("%s",
					String.valueOf(rowIndex));
			String displayedColumnValue = driver.findElement(
					By.xpath(revisedTableBodyObject.replace("%c",
							String.valueOf(columnIndex)))).getText();
			columnValuesList.add(displayedColumnValue);

			System.out
					.println("displayedColumnValue   " + displayedColumnValue);

		}

		return columnValuesList;
	}

	public static String convertLongTokMBT(Long totalLoan) {
		if (totalLoan < 1000) {
			return "" + totalLoan;
		}
		int exp = (int) (Math.log(totalLoan) / Math.log(1000));
		return String.format("%.2f %c", totalLoan / Math.pow(1000, exp),
				"kMBT".charAt(exp - 1));
	}

	// public String fetchPropertyAddressFormEncompass(String loanNumber) throws
	// Exception {
	//
	// // 11+12+14+15+13
	// guiIdPayload1 = guiIdPayload1.replace("$", loanNumber);
	// String address = "";
	// ResponseEntity<?> responseEntity = postRequest(guiIDurl, guiIdPayload1);
	//
	// if (responseEntity.getStatusCode() != HttpStatus.OK) {
	// addExceptionToReport("Fetching GUID From Encompass failed for Loan Number "
	// + loanNumber,
	// String.valueOf(responseEntity.getStatusCode()), "200");
	// }
	//
	// ObjectMapper objectMapper = new ObjectMapper();
	// objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
	// false);
	// try {
	//
	// MainParser mp =
	// objectMapper.readValue(responseEntity.getBody().toString(),
	// MainParser.class);
	// objectMapper.readValue(responseEntity.getBody().toString(),
	// MainParser.class);
	//
	// address = mp.getLoans().get(0).getaddress11() == null ? address
	// : address + " " + mp.getLoans().get(0).getaddress11().trim();
	// address = mp.getLoans().get(0).getaddress12() == null ? address
	// : address + " " + mp.getLoans().get(0).getaddress12().trim();
	// address = mp.getLoans().get(0).getaddress14() == null ? address
	// : address + " " + mp.getLoans().get(0).getaddress14().trim();
	// address = mp.getLoans().get(0).getaddress15() == null ? address
	// : address + " " + mp.getLoans().get(0).getaddress15().trim();
	// address = mp.getLoans().get(0).getaddress13() == null ? address
	// : address + " " + mp.getLoans().get(0).getaddress13().trim();
	//
	// return address.trim();
	//
	// } catch (Exception e) {
	// addExceptionToReport("Failed to fetch Property Address from Encompass for loan number "
	// + loanNumber,
	// e.toString(), "");
	// }
	//
	// return null;
	// }

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
