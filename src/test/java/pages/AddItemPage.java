package pages;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.TestBase;

public class AddItemPage extends TestBase {

	@FindBy(xpath="//a[text()='Add to cart']")
	WebElement addToCart;
	
	@FindBy(xpath="//a[text()='Home ']")
	WebElement home;
	
	public AddItemPage() {
		PageFactory.initElements(driver,this);
	}
	
	public void toPreview(String category, String itemName) throws InterruptedException{
		extentTest = reports.createTest("Add Item Test");
    	home.click();
    	String currentCategory = "//a[text()='"+category+"']";
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    	Thread.sleep(1000);
    	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='"+category+"']")));
    	driver.findElement(By.xpath(currentCategory)).click();
    	Thread.sleep(1000);
    	String currentItem = "//a[text()='"+itemName+"']";
    	wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='"+itemName+"']")));
    	driver.findElement(By.xpath(currentItem)).click();
    	Thread.sleep(1000);
    	WebElement btn = addToCart;
		wait.until(ExpectedConditions.elementToBeClickable(btn));
		btn.click();
		wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		System.out.println(itemName+","+alert.getText());
		alert.accept();
	}
}