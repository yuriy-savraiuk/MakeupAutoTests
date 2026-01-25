package com.makeup.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.makeup.utils.ConfigReader;
import io.qameta.allure.Step;


import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // 1. Локатори (зберігаємо як поля класу)
    private By searchButtonBy = By.cssSelector("div.search-button");
    private By searchInputBy = By.id("search-input");

    // 2. Конструктор (ми передаємо сюди драйвер з тесту)
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 3. Методи дії (Business Logic)

    @Step("Відкрити головну сторінку")
    public void open() {
        driver.get(ConfigReader.getBaseUrl());
    }

    @Step("Виконати пошук за запитом: {text}")
    public void searchFor(String text) {
        // Клікаємо на лупу
        driver.findElement(searchButtonBy).click();

        // Чекаємо і вводимо текст
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInputBy));
        input.clear();
        input.sendKeys(text);
        input.sendKeys(Keys.ENTER); // Замість submit() можна тиснути Enter
    }
}
