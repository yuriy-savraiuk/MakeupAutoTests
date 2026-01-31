package com.makeup.pages;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class SearchResultsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Локатор блоку з результатами (той самий, що ми знайшли минулого разу)
    private By resultsInfoBy = By.cssSelector("div.search-results.info-text");

    // Локатор карток товарів (знадобиться для підрахунку)
    private By productCardsBy = By.cssSelector("div.simple-slider-list__link"); // Приклад, треба перевірити на сайті

    // Додайте цей локатор вгорі класу SearchResultsPage, де інші поля
    // Він нам знадобиться, щоб перевіряти оновлення списку
    private By brandCheckBoxes = By.cssSelector("div.catalog-filter-list-wrap");

    // Локатор для чіпсів обраних фільтрів
    // Ми беремо текст безпосередньо з лейбла або спана всередині
    private By selectedFiltersBy = By.cssSelector("div.selected-filter-list label.selected-filter-list__item");

    // Контейнер списку товарів
    private By productsContainerBy = By.cssSelector("div.catalog-products");

    // Перший товар (li в ul.simple-slider-list)
    private By firstProductItemBy = By.cssSelector("div.catalog-products ul.simple-slider-list > li");

    // Назва товару всередині першої картки (потрібно інспектнути точніше)
    private By firstProductTitleBy = By.cssSelector("div.catalog-products ul.simple-slider-list > li:first-child .simple-slider-list__description");

    // Кнопка Buy, яка з'являється на hover (у вас опис: div.button.buy)
    private By firstProductBuyButtonBy = By.cssSelector("div.catalog-products ul.simple-slider-list > li:first-child .button.buy");

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Обирає бренд у фільтрі зліва.
     * @param brandName Назва бренду точно так, як вона записана в атрибуті (наприклад "Vichy", "L'Oreal Paris")
     */

    @Step("Вибрати бренд {brandName}")
    public void selectBrand(String brandName) {
        WebElement brandCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("li[data-filter-variant-name='" + brandName + "']")
        ));

        // Скролимо елемент у центр екрану
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'center'});",
                brandCheckbox
        );

        // Пауза після скролу
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Клікаємо через JavaScript (надійніше для headless)
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", brandCheckbox);
    }

    /**
     * Перевіряє, чи активний фільтр з заданою назвою (чи є така чіпса).
     */
    public boolean isFilterApplied(String filterName) {
        // Знаходимо всі активні фільтри
        List<WebElement> filters = driver.findElements(selectedFiltersBy);

        // Проходимось по списку і шукаємо потрібний текст
        for (WebElement filter : filters) {
            if (filter.getText().contains(filterName)) {
                return true; // Знайшли!
            }
        }
        return false; // Не знайшли
    }

    // Метод, що повертає текст результатів ("Знайдено ...")
    public String getResultsText() {
        WebElement infoBlock = wait.until(ExpectedConditions.visibilityOfElementLocated(resultsInfoBy));
        return infoBlock.getText();
    }

    // Метод для отримання кількості знайдених товарів (реальної, на сторінці)
    public int getProductsCount() {
        List<WebElement> products = driver.findElements(productCardsBy);
        return products.size();
    }
    /*
    wait.until(ExpectedConditions.visibilityOfElementLocated(productsContainerBy)); — “страховка”, що блок з товарами вже з’явився і видимий, тобто сторінка результатів пошуку реально завантажила контент, а не тільки HTML-каркас.
    WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(firstProductTitleBy)); — чекає, доки елемент з назвою першого товару стане видимим, і тільки тоді повертає його як WebElement.
    title.getText().trim() — бере видимий текст з елемента (назву) і прибирає зайві пробіли на початку/кінці, щоб порівняння в Assert було стабільніше.
     */

    @Step("Отримати назву першого товару")
    public String getFirstProductTitle() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productsContainerBy));
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(firstProductTitleBy));
        return title.getText().trim();
    }
    /*
    wait.until(...productsContainerBy) — знову переконується, що контейнер з товарами видно (корисно, якщо метод викликається одразу після пошуку/фільтра).​
    firstItem = wait.until(visibilityOfElementLocated(firstProductItemBy)) — знаходить першу карточку товару і чекає, щоб вона стала видимою (інакше hover може не спрацювати).​
    Actions actions = new Actions(driver) — створює інструмент для “складних” дій мишкою/клавіатурою (hover, drag&drop тощо).​
    actions.moveToElement(firstItem).perform() — виконує наведення миші на товар; це потрібно, бо кнопка Buy часто з’являється тільки в стані hover.​
    buyButton = wait.until(elementToBeClickable(...)) — чекає, поки кнопка Buy стане клікабельною (тобто видима і не заблокована), і лише тоді клікає.
     */

    @Step("Навести мишку на перший товар і натиснути Buy")
    public void hoverFirstProductAndClickBuy() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productsContainerBy));

        // Отримуємо ВСІ елементи списку
        List<WebElement> allItems = driver.findElements(
                By.cssSelector("ul.simple-slider-list > li")
        );

        // Шукаємо перший, в якого є кнопка Buy
        WebElement firstValidProduct = null;
        for (WebElement item : allItems) {
            try {
                item.findElement(By.cssSelector(".button.buy"));
                firstValidProduct = item;
                break;
            } catch (NoSuchElementException e) {
                continue;
            }
        }

        if (firstValidProduct == null) {
            throw new AssertionError("Не знайдено жодного товару з кнопкою Buy!");
        }

        // Скролимо до товару
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                firstValidProduct
        );

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Ховер (для GUI режиму)
        Actions actions = new Actions(driver);
        actions.moveToElement(firstValidProduct).perform();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Знаходимо кнопку (вона існує в DOM, але може бути невидимою)
        WebElement buyButton = firstValidProduct.findElement(By.cssSelector(".button.buy"));

        // КЛЮЧОВА ЗМІНА: Робимо її видимою СИЛОЮ через JavaScript
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.visibility = 'visible'; " +
                        "arguments[0].style.display = 'block'; " +
                        "arguments[0].style.opacity = '1';",
                buyButton
        );

        // Клік через JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", buyButton);

        // ДОДАТИ: Чекаємо, поки popup з'явиться (сайту потрібен час на AJAX-запит)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}
