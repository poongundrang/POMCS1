package testScripts;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import base.TestBase;
import commonUtils.Utilitys;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.AddItemPage;
import pages.DeleteItemPage;
import pages.HomePage;
import pages.LoginPage;
import pages.PurchaseItems;

public class PlaceOrderTest extends TestBase {
	
	HomePage HomePage;
	LoginPage LoginPage;
	
	@BeforeTest
	public void setup() {
		initialize();
	}
	
    @Test(priority=1)
    public void login() {
    	HomePage = new HomePage();
    	LoginPage = HomePage.toLogin();
    	LoginPage.toCategories("Poongundran", "Atman");
    }
    
    @Test(priority=2,dataProvider = "items")
    public void addItems(String category, String itemName) throws InterruptedException {
    	AddItemPage addItemPage = new AddItemPage();
		addItemPage.toPreview(category, itemName);
    }
    
    @Test(priority=3)
    public void deleteItems() throws InterruptedException {
    	DeleteItemPage deleteItemPage = new DeleteItemPage();
		deleteItemPage.deleteItem();
    }
    
    @Test(dependsOnMethods="deleteItems")
    public void purchaseItems() throws InterruptedException {
    	PurchaseItems purchaseItems = new PurchaseItems();
		purchaseItems.purchaseItem("Poongundran", "India", "VPM", "7867867860", "OCT", "2024");
    }
    
    public static void initialize() {
		String strBrowser = prop.getProperty("browser");
		if(strBrowser.equalsIgnoreCase("chrome")){
			  WebDriverManager.chromedriver().setup();
			  driver = new ChromeDriver();
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
		driver.get(prop.getProperty("Url"));
	}
    
    @DataProvider(name="items")								
    public Object[][] getData() throws CsvValidationException, IOException{
  	  String path = System.getProperty("user.dir")
  			  +"//src//test//resources//configFiles//items.csv";
  	  String[] cols;											
  	  CSVReader reader = new CSVReader(new FileReader(path));
  	  ArrayList<Object> dataList = new ArrayList<Object>();
  	  while((cols = reader.readNext())!=null){					
  		  Object[] record = {cols[0],cols[1]};					
  		  dataList.add(record);									
  	  }	  
  	  return dataList.toArray(new Object[dataList.size()][]);
    }
    
     @AfterMethod()
	 public void tearDown(ITestResult result) {
		 if(ITestResult.FAILURE == result.getStatus()) {
			 extentTest.log(Status.FAIL, result.getThrowable().getMessage());
			 String strPath = Utilitys.getScreenShotPath(driver);
			 extentTest.addScreenCaptureFromPath(strPath);
		 }
	 }
     
     @AfterTest
     public void closeApp() {
    	 driver.close();
    	 reports.flush();
     }
}