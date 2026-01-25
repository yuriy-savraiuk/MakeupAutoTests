package com.makeup.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPopup {

    private WebDriver driver;
    private WebDriverWait wait;

    private By popupRootBy = By.cssSelector("div.popup.cart");
    private By popupHeaderBy = By.cssSelector("div.popup.cart .page-header");

    // Шукаємо опис (L'Oreal Paris...) всередині попапа
    private By productTitleInPopupBy = By.cssSelector("div.popup.cart .product-list__item .product__header-desc");

    public CartPopup(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isOpened() {
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(popupHeaderBy));
        return header.getText().trim().equalsIgnoreCase("Кошик");
    }

    public String getProductTitle() {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(productTitleInPopupBy));
        return title.getText().trim();
    }
}
