package com.unisys.vp.testautomation.selenium.pages.pom;

import static com.unisys.vp.testautomation.selenium.pages.data.FeedbackPageData.*;
import static com.unisys.vp.testautomation.selenium.pages.data.ChangePasswordPageData.ID_CHANGE_PASSWORD_BTN;
import static com.unisys.vp.testautomation.selenium.pages.data.LoginPageData.URL;
import static com.unisys.vp.testautomation.selenium.pages.data.HamburgerMenuData.*;
import static com.unisys.vp.testautomation.selenium.pages.data.LoginPageData.*;
import static com.unisys.vp.testautomation.selenium.pages.data.HomePageData.*;
import static com.unisys.vp.testautomation.selenium.pages.data.ChangeRequestPageData.XPATH_PRINT_ICON;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import com.google.common.base.Function;
import com.google.common.escape.Escapers.Builder;
import com.unisys.vp.reports.TestResult;
import com.unisys.vp.reports.TestStep;

public abstract class Page {

	protected WebDriver driver;
	public TestResult testResult;
	

	protected abstract String getTitle();

	public Page(WebDriver driver, TestResult testResult) {
		this.driver = driver;
		this.testResult = testResult;
	}

	public String getPageTitle() {
		return driver.getTitle();
	}

	
	//isAt - modified by Pramod S
	public boolean isAt() {    
		try{
		Wait<WebDriver> wait = new WebDriverWait(driver, 180);
	    wait.until(new Function<WebDriver, Boolean>() {
	        public Boolean apply(WebDriver driver) {
	            System.out.println("Current Window State       : "
	                + String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
	            return String
	                .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
	                .equals("complete");
	        }
	    });
		boolean verify = StringUtils.equals(getTitle(), driver.getTitle());
		testResult.addTestSteps(
				new TestStep("Verifying at Page Title Expected :" + getTitle() + " Actual : " + driver.getTitle(),
						verify ? "PASSED" : "FAILED"));
		return verify;
		}
		catch(TimeoutException e)
		{boolean verify = StringUtils.equals(getTitle(), driver.getTitle());
		testResult.addTestSteps(
				new TestStep("Verifying at Page Title Expected :" + getTitle() + " Actual : " + driver.getTitle(),
						verify ? "PASSED" : "FAILED"));
		return verify;}
		catch(WebDriverException e)
		{boolean verify = StringUtils.equals(getTitle(), driver.getTitle());
		testResult.addTestSteps(
				new TestStep("Verifying at Page Title Expected :" + getTitle() + " Actual : " + driver.getTitle(),
						verify ? "PASSED" : "FAILED"));
		return verify;}
		
		
	}

	public void goToPage() {
		driver.get(URL);
		testResult.addTestSteps(new TestStep("Loading URL : " + URL, "PASSED"));
	}

	
	protected void findElementByIDAndType(String id, String keys) {
		WebElement element = driver.findElement(By.id(id));
		element.click();
		//element.sendKeys(keys);
		((JavascriptExecutor)this.driver).executeScript("arguments[0].value=arguments[0].value+arguments[1]", element, keys);
		testResult.addTestSteps(new TestStep("Identify with ID : " + id + " and type : " + keys, "PASSED"));
		
	}

	protected void findElementByXpathAndType(String xpath, String keys) {
		WebElement element = driver.findElement(By.xpath(xpath));
		element.click();
		//element.sendKeys(keys);
		((JavascriptExecutor)this.driver).executeScript("arguments[0].value=arguments[0].value+arguments[1]", element, keys);

		testResult.addTestSteps(new TestStep("Identify with Xpath : " + xpath + " and type : " + keys, "PASSED"));
	}
	
	public boolean verifyElementByIDAndType(String elementName, String locator) 
	{
		try{
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath(locator)));
		boolean element = driver.findElement(By.xpath(locator)).isDisplayed();
		testResult.addTestSteps(new TestStep(" Verify the element" + elementName + "with xpath : " + locator,
				element ? "PASSED" : "FAILED"));
		return element;
		}
		catch(org.openqa.selenium.NoSuchElementException e)
		{
			return false;
		}
	}
	
	//isExists - added by Pramod S
	public boolean isExists(String elementName,String path)
	{
		try{
//		WebDriverWait wait = new WebDriverWait(driver,120);   
//		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(path)));
		Boolean isPresent = driver.findElements(By.xpath(path)).size() > 0;
		testResult.addTestSteps(new TestStep(" Verify the element" + elementName + "with xpath : " + path,
				isPresent ? "PASSED" : "FAILED"));
		return isPresent;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public WebElement findElement(By by, int waitTime)
	{
		WebDriverWait wait =new WebDriverWait(driver,waitTime);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}
	public boolean checkIfElementExists(By by ,String elementName)
	{
		try{
		WebDriverWait wait = new WebDriverWait(driver,120);   
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		Boolean isPresent = driver.findElements(by).size() > 0;
		testResult.addTestSteps(new TestStep(" Verify the element" + elementName,
				isPresent ? "PASSED" : "FAILED"));
		return isPresent;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public String getTextUsingID(String locator) {
		WebDriverWait wait = new WebDriverWait(driver,15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator)));
		String textValue = driver.findElement(By.id(locator)).getAttribute("innerHTML");
		testResult.addTestSteps(new TestStep("Identify with id " + locator + "and get text " + textValue, "PASSED"));
		return textValue;
	}
	public String getValueUsingID(String locator) {
		String textValue = driver.findElement(By.id(locator)).getAttribute("value");
		testResult.addTestSteps(new TestStep("Identify with id " + locator + "and get Value " + textValue, "PASSED"));
		return textValue;
	}
	
	public String getTextUsingXPath(String path) {
		WebDriverWait wait = new WebDriverWait(driver,30);
	    WebElement element=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(path)));
		String textValue = element.getText();
		testResult.addTestSteps(new TestStep("Identify with xpath " + path + "and get Value " + textValue, "PASSED"));
		return textValue;
	}
	
	public void findElementByXPathAndClick(String path) {
		WebDriverWait wait = new WebDriverWait(driver,30);
		WebElement element=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(path)));
		element.click();
		testResult.addTestSteps(new TestStep("Identify Element with XPath : " + path + " and Click", "PASSED"));
	}
	public void findElementByXPathAndType(String xpath,String keys)
	{
		WebElement element = driver.findElement(By.xpath(xpath));
		element.click();
		//element.sendKeys(keys);
		((JavascriptExecutor)this.driver).executeScript("arguments[0].value=arguments[0].value+arguments[1]", element, keys);

		testResult.addTestSteps(new TestStep("Identify with Xpath : " + xpath + " and type : " + keys, "PASSED")); 
	}
	//findElementByXpathAndClickUsingJS - added by Pramod S
	public void findElementByXpathAndClickUsingJS(String path) {
		try
		{
		WebDriverWait wait = new WebDriverWait(driver,30);
		WebElement element=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(path)));
		JavascriptExecutor jse = (JavascriptExecutor)driver;    
		jse.executeScript("arguments[0].scrollIntoView(true);", element);
		jse.executeScript("arguments[0].click();",element); 
		testResult.addTestSteps(new TestStep("Identify Element with XPath : " + path + " and Click", "PASSED"));
		}
		catch(org.openqa.selenium.TimeoutException e)
		{
			WebElement element=driver.findElement(By.xpath(path));
			JavascriptExecutor jse = (JavascriptExecutor)driver;    
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
			jse.executeScript("arguments[0].click();",element); 
			testResult.addTestSteps(new TestStep("Identify Element with XPath : " + path + " and Click", "PASSED"));
		}
		catch(org.openqa.selenium.WebDriverException e)
		{
			WebElement element=driver.findElement(By.xpath(path));
			JavascriptExecutor jse = (JavascriptExecutor)driver;    
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
			jse.executeScript("arguments[0].click();",element); 
			testResult.addTestSteps(new TestStep("Identify Element with XPath : " + path + " and Click", "PASSED"));
		}
	}
	//findElementByCSSSelectorAndClick - added by Pramod S
	public void findElementByCSSSelectorAndClick(String path)
	{
		WebElement element = driver.findElement(By.cssSelector(path));
		JavascriptExecutor jse = (JavascriptExecutor)driver;    
//		jse.executeScript("arguments[0].scrollIntoView(true);", element);
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		jse.executeScript("arguments[0].click();",element); 
//		Actions actions = new Actions(driver);
//		actions.moveToElement(element).click().build().perform();
		testResult.addTestSteps(new TestStep("Identify Element with CSS : " + path + " and Click", "PASSED"));
	}

	public void findElementByIDAndClick(String id) 
	{
		WebElement element = driver.findElement(By.id(id));
		Actions act = new Actions(driver);
		act.moveToElement(element).build().perform();
		element.click();
		testResult.addTestSteps(new TestStep("Identify Element with ID : " + id + " and Click", "PASSED"));
	}

	public WebElement findElementById(String id) {
		WebElement element = driver.findElement(By.id(id));
		testResult.addTestSteps(new TestStep("Identify Element with id : " + id, "PASSED")	);
		return element;
	}

	public WebElement findElementByXpath(String xpath) {
		
		WebDriverWait wait = new WebDriverWait(driver,45);
		WebElement element=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		 element = driver.findElement(By.xpath(xpath));
		testResult.addTestSteps(new TestStep("Identify Element with xpath : " + xpath, "PASSED"));
		return element;
	}
	
	
	public boolean checkFieldExist(String id) {
		boolean isExists = true;
		try {
			driver.findElement(By.id(id));
		} catch (NoSuchElementException e) {
			isExists = false;
		}
		testResult.addTestSteps(new TestStep("Identify Element with ID : " + id, isExists ? "PASSED" : "FAILED"));
		return isExists;
	}
	public boolean checkFieldExistUsingXpath(String xpath) {
		boolean isExists = true;
		try {
			driver.findElement(By.xpath(xpath));
		} catch (org.openqa.selenium.NoSuchElementException e) {
			isExists = false;
		}
		testResult.addTestSteps(new TestStep("Identify Element with Xpath : " + xpath, isExists ? "PASSED" : "FAILED"));
		return isExists;
	}

	public boolean enterTextInTextBoxByid(String Locator, String Text ) {
//		System.out.println("Passw");
		driver.findElement(By.id(Locator)).clear();
		driver.findElement(By.id(Locator)).click();
		WebElement element = findElementById(Locator);
	//	driver.findElement(By.id(Locator)).sendKeys(Text);
		((JavascriptExecutor)this.driver).executeScript("arguments[0].value=arguments[0].value+arguments[1]", element, Text);
		testResult.addTestSteps(new TestStep("Successfully entered the text in :" + Locator  , "PASSED" ));
		return true;

	}
	
		public String findColorById(String id) throws InterruptedException {
			Thread.sleep(5000);
		String color = driver.findElement(By.id(id)).getCssValue("background-color");//revert back
		System.out.println("Color of a button before mouse hover: "	+ color);
		Thread.sleep(5000);
		WebElement googleSearchBtn = driver.findElement(By.id(id)); 
		Actions action = new Actions(driver);
		action.moveToElement(googleSearchBtn).build().perform();
		Thread.sleep(5000);
		System.out.println("Color of a button after mouse hover : "	+ googleSearchBtn.getCssValue("color"));
		String[] hexValue = color.replace("rgba(", "").replace(")", "").split(",");

		int hexValue1=Integer.parseInt(hexValue[0]);
		hexValue[1] = hexValue[1].trim();
		int hexValue2=Integer.parseInt(hexValue[1]);
		hexValue[2] = hexValue[2].trim();
		int hexValue3=Integer.parseInt(hexValue[2]);

		String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
		System.out.println(actualColor);
		testResult.addTestSteps(new TestStep("Identify Element with color : " + actualColor, "PASSED"));
		return actualColor;
}
	
	
	public String findColorByXpath(String xpath) {
		String color = driver.findElement(By.xpath(xpath)).getCssValue("color");
		System.out.println("Color of a button before mouse hover: "	+ color);
		WebElement googleSearchBtn = driver.findElement(By.xpath(xpath)); 
		Actions action = new Actions(driver);
		action.moveToElement(googleSearchBtn).perform();
		System.out.println("Color of a button after mouse hover : "	+ googleSearchBtn.getCssValue("color"));
		String[] hexValue = color.replace("rgba(", "").replace(")", "").split(",");

		int hexValue1=Integer.parseInt(hexValue[0]);
		hexValue[1] = hexValue[1].trim();
		int hexValue2=Integer.parseInt(hexValue[1]);
		hexValue[2] = hexValue[2].trim();
		int hexValue3=Integer.parseInt(hexValue[2]);

		String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
		System.out.println(actualColor);
		testResult.addTestSteps(new TestStep("Identify Element with color : " + actualColor, "PASSED"));
		return actualColor;
	}

	public static String toBrowserHexValue(int number) {
		StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
		while (builder.length() < 2) {
		builder.append("0");
		}
		return builder.toString().toUpperCase();
		}
		
	public boolean changeToDefaultUSlang() {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		WebElement element = driver.findElement(By.xpath(XPATH_HAMBURGERMENU));
		jse.executeScript("arguments[0].click();",element); 
		WebElement element1 = driver.findElement(By.xpath(XPATH_HAMBURGERLANGUAGEDROPDOWN));
		jse.executeScript("arguments[0].click();",element1); 
		WebElement element2 = driver.findElement(By.xpath(XPATH_HAMBURGERLANGUAGEDROPDOWNEngUS));
		jse.executeScript("arguments[0].click();",element2); 
		return true; 
	}
	
	public List<String> getListOfItesmFromXPath(String xpath)
	{
		List<WebElement> allElements = driver.findElements(By.xpath(xpath));
		List<String> menuItems = new ArrayList<>();
		for (WebElement element : allElements) {
			System.out.println("option "+element.getAttribute("innerHTML"));
			menuItems.add(element.getAttribute("innerHTML"));
			
		} 
		return menuItems;
	}
	
	protected List<String> getListOfItesmFromSelector(String selector){
		List<WebElement> allElements = driver.findElements(By.cssSelector(selector));
		List<String> menuItems = new ArrayList<>();
		for (WebElement element : allElements) {
			System.out.println(element.getText());
			menuItems.add(element.getText());
		}
		for (WebElement element : allElements) {
			System.out.println(element.getText());
			menuItems.add(element.getText());
		}
		return menuItems;
	}
	
	//isElementPresentByXpath - added by Pramod S
	public boolean isElementPresentByXpath(String Locator) {
	    try 
	    {
	        driver.findElements(By.xpath(Locator));
	        return true;
	    }
	    catch (org.openqa.selenium.NoSuchElementException e) 
	    {
	        return false;
	    }
	}
	
	
	//findElementsByXpath - added by Pramod S
	public List<WebElement> findElementsByXpath(String xpath)
	{
		
		try{WebDriverWait wait = new WebDriverWait(driver,20);   
		List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath))); 
		return elements;}
		catch(TimeoutException e)
		{
			List<WebElement> elements=null;
			return elements;
		}
	}
	
	//clickOnElementUsingJS - added by Pramod S
	public void clickOnElementUsingJS(WebElement element,String name)
	{
		try
		{
//		WebDriverWait wait = new WebDriverWait(driver,30);
		JavascriptExecutor jse = (JavascriptExecutor)driver;    
		jse.executeScript("arguments[0].scrollIntoView(true);", element);
		jse.executeScript("arguments[0].click();",element); 
//		jse.executeScript("arguments[0].scrollOutofView(false);", element);
		testResult.addTestSteps(new TestStep("Identify Element : " + name + " and Click", "PASSED"));
		}
		catch(org.openqa.selenium.TimeoutException e)
		{
			JavascriptExecutor jse = (JavascriptExecutor)driver;    
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
			jse.executeScript("arguments[0].click();",element); 
			testResult.addTestSteps(new TestStep("Identify Element : " + name + " and Click", "PASSED"));
		}
		catch(org.openqa.selenium.WebDriverException e)
		{
			JavascriptExecutor jse = (JavascriptExecutor)driver;    
			jse.executeScript("arguments[0].scrollIntoView(true);", element);
			jse.executeScript("arguments[0].click();",element); 
			testResult.addTestSteps(new TestStep("Identify Element : " + name + " and Click", "PASSED"));
		}
	}
	//findElementByXpathAndClickUsingActions - Pramod S
	public void findElementByXpathAndClickUsingActions(String xpath)
	{
		WebElement element = driver.findElement(By.xpath(xpath));
		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().build().perform();
		testResult.addTestSteps(new TestStep("Identify Element with xpath : " + xpath + " and Click", "PASSED"));
	}
	
	//getTextUsingInnerHtmlAttribute - Pramod S
	public String getTextUsingInnerHtmlAttribute(WebElement element)
	{
		/*WebDriverWait wait = new WebDriverWait(driver,120);   
		wait.until(ExpectedConditions.presenceOfElementLocated(locator))*/
		return element.getAttribute("innerHTML");
	}
	
	//getTextUsingInnerHtmlAttribute - Pramod S
	public String getValueUsingValueAttribute( final WebElement element)
	{
		WebDriverWait wait = new WebDriverWait(driver,120);   
		wait.until(new Function<WebDriver, Boolean>() 
		{
	        public Boolean apply(WebDriver driver) 
	        {
	            return element.isDisplayed();
	        }
	    }); 
	    
		return element.getAttribute("value");
	}
	//getTitleOfCurrentPage - Pramod S
	public String getTitleOfCurrentPage()
	{
		return driver.getTitle();
	}
	//checkIfButtonisEnabled - Pramod S
	public boolean checkIfButtonisEnabled(String xpath) 
	{
		WebDriverWait wait = new WebDriverWait(driver,30);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		boolean res = driver.findElement(By.xpath(xpath)).isEnabled();
		testResult.addTestSteps(new TestStep("Identify Element with xpath " + xpath+" is Enabled:", "PASSED"));
		return res;
	}
	//checkIfButtonisEnabledByID - Pramod S
	public boolean checkIfButtonisEnabledByID(String id) 
	{
		
		try
		{
			WebDriverWait wait = new WebDriverWait(driver,30);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(id)));
		boolean res = driver.findElement(By.xpath(id)).isEnabled();
		new TestStep("BUTTON WITH XPATH "+id+" ENABLED :" ,
				res ? "ENABLED" : "NOT ENABLED");
		return res;
		}
		catch(Exception e)
		{
			boolean res = false;
			System.out.println("BUTTON NOT ENABLED\n");
			new TestStep("BUTTON WITH XPATH "+id+" ENABLED :" ,
					res ? "ENABLED" : "NOT ENABLED");
			return res;
		}
	}
	//findColorOfElementByID - Pramod S
	public String findColorOfElementByID(String id)
	{
		String color = driver.findElement(By.id(id)).getCssValue("color");
		return color;	
	}
	//findBackgroundColorOfElementByID - Pramod S
	public String findBackgroundColorOfElementByID(String id)
	{
		String color = driver.findElement(By.id(id)).getCssValue("background-color");
		return color;	
	}
	// getTextAlignmentOfElementByID - Pramod S
	public String getTextAlignmentOfElementByID(String id)
	{
		String align = driver.findElement(By.id(id)).getCssValue("text-align");
		return align;
	}
	//getTextAlignmentOfElementByXPath - Pramod S
	public String getTextAlignmentOfElementByXPath(String xpath)
	{
		String align = driver.findElement(By.xpath(xpath)).getCssValue("text-align");
		return align;
	}
	//findElementByIDAndClickUsingJS - Pramod S
	public void findElementByIDAndClickUsingJS(String id) 
	{
		WebDriverWait wait = new WebDriverWait(driver,120);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
		WebElement element = driver.findElement(By.id(id));
//		Actions actions = new Actions(driver);
//		actions.moveToElement(element).perform();
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", element);
		jse.executeScript("arguments[0].click();", element);
		testResult.addTestSteps(new TestStep("Identify Element with ID : " + id + " and Click", "PASSED"));
	}

	//checkIfElementIsPresentUsingCSS - Pramod S
	public boolean checkIfElementIsPresentUsingCSS(String path)
	{
		return driver.findElement(By.cssSelector(path)).isDisplayed();
	}

	//verifyElementByID - Pramod S
	public boolean verifyElementByID(String elementName, String locator) 
	{
		boolean element = driver.findElement(By.id(locator)).isDisplayed();
		testResult.addTestSteps(new TestStep(" Verify the element" + elementName + "with xpath : " + locator,
				element ? "PASSED" : "FAILED"));
		return element;
	}
	//verifyElementByName - Pramod S
	public boolean verifyElementByName(String elementName, String locator) 
	{
		boolean element = driver.findElement(By.name(locator)).isDisplayed();
		testResult.addTestSteps(new TestStep(" Verify the element" + elementName + "with xpath : " + locator,
				element ? "PASSED" : "FAILED"));
		return element;
	}
	//getPlaceHolderByID - Pramod S
	public String getPlaceHolderByID(String id) 
	{
		return driver.findElement(By.id(id)).getAttribute("placeholder");
	}

	public boolean verifyElementByCSS(String element_name,String css_path)
	{
		WebDriverWait wait = new WebDriverWait(driver,20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css_path)));
		boolean res = driver.findElement(By.cssSelector(css_path)).isDisplayed();
		testResult.addTestSteps(new TestStep(" Verify the element" + element_name + "with xpath : " + css_path,
				res ? "PASSED" : "FAILED"));
		return res;
	}
	public WebElement findElementByCSS(String css)
	{
		WebDriverWait wait = new WebDriverWait(driver,30);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.cssSelector(css))));
		testResult.addTestSteps(new TestStep("Identify Element with CSS : " + css , "PASSED"));
		return driver.findElement(By.cssSelector(css));//add testresult
	}
	public String getBackgroundColorOfElement(WebElement element)
	{
		return element.getCssValue("background-color");
	}
	public List<WebElement> findElementsByCSS(String css)
	{
//		Wait<WebDriver> wait = new WebDriverWait(driver, 30).pollingEvery(3, TimeUnit.SECONDS);
//		wait.until(new Function<WebDriver, Boolean>() {
//			@Override
//			public Boolean apply(WebDriver driver) {
//				System.out.println("Element state      : "
//						+ driver.findElements(By.cssSelector(css)).size());
//				return driver.findElement(By.cssSelector(css)).isDisplayed();
//			}
//		});
//		WebDriverWait wait = new WebDriverWait(driver,30);
//		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(css)));
		return driver.findElements(By.cssSelector(css));
	}

	public String getElementAttribute(WebElement element,String attribute)
	{
		return element.getAttribute(attribute);
	}
	public String getCssValueOfElement(WebElement element, String css_value)
	{
		return element.getCssValue(css_value);
	}
	
	public String findBackgroundColorByXpath(String xpath) {
		String color = driver.findElement(By.xpath(xpath)).getCssValue("background-color");
		String[] hexValue = color.replace("rgba(", "").replace(")", "").split(",");
		int hexValue1=Integer.parseInt(hexValue[0]);
		hexValue[1] = hexValue[1].trim();
		int hexValue2=Integer.parseInt(hexValue[1]);
		hexValue[2] = hexValue[2].trim();
		int hexValue3=Integer.parseInt(hexValue[2]);
		String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
		System.out.println(actualColor);
		testResult.addTestSteps(new TestStep("Identify Element with color : " + actualColor, "PASSED"));
		return actualColor;
}
	public boolean switchToNewTabandVerify(String url) throws InterruptedException
	{
		
		List<String> browserTabs = new ArrayList<String> (driver.getWindowHandles());
		System.out.println("No of open windows = "+ browserTabs.size());
		driver.switchTo().window(browserTabs .get(1));
		Thread.sleep(2000);
		if(url.equals("null"))
		{
			Assert.assertNotNull(driver.findElement(By.xpath(XPATH_PRINT_ICON)));
			return true;
		}
		System.out.println(driver.getCurrentUrl());
		if(driver.getCurrentUrl().contains(url))
		{
			testResult.addTestSteps(new TestStep("Verified the URL of new window "+url, "PASSED"));
			driver.close();
			driver.switchTo().window(browserTabs .get(0));
			return true;
		}
		return false;
	}
//CSS Function
	public WebElement findElementByCss(String css) {
		WebDriverWait wait = new WebDriverWait(driver,30);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(css)));
		WebElement element = driver.findElement(By.cssSelector(css));
		testResult.addTestSteps(new TestStep("Identify Element with css : " + css, "PASSED"));
		return element;
	}
	public void findElementByCssAndClick(String css) {
		driver.findElement(By.cssSelector(css)).click();
		testResult.addTestSteps(new TestStep("Identify Element with Css : " + css + " and Click", "PASSED"));
	}
	public void findElementByCSSAndType(String css, String keys) {
		WebElement element = driver.findElement(By.cssSelector(css));
		element.click();
		//element.sendKeys(keys);
		((JavascriptExecutor)this.driver).executeScript("arguments[0].value=arguments[0].value+arguments[1]", element, keys);

		testResult.addTestSteps(new TestStep("Identify with CSS : " + css + " and type : " + keys, "PASSED"));
		
	}
	public String getTextUsingCSS(String css) {
		String textValue = driver.findElement(By.cssSelector(css)).getText();
		testResult.addTestSteps(new TestStep("Identify with css " + css + "and get Value " + textValue, "PASSED"));
		return textValue;
	}
	/*public void forceSendKeys(WebElement element, String text)
	{
		if (element != null)
		{
			((JavascriptExecutor) this.driver).executeScript("arguments[0].value=arguments[0].value+arguments[1]", element, text);
		}
	}*/
	public String findColorByCss(String css) {
		String color = driver.findElement(By.cssSelector(css)).getCssValue("color");
		System.out.println("Color of a button before mouse hover: "	+ color);
		WebElement googleSearchBtn = driver.findElement(By.cssSelector(css)); 
		Actions action = new Actions(driver);
		action.moveToElement(googleSearchBtn).perform();
		System.out.println("Color of a button after mouse hover : "	+ googleSearchBtn.getCssValue("color"));
		String[] hexValue = color.replace("rgba(", "").replace(")", "").split(",");

		int hexValue1=Integer.parseInt(hexValue[0]);
		hexValue[1] = hexValue[1].trim();
		int hexValue2=Integer.parseInt(hexValue[1]);
		hexValue[2] = hexValue[2].trim();
		int hexValue3=Integer.parseInt(hexValue[2]);

		String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
		System.out.println(actualColor);
		testResult.addTestSteps(new TestStep("Identify Element with color : " + actualColor, "PASSED"));
		return actualColor;
	}
	public String findBackgroundColorByCss(String css) {
		String color = driver.findElement(By.cssSelector(css)).getCssValue("background-color");
		String[] hexValue = color.replace("rgba(", "").replace(")", "").split(",");
		int hexValue1=Integer.parseInt(hexValue[0]);
		hexValue[1] = hexValue[1].trim();
		int hexValue2=Integer.parseInt(hexValue[1]);
		hexValue[2] = hexValue[2].trim();
		int hexValue3=Integer.parseInt(hexValue[2]);
		String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
		System.out.println(actualColor);
		testResult.addTestSteps(new TestStep("Identify Element with color : " + actualColor, "PASSED"));
		return actualColor;
}

}
