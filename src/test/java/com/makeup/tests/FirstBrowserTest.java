package com.makeup.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FirstBrowserTest {

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        // Налаштовуємо "Розумне очікування" (Explicit Wait).
        // Ми кажемо драйверу: "Чекай до 10 секунд, поки елемент з'явиться.
        // Якщо з'явиться раніше — йди далі одразу".
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.manage().window().maximize(); // Розгорнемо на весь екран
            driver.get("https://makeup.com.ua/");

            // 1. Знаходимо елемент, який відкриває пошук.
            // На Makeup це часто div з класом "search-button" або саме поле, яке треба "розбудити".
            // Спробуємо клікнути по контейнеру пошуку.
            // (Увага: цей локатор може залежати від версії сайту, спробуємо найбільш вірогідний)
            WebElement searchTrigger = driver.findElement(By.cssSelector("div.search-button"));
            searchTrigger.click();

            // 2. Тепер ЧЕКАЄМО, поки справжнє поле вводу стане видимим і готовим.
            // Використовуємо наш 'wait'.
            WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("search-input")));

            // 3. Тепер вводимо текст
            searchInput.sendKeys("Туш для вій");

            // 4. Натискаємо Enter (submit форми), щоб запустити пошук
            searchInput.submit();

            System.out.println("Пошук виконано успішно!");

            // Пауза, щоб ви побачили результат очима
            Thread.sleep(3000);

        } catch (Exception e) {
            System.out.println("Щось пішло не так: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
