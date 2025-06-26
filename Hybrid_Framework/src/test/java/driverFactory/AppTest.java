package driverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class AppTest  {

	//all are global variables
	WebDriver driver;
	String Fileinput= "./DataTables/DataEngine.xlsx";
	String Fileoutput = "./DataTables/HybridResults.xlsx";
	ExtentReports reports;  //path of HTML report
	ExtentTest logger; // all status  method  
	String TCSheet = "MasterTestCases";
	
	@Test
	public void startTest() throws Throwable 
	{
		String module_Pass = "";
		String module_Fail = ""; 
	//create object for excelfileutil class
	 
	ExcelFileUtil xl = new ExcelFileUtil(Fileinput);
	//count no of rows in TCsheet
	int rc = xl.rowCount(TCSheet);
	//iterate all rows in TCSheet
	for(int i=1; i <=rc ; i++) {
	if(xl.getCellData(TCSheet, i, 2).equalsIgnoreCase("Y"))	{
		//read module cell into one variable
		String TCModule = xl.getCellData(TCSheet, i, 1); //holding modulenames or sheet name in Data Engine 
		//define path of extent reports
		reports =new ExtentReports("./target/Extentreports/"+ TCModule +FunctionLibrary.generateDate()+ ".html");
		logger = reports.startTest(TCModule);
		logger.assignAuthor("Jayashree");
		for(int j=1 ; j<=xl.rowCount(TCModule); j++) {
			
			//read sll cells from TCModule
			String Description = xl.getCellData(TCModule, j, 0);
			String Object_type = xl.getCellData(TCModule, j, 1);
			String Ltype = xl.getCellData(TCModule, j, 2);
			String Lvalue = xl.getCellData(TCModule, j, 3);
			String TData = xl.getCellData(TCModule, j, 4);
			
			try {
				if(Object_type.equalsIgnoreCase("startBrowser")) {
					driver = FunctionLibrary.startBrowser();
					logger.log(LogStatus.INFO, Description);
				}
				if(Object_type.equalsIgnoreCase("openUrl")) {
					FunctionLibrary.openUrl();
					logger.log(LogStatus.INFO, Description);
				}
				if(Object_type.equalsIgnoreCase("waitForElement")) {
					FunctionLibrary.waitForElement(Ltype, Lvalue, TData);
					logger.log(LogStatus.INFO, Description);
				}
				if(Object_type.equalsIgnoreCase("typeAction")) {
					FunctionLibrary.typeAction(Ltype, Lvalue, TData);	
					logger.log(LogStatus.INFO, Description);
				}
				if(Object_type.equalsIgnoreCase("clickAction")) {
					FunctionLibrary.clickAction(Ltype, Lvalue);	
					logger.log(LogStatus.INFO, Description);
				}
				if(Object_type.equalsIgnoreCase("validateTitle")) {
					FunctionLibrary.validateTitle(TData);
					logger.log(LogStatus.INFO, Description);
				}	
					
				if(Object_type.equalsIgnoreCase("closeBrowser"))	{
					FunctionLibrary.closeBrowser();
					logger.log(LogStatus.INFO, Description);
					
				}
			if(Object_type.equalsIgnoreCase("dropDownAction"))	
			{
				FunctionLibrary.dropDownAction(Ltype, Lvalue, TData);
				logger.log(LogStatus.INFO, Description);
			}
			if(Object_type.equalsIgnoreCase("captureStock")) {
				FunctionLibrary.captureStock(Ltype, Lvalue);
				logger.log(LogStatus.INFO, Description);
			}
			if(Object_type.equalsIgnoreCase("stocktable"))
			{
				FunctionLibrary.stocktable();
				logger.log(LogStatus.INFO, Description);
			}
			if(Object_type.equalsIgnoreCase("capturesup"))
			{
				FunctionLibrary.capturesup(Ltype, Lvalue);
				logger.log(LogStatus.INFO, Description);
			}
			if(Object_type.equalsIgnoreCase("supplierTable"))
			{
				FunctionLibrary.supplierTable();
				logger.log(LogStatus.INFO, Description);
			}
			
			if(Object_type.equalsIgnoreCase("capturecus"))
			{
				FunctionLibrary.capturecus(Ltype, Lvalue);
				logger.log(LogStatus.INFO, Description);
			}
			if(Object_type.equalsIgnoreCase("customerTable"))
			{
				FunctionLibrary.customerTable();
				logger.log(LogStatus.INFO, Description);
			}

			

				
	//write as pass into status cell TCModule
				
				xl.setCellData(TCModule, j, 5, "Pass", Fileoutput);
				logger.log(LogStatus.PASS, Description);
				module_Pass="True";
		
			}catch(Exception e ) {
				System.out.println(e.getMessage());
				//Write as Fail into startus cell TC module
				xl.setCellData(TCModule, j, 5, "Fail", Fileoutput);
				logger.log(LogStatus.FAIL, Description);
				module_Fail="False";
				File screen = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screen, new File("./target/Screenshot/"+ Description + FunctionLibrary.generateDate()+ ".png"));
			
			}
			if(module_Pass.equalsIgnoreCase("True")) {
				//write as Pass into TCSheet 
				xl.setCellData(TCSheet, i, 3, "Pass", Fileoutput);
				
			}if(module_Fail.equalsIgnoreCase("False")) {
				//write as Faile into TCSheet
				xl.setCellData(TCSheet, i, 3, "Fail", Fileoutput);
			}		
		}
		
		reports.endTest(logger);
		reports.flush();
	}else {
		//write as block into status cell in TC HSeet 
		xl.setCellData(TCSheet, i, 3, "Blocked", Fileoutput);
		
	}
		
		
		
		
	}
	
	}	
	
}
