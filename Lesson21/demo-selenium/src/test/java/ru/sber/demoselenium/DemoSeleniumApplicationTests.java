package ru.sber.demoselenium;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DemoSeleniumApplicationTests {

    private WebDriver driver;

    private WebDriver createDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                return new ChromeDriver();
            case "firefox":
                return new FirefoxDriver();

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "firefox"})
    void testTitle(String browser) {
        driver = createDriver(browser);
        driver.get("http://localhost:8080/menu?page=1&sortBy=id&direction=asc");

        assertEquals("Меню", driver.getTitle());
        driver.quit();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "firefox"})
    void testTagH2(String browser) {
        driver = createDriver(browser);
        driver.get("http://localhost:8080/menu?page=1&sortBy=id&direction=asc");

        WebElement header = driver.findElement(By.tagName("h2"));
        assertEquals("Меню Ресторана", header.getText());
        driver.quit();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "firefox"})
    void testButtonПрименить(String browser) {
        driver = createDriver(browser);
        driver.get("http://localhost:8080/menu?page=1&sortBy=id&direction=asc");

        WebElement applyButton = driver.findElement(By.xpath("//button[@type='submit']"));
        assertEquals("Применить", applyButton.getText());
        driver.quit();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "firefox"})
    void testMenuTableExists(String browser) {
        driver = createDriver(browser);
        driver.get("http://localhost:8080/menu?page=1&sortBy=id&direction=asc");

        WebElement table = driver.findElement(By.tagName("table"));
        assertNotNull(table, "Таблица с меню должна существовать на странице.");
        driver.quit();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "firefox"})
    void testWidthСтолбцаНазвание(String browser) {
        driver = createDriver(browser);
        driver.get("http://localhost:8080/menu?page=1&sortBy=id&direction=asc");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        WebElement width2 = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//th[text()='Название']")
        ));
        assertEquals(200, width2.getSize().getWidth());
        driver.quit();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "firefox"})
    void testAddButtonLink(String browser) {
        driver = createDriver(browser);
        driver.get("http://localhost:8080/menu?page=1&sortBy=id&direction=asc");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        WebElement addButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//form/a[contains(@href, '/menu/new')]")
        ));
        assertTrue(addButton.isDisplayed(), "Кнопка 'Добавить' должна быть видима.");
        driver.quit();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chrome", "firefox"})
    void testEditButtonNavigation(String browser) {
        driver = createDriver(browser);
        driver.get("http://localhost:8080/menu?page=1&sortBy=id&direction=asc");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        WebElement editButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(@href, '/menu/') and contains(@href, '/edit')]")
        ));
        editButton.click();
        assertTrue(driver.getCurrentUrl().contains("/edit"), "Должна открыться страница редактирования.");
        driver.quit();
    }

}
