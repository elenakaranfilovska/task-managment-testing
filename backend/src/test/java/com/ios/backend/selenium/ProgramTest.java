package com.ios.backend.selenium;

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

public class ProgramTest {

    public WebDriver driver;
    public WebDriverWait wait;

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\elena\\OneDrive\\Desktop\\TASK-Management-System\\backend\\src\\test\\java\\com\\ios\\backend\\selenium\\drivers\\chromedriver.exe");
        Capabilities capabilities = new ChromeOptions();
        driver = new ChromeDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        this.wait = new WebDriverWait(driver, 50);
        login();
    }

    public void login() {
        driver.get("http://localhost:4200/login");
        driver.findElement(By.name("username")).sendKeys("ece");
        driver.findElement(By.name("password")).sendKeys("test123");
        WebElement login = driver.findElement(By.cssSelector("input.btn"));
        login.click();
        wait.until(ExpectedConditions.invisibilityOf(login));
    }

    @Test
    public void shouldCreateProgram(){
        driver.get("http://localhost:4200/u/createProgram");
        driver.findElement(By.name("name")).sendKeys("Program No.1");
        driver.findElement(By.className("ql-editor")).sendKeys("This is new program");
        driver.findElements(By.className("nxt-btn")).get(0).click();
        driver.findElement(By.id("done")).click();
        driver.get("http://localhost:4200/u/prg");
    }
}
