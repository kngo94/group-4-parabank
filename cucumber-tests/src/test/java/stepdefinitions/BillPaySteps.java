package stepdefinitions;

import context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BillPaySteps {
    private final TestContext ctx;

    public BillPaySteps(TestContext ctx) { this.ctx = ctx; }

    @When("I pay {string} to payee {string} with account number {string} verified as {string}")
    public void pay(String amount, String payee, String account, String verify) {
        openBillPay();
        fill("payee.name", payee);
        fill("payee.address.street", "1 Main St");
        fill("payee.address.city", "Melbourne");
        fill("payee.address.state", "VIC");
        fill("payee.address.zipCode", "3000");
        fill("payee.phoneNumber", "0400000000");
        fill("payee.accountNumber", account);
        fill("verifyAccount", verify);
        fill("amount", amount);
        ctx.driver.findElement(By.cssSelector("input[value='Send Payment']")).click();
    }

    @When("I submit the bill payment form with no details")
    public void submitEmpty() {
        openBillPay();
        ctx.driver.findElement(By.cssSelector("input[value='Send Payment']")).click();
    }

    @Then("I should see the bill payment confirmation for {string} and {string}")
    public void confirmation(String payee, String amount) {
        waiter().until(ExpectedConditions.visibilityOfElementLocated(By.id("billpayResult")));
        String text = ctx.driver.findElement(By.id("billpayResult")).getText();
        Assert.assertTrue(text.contains("Bill Payment Complete"));
        Assert.assertTrue(text.contains(payee));
        Assert.assertTrue(text.contains(String.format("$%.2f", Double.parseDouble(amount))));
    }

    @Then("I should see the validation message {string}")
    public void validationMessage(String message) {
        // spans exist but are hidden, so look for a visible one with this text
        By locator = By.xpath("//span[@class='error' and contains(normalize-space(.), \""
                + message + "\")]");
        waiter().until(d -> d.findElements(locator).stream().anyMatch(WebElement::isDisplayed));
    }

    @Then("the bill payment should not be completed")
    public void notCompleted() {
        Assert.assertFalse(ctx.driver.findElement(By.id("billpayResult")).isDisplayed());
    }

    private void openBillPay() {
        ctx.driver.findElement(By.linkText("Bill Pay")).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(By.name("payee.name")));
    }

    private void fill(String name, String value) {
        ctx.driver.findElement(By.name(name)).sendKeys(value);
    }

    private WebDriverWait waiter() {
        return new WebDriverWait(ctx.driver, Duration.ofSeconds(15));
    }
}