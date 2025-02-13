package ru.sber.demoselenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DemoSeleniumApplicationChromeTests {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        // Указываем путь к драйверу ChromeDriver
        driver = new ChromeDriver();
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
        assertEquals(200, width.getSize().getWidth());
    }

    @Test
    void testAddButtonLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

        // Ждем, пока body загрузится полностью
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        // Затем ищем кнопку "Добавить"
        WebElement addButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//form/a[contains(@href, '/menu/new')]")
        ));

        assertTrue(addButton.isDisplayed(), "Кнопка 'Добавить' должна быть видима.");
    }


    @Test
    void testEditButtonNavigation() {
        // Находим первую кнопку "Редактировать" (если записи есть)
        WebElement editButton = driver.findElement(By.xpath("//a[contains(@href, '/menu/') and contains(@href, '/edit')]"));

        // Кликаем по ней
        editButton.click();

        // Ожидаем, что URL изменился на страницу редактирования
        assertTrue(driver.getCurrentUrl().contains("/edit"), "Должна открыться страница редактирования.");
    }


    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
