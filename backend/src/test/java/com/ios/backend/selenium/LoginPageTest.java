package com.ios.backend.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;

public class LoginPageTest {
    public WebDriver driver;
    public WebDriverWait wait;


    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\elena\\OneDrive\\Desktop\\TASK-Management-System\\backend\\src\\test\\java\\com\\ios\\backend\\selenium\\drivers\\chromedriver.exe");
        Capabilities capabilities = new ChromeOptions();
        driver = new ChromeDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        this.wait = new WebDriverWait(driver, 50);
    }

    @Test
    public void shouldOpenSignUpPage() {
        driver.get("http://localhost:4200/signup");
        WebElement signUpButton = driver.findElement(By.tagName("button"));
        assertEquals(signUpButton.getText(), "Register");
    }

    @Test
    public void shouldShowNameIsRequiredError() {
        driver.get("http://localhost:4200/signup");
        WebElement signUpButton = driver.findElement(By.tagName("button"));
        signUpButton.click();
        String errorMessage = driver.findElements(By.cssSelector("div.error-field")).get(1).getText();
        assertEquals(errorMessage, "Username is required");
    }

    @Test
    public void shouldShowEmailIsNotValidError() {
        driver.get("http://localhost:4200/signup");
        WebElement emailInput = driver.findElement(By.name("email"));
        emailInput.sendKeys("test");//invalid email format
        WebElement signUpButton = driver.findElement(By.tagName("button"));
        signUpButton.click();
        String errorMessage = driver.findElements(By.cssSelector("div.error-field")).get(2).getText();
        assertEquals(errorMessage, "Email must be a valid email address");
    }

    @Test
    public void shouldRegisterSuccessfully() {
        driver.get("http://localhost:4200/signup");
        driver.findElement(By.name("name")).sendKeys("usertest1");
        driver.findElement(By.name("username")).sendKeys("usertest1");
        driver.findElement(By.name("email")).sendKeys("usertest@live.com");
        driver.findElement(By.name("password")).sendKeys("test123");
        WebElement signUpButton = driver.findElement(By.tagName("button"));
        driver.findElement(By.name("email")).click();
        wait.until(ExpectedConditions.visibilityOf(signUpButton));
        wait.until(ExpectedConditions.elementToBeClickable(signUpButton));
        signUpButton.click();
    }

    @Test
    public void login() {
        driver.get("http://localhost:4200/login");
        driver.findElement(By.name("username")).sendKeys("ece");
        driver.findElement(By.name("password")).sendKeys("test123");
        WebElement login = driver.findElement(By.cssSelector("input.btn"));
        login.click();
        wait.until(ExpectedConditions.invisibilityOf(login));
        assertEquals(driver.getCurrentUrl(), "http://localhost:4200/u/prg");
    }

    @After
    public void tearDown() {
        this.driver.close();
    }
}
