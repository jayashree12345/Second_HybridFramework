package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {

	public static WebDriver driver;  //instance obj of webdriver
	public static Properties conpro;  //ref ob for Property
	//method for launching browser

	public static WebDriver startBrowser() throws Throwable {

		//return type -webdriver  browser launching initiating webdriver obj  level 
		//remaining method  for other method as void 

		conpro = new Properties();
		//oad property file
		conpro.load(new FileInputStream("./PropertyFiles/Environment.properties"));
		if(conpro.getProperty("Browser").equalsIgnoreCase("chrome")) 
		{
			driver= new ChromeDriver();
			driver.manage().window().maximize();
		}
		else if (conpro.getProperty("Browser").equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();

		}

		else 
		{
			try {
				throw new IllegalAccessException("Browser value is not message ");
			}catch(IllegalArgumentException i ) {
				System.out.println(i.getMessage());
			}

		}
		return driver;
	}
	//method for launching 
	public static void openUrl() {
		driver.get(conpro.getProperty("Url"));
	}
	//method for waiting for any webelement
	public static void waitForElement(String LocatorType , String  LocatorValue  , String wait) {
		//use parseint()-- to convert string  to integer 
		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(wait))) ;
		if(LocatorType.equalsIgnoreCase("xpath"))	{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("name")) {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("id")) {
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
		}

	}

	//method for textbox
	public static void typeAction(String LocatorType , String  LocatorValue  , String TestData) {
		if(LocatorType.equalsIgnoreCase("xpath")) {
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
		}

		if(LocatorType.equalsIgnoreCase("name")) {
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
		}

		if(LocatorType.equalsIgnoreCase("id")) {
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
		}
	}
	//method buttons links, radiobutton ,chkboxes and images

	public static void clickAction(String LocatorType, String LocatorValue) {
		if(LocatorType.equalsIgnoreCase("xpath")) {
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("name")) {
			driver.findElement(By.name(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("id")) {
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
		}
	}

	//method for validate title
	public static void validateTitle(String Expected_title) {
		String Actual_Title = driver.getTitle();
		try {
			Assert.assertEquals(Actual_Title, Expected_title, "Title is not matching");
		}catch(AssertionError a)
		{
			System.out.println(a.getMessage());
		}	

	}

	public static void closeBrowser() {
		driver.quit();
	}

	//method for date generate{

	public static String generateDate() {
		Date date = new Date() ;
		DateFormat df= new SimpleDateFormat("YYYY_MM_DD");
		return df.format(date);
	}

	//method for dropdown or listbox

	public static void dropDownAction(String LocatorType, String LocatorValue, String TestData) throws Throwable {
	Thread.sleep(3000);
		if(LocatorType.equalsIgnoreCase("xpath")) {
			//convert testdata  value into Integer Type
			int  value =  Integer.parseInt(TestData);
			Select element  = new Select(driver.findElement(By.xpath(LocatorValue)));
			element.selectByIndex(value);
		}

		if(LocatorType.equalsIgnoreCase("name")) {
			//convert testdata  value into Integer Type
			int  value =  Integer.parseInt(TestData);
			Select element  = new Select(driver.findElement(By.name(LocatorValue)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("id")) {
			//convert testdata  value into Integer Type
			int  value =  Integer.parseInt(TestData);
			Select element  = new Select(driver.findElement(By.id(LocatorValue)));
			element.selectByIndex(value);
		}

	}
	//method to capture stock number into note pad
		public static void captureStock(String LocatorType,String LocatorValue)throws Throwable
		{
			String StockNum ="";
			if(LocatorType.equalsIgnoreCase("xpath"))
			{
				StockNum =driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
			}
			if(LocatorType.equalsIgnoreCase("name"))
			{
				StockNum =driver.findElement(By.name(LocatorValue)).getAttribute("value");
			}
			if(LocatorType.equalsIgnoreCase("id"))
			{
				StockNum =driver.findElement(By.id(LocatorValue)).getAttribute("value");
			}
			//create note pad under CaptureData folder and write stocknum
			FileWriter fw = new FileWriter("./CaptureData/stocknumber.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(StockNum);
			bw.flush();
			bw.close();
		}
		//method validate stconumber in stock table
		public static void stocktable()throws Throwable
		{
			//read stock number from above stocknumber notepad
			FileReader fr = new FileReader("./CaptureData/stocknumber.txt");
			BufferedReader br = new BufferedReader(fr);
			String Exp_data =br.readLine();
			if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
				//clik search panel if search textbox not displayed
				driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
			driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
			driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_data);
			driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
			Thread.sleep(2000);
			String Act_Data =driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
			Reporter.log(Exp_data+"      "+Act_Data,true);
			try {
				Assert.assertEquals(Act_Data, Exp_data, "Stock Number Not Found In Table");
			} catch (AssertionError e) {
				System.out.println(e.getMessage());
			}
			
		}

	
	
	//method for capturing supplier number inot notpad under capture data folder
public static void capturesup(String LocatorType, String LocatorValue) throws Throwable	{
	String supplierNum = "";
	if(LocatorType.equalsIgnoreCase("xpath")) {
		supplierNum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("name")) {
		supplierNum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
	}
	if(LocatorType.equalsIgnoreCase("id")) {
		supplierNum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
	}
	
	FileWriter fw = new FileWriter("./CaptureData/suppliernum.txt");
	BufferedWriter bw = new BufferedWriter(fw);
	bw.write(supplierNum);
	bw.flush();
	bw.close();
}
	//,method for verify suppleir number in supplier table
	
public static void supplierTable() throws Throwable {
	//read supplier number from above note pad
	FileReader fr = new FileReader("./CaptureData/suppliernum.txt");
	BufferedReader br = new BufferedReader(fr);
	String Exp_data =br.readLine();
	if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
	driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_data);
	driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
	Thread.sleep(2000);
	String Act_data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
	Reporter.log(Act_data+"          "+Exp_data,true);
	try {
		Assert.assertEquals(Act_data, Exp_data, "Supplier Number Not Found In table");
	} catch (AssertionError a) {
		System.out.println(a.getMessage());
		
	}
	
}
	//method for capturing customer number into notepad under Capturedata Folder
			public static void capturecus(String LocatorType,String LocatorValue)throws Throwable
			{
				String customerNum="";
				if(LocatorType.equalsIgnoreCase("xpath"))
				{
					customerNum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
				}
				if(LocatorType.equalsIgnoreCase("name"))
				{
					customerNum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
				}
				if(LocatorType.equalsIgnoreCase("id"))
				{
					customerNum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
				}
				FileWriter fw = new FileWriter("./CaptureData/customernum.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(customerNum);
				bw.flush();
				bw.close();
			}
			//method for verify customer number in supplierTable
			public static void customerTable()throws Throwable
			{
				//read supplier number from above note pad
				FileReader fr = new FileReader("./CaptureData/customernum.txt");
				BufferedReader br = new BufferedReader(fr);
				String Exp_data =br.readLine();
				if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
					driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
				driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
				driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_data);
				driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
				Thread.sleep(2000);
				String Act_data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
				Reporter.log(Act_data+"          "+Exp_data,true);
				try {
					Assert.assertEquals(Act_data, Exp_data, "customer Number Not Found In table");
				} catch (AssertionError a) {
					System.out.println(a.getMessage());
					
				}	
}
}
