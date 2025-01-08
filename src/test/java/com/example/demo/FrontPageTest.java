package com.example.demo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FrontPageTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testHomePageTitle() {
        driver.get("http://127.0.0.1:5500/index.html");

        String expectedTitle = "Side Hustle Loads";
        String actualTitle = driver.getTitle();
        assertEquals(expectedTitle, actualTitle, "Page title should match the expected title");
    }

    @Test
    public void testLoginButtonRedirects() {
        driver.get("http://127.0.0.1:5500/index.html");

        WebElement loginButton = driver.findElement(By.id("loginButton"));
        loginButton.click();

        String expectedUrl = "http://127.0.0.1:5500/login";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl, "The login button should redirect to the login page");
    }
}
