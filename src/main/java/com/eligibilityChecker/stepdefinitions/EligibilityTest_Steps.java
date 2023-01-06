package com.eligibilityChecker.stepdefinitions;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.Reporter;

import com.eligibilityChecker.testRunner.TestRunner;
import com.eligibilityChecker.utilities.CustomizedMethods;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class EligibilityTest_Steps extends TestRunner{

	CustomizedMethods CM=new CustomizedMethods();
	
	@Given("^I launch browser and Navigate to Application$")
	public void launchbrowser(){
		
		driver.get("https://services.nhsbsa.nhs.uk/check-for-help-paying-nhs-costs/start");
		driver.manage().window().maximize();
		Reporter.log("Opened URL : https://services.nhsbsa.nhs.uk/check-for-help-paying-nhs-costs/start");
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h1[text()='Check what help you could get to pay for NHS costs']"))));
		boolean HomePage = driver.findElement(By.xpath("//h1[text()='Check what help you could get to pay for NHS costs']")).isDisplayed();
		if(HomePage){
			Reporter.log("Check what help you could get to pay for NHS cost : Header is Displaying");
		}
		
	}

	public void selectcountry () throws InterruptedException{
		//WebElement whichcountry = driver.findElement(By.xpath("//h1[contains(text(),'Which country do you live in?')]"));
		//Assert.assertTrue(whichcountry.isDisplayed());
		WebElement walesradio = driver.findElement(By.id("label-wales"));
		Reporter.log("Selected Wales for Which country do you live in? Question");

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", walesradio);
		//Assert.assertTrue(walesradio.isDisplayed());
		walesradio.click();
		Thread.sleep(1000);
		driver.findElement(By.id("next-button")).click();
		Reporter.log("Clicked on Next Button");

	}
	
	@When("^I put my circumstances into the Checker tool Scenario1$")
	public void Scenario1() throws InterruptedException, IOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		FullFlow(methodName);
	}

	@When("^I put my circumstances into the Checker tool Scenario2$")
	public void Scenario2() throws InterruptedException, IOException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		FullFlow(methodName);
	}
	
	public void FullFlow(String Scenario) throws InterruptedException, IOException {
		WebElement Start = driver.findElement(By.xpath("//input[@value='Start now']"));
		Reporter.log("Clicked on StartNow Button");
		wait.until(ExpectedConditions.visibilityOf(Start));
		Start.click();
		selectcountry();
		boolean nextavailable=true;
		
		while(nextavailable){
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//h1[@id='question-heading']"))));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.xpath("//h1[@id='question-heading']")));
			String question=driver.findElement(By.xpath("//h1[@id='question-heading']")).getText();
			LinkedHashMap<String, String> testdata=CM.readTestdatawithrowvalue("TestData", Scenario);
			String answer=testdata.get(question.trim());
			if(answer.contains("/")){
				driver.findElement(By.xpath("//input[contains(@id,'dob-day')]")).sendKeys(answer.split("/")[0]);
				driver.findElement(By.xpath("//input[contains(@id,'dob-month')]")).sendKeys(answer.split("/")[1]);
				driver.findElement(By.xpath("//input[contains(@id,'dob-year')]")).sendKeys(answer.split("/")[2]);
			}
			else{
				try{
				driver.findElement(By.xpath("//input[contains(@value,'"+answer+"')]/parent::label")).click();
				}catch(Exception e){
					driver.findElement(By.xpath("//input[contains(@value,'"+answer+"')]/parent::div")).click();
					
				}
					
			}
			Reporter.log("for "+question.trim()+" :: We selected : "+answer);
			Thread.sleep(1000);
			driver.findElement(By.id("next-button")).click();
			Reporter.log("Clicked on Next Button");
			try{
				nextavailable=driver.findElement(By.id("next-button")).isDisplayed();
			}catch(Exception e){
				System.out.println("Next is not available");
				nextavailable=false;
				
			}
		}
	}
	
	
	@Then("^I should get a result of whether I will get help or not$")
	public void CheckResult() throws InterruptedException {

		WebElement results = driver.findElement(By.xpath("//h1[contains(@id,'result-heading')]"));
		System.out.println("********************************************************");
		System.out.println(results);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", results);
		String result=results.getText().trim();
		if(result.contains("You get help with NHS costs")){
			Reporter.log("********** You get help with NHS costs ************");
			System.out.println("PASS:: You will Get help");
		}else{
			System.out.println("FAIL:: You will not get Help");
			Reporter.log("********** You will not get help with NHS costs ************");
			Assert.fail();
		}

	}
	
	
	@Then("^I should get a result of whether I will get help with freecosts$")
	public void CheckResults() throws InterruptedException {

		WebElement results = driver.findElement(By.xpath("//h2[@class='heading-large']"));
		System.out.println("********************************************************");
		System.out.println(results);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", results);
		WebElement done = driver.findElement(By.xpath("//h1[@class='heading-xlarge done-panel']"));
		String result=results.getText().trim();
		String Done= done.getText().trim();
		if(result.contains("You get help with NHS costs")&&Done.contains("Done")){
			Reporter.log("Done ********** You get help with NHS costs ************");
			System.out.println("PASS::Done ** You will Get help");
		}else{
			Reporter.log("Done ********** You will not get help with NHS costs ************");
			Assert.fail();
		}
		List<WebElement> freeoptions= driver.findElements(By.xpath("//h2[text()='You get free:']/following-sibling::ul[1]/li"));
		System.out.println("You get free following items : ");
		Reporter.log("You get free following items : ");
		for(int i=0;i<freeoptions.size();i++){
			String freeoption=freeoptions.get(i).getText().trim();
			System.out.println(freeoption);
			Reporter.log(freeoption+" **********");
		}

	}
	
	
	@Then("^Close Browser$")
	public void CloseBrowser() throws InterruptedException {
		driver.close();
		Reporter.log("Browser Closed");
	}
	
	

	
}
