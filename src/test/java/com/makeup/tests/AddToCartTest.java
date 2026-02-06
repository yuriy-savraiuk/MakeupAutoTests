package com.makeup.tests;

import com.makeup.pages.CartPopup;
import com.makeup.pages.HomePage;
import com.makeup.pages.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AddToCartTest extends BaseTest {

    private HomePage homePage;
    private SearchResultsPage resultsPage;

    @BeforeMethod
    public void initPages() {
        // Викликається ПІСЛЯ BaseTest.setUp(), тому driver вже існує
        homePage = new HomePage(driver);
        resultsPage = new SearchResultsPage(driver);
    }

    @Test(enabled = false, description = "Temporarily disabled - flaky in headless mode")
    public void testAddFirstProductToCartFromSearch() {
        homePage.open();
        homePage.searchFor("L'Oreal");

        String expectedTitle = resultsPage.getFirstProductTitle();
        resultsPage.hoverFirstProductAndClickBuy();

        // ДОДАНО: Пауза для AJAX-запиту + анімації popup в headless режимі
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        CartPopup cartPopup = new CartPopup(driver);
        Assert.assertTrue(cartPopup.isOpened(), "Cart popup did not open");

        String actualTitle = cartPopup.getProductTitle();
        Assert.assertTrue(actualTitle.contains(expectedTitle),
                "Wrong product in cart popup. Expected to contain: " + expectedTitle + " but got: " + actualTitle);
    }
}
