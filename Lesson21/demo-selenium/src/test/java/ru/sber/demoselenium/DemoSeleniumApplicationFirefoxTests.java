package ru.sber.demoselenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DemoSeleniumApplicationFirefoxTests {

    public static final int WIDTH_COLUMN_TITLE = 200;
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        // Указываем путь к драйверу FirefoxDriver
        driver = new FirefoxDriver();
        driver.get("http://localhost:8080/menu?page=1&sortBy=id&direction=asc");
    }

    @Test
    public void testTitle() {
        assertEquals("Меню",
                driver.getTitle()); //Меню
    }

    @Test
    public void testTagH2() {
        // Проверяем заголовок h2
        WebElement header = driver.findElement(By.tagName("h2"));
        assertEquals("Меню Ресторана", header.getText());
    }

    @Test
    public void testButtonПрименить() {
        // Проверяем кнопку "Применить"
        WebElement applyButton = driver.findElement(By.xpath("//button[@type='submit']"));
        assertEquals("Применить", applyButton.getText());
    }

    @Test
    void testMenuTableExists() {
        // Проверяем, что на странице есть таблица
        WebElement table = driver.findElement(By.tagName("table"));
        assertNotNull(table, "Таблица с меню должна существовать на странице.");
    }

    @Test
    void testWidthСтолбцаНазвание() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

        WebElement width = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//th[text()='Название']")
        ));
        System.out.println(width.getSize().getWidth());
        assertEquals(WIDTH_COLUMN_TITLE, width.getSize().getWidth());
    }


    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
