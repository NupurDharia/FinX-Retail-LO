package com.FinXRetailLO;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import com.tavant.base.DriverFactory;
import com.tavant.kwutils.CustomStep;
import com.tavant.kwutils.KWVariables;

//Unable to read from test data some issue happening with FIRE hence hardcoded the input values
// Tried with Datapool

/**
 * Login to application as Lender Admin
 *
 */
public class LoginPage extends CustomStep {


	/**

     * Logs into the application a POrtal Admin


      * @throws Exception

     */

     public void loginIntoApplication(String args) throws Exception {

           WebDriver driver = DriverFactory.getDriver();
           String data = KWVariables.getVariables().get(args);
           String[] loginCredentials = data.split(":");
           String strUserName = loginCredentials[0];
           String strPassword = loginCredentials[1];
           getElementByUsing("Product_userName").clear();
           getElementByUsing("Product_userName").sendKeys(strUserName);
           getElementByUsing("Product_userName").sendKeys(Keys.TAB);
           getElementByUsing("Product_password").sendKeys(strPassword);
           getElementByUsing("Product_LoginButton").click(); 
           waitForElement(getElementByUsing("Home_WelcomeMessage"), 240);
          
     }



 

	@Override
	public void checkPage() {

	}

}
