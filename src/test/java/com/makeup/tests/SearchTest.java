package com.makeup.tests;

import com.makeup.pages.HomePage;
import com.makeup.pages.SearchResultsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SearchTest extends BaseTest {

    private HomePage homePage;
    private SearchResultsPage resultsPage;

    @BeforeMethod
    public void initPages() {
        // Викликається ПІСЛЯ BaseTest.setUp(), тому driver вже існує
        homePage = new HomePage(driver);
        resultsPage = new SearchResultsPage(driver);
    }

    @Test
    public void testSearchFunctionality() {
        // Крок 1: Відкрити сайт
        homePage.open();

        // Крок 2: Зробити пошук
        String query = "L'Oreal";
        homePage.searchFor(query);

        // Крок 3: Перевірити результат
        String actualText = resultsPage.getResultsText().toLowerCase();

        Assert.assertTrue(actualText.contains(query.toLowerCase()),
                "Текст результатів не містить пошукового слова!");
    }

    @Test
    public void testBrandFilter() {
        homePage.open();
        homePage.searchFor("Шампунь");

        String brandToSelect = "Vichy";
        resultsPage.selectBrand(brandToSelect);

        // Перевірка: чи з'явилася чіпса з назвою "Vichy"?
        boolean isApplied = resultsPage.isFilterApplied(brandToSelect);

        Assert.assertTrue(isApplied,
                "Фільтр '" + brandToSelect + "' не був застосований! Чіпса не знайдена.");
    }


    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
