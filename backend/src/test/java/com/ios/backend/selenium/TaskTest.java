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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TaskTest {
    public WebDriver driver;
    public WebDriverWait wait;

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\elena\\OneDrive\\Desktop\\TASK-Management-System\\backend\\src\\test\\java\\com\\ios\\backend\\selenium\\drivers\\chromedriver.exe");
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
    public void shouldOpenCreateTaskPage() {
        driver.get("http://localhost:4200/u/prg");
        driver.findElements(By.cssSelector(".list-group a")).get(0).click();
        driver.findElements(By.cssSelector("mat-list-item")).get(1).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        assertEquals(driver.getCurrentUrl(), "http://localhost:4200/t/taskCreate");
    }


    @Test
    public void taskNameIsRequired() {
        driver.get("http://localhost:4200/u/prg");
        driver.findElements(By.cssSelector(".list-group a")).get(0).click();
        driver.findElements(By.cssSelector("mat-list-item")).get(1).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElements(By.tagName("mat-step-header")).get(1).click();
        assertTrue(driver.findElement(By.name("name")).isDisplayed());//stays on same page because name is required
    }

    @Test
    public void shouldCreateNewTask() {
        driver.get("http://localhost:4200/u/prg");
        driver.findElements(By.cssSelector(".list-group a")).get(0).click();
        driver.findElements(By.cssSelector("mat-list-item")).get(1).click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.name("name")).sendKeys("new task");
        driver.findElement(By.className("ql-editor")).sendKeys("This is new task");
        driver.findElements(By.tagName("mat-step-header")).get(1).click();
        driver.findElement(By.id("start")).sendKeys("2021-12-01T08:30");
        driver.findElement(By.id("deadline")).sendKeys("2021-12-31T08:30");
        driver.findElements(By.tagName("mat-step-header")).get(2).click();
        if (driver.findElements(By.className("ui-button-icon-left ui-clickable pi pi-angle-double-right")).size() > 0) {
            driver.findElement(By.className("ui-button-icon-left ui-clickable pi pi-angle-double-right")).click();
        }
        driver.findElements(By.tagName("mat-step-header")).get(3).click();
        driver.findElement(By.id("done")).click();
        driver.get("http://localhost:4200/t/myTasks");
    }

}