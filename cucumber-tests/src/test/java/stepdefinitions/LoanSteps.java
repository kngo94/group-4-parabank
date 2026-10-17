package stepdefinitions;

import context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoanSteps {
    private final TestContext ctx;

    public LoanSteps(TestContext ctx) { this.ctx = ctx; }

    @When("I apply for a loan of {string} with a down payment of {string}")
    public void apply(String loan, String down) {
        ctx.driver.findElement(By.linkText("Request Loan")).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(By.id("downPayment")));
        waiter().until(d -> !d.findElements(By.cssSelector("#fromAccountId option")).isEmpty());
        ctx.driver.findElement(By.id("amount")).sendKeys(loan);
        ctx.driver.findElement(By.id("downPayment")).sendKeys(down);
        ctx.driver.findElement(By.cssSelector("input[value='Apply Now']")).click();
    }

    @Then("I should see the loan status {string}")
    public void status(String expected) {
        waiter().until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("rightPanel"), "Loan Request Processed"));
        String panel = ctx.driver.findElement(By.id("rightPanel")).getText();
        Assert.assertTrue("Expected status " + expected + " but page said: " + panel,
                panel.contains("Status: " + expected));
    }

    @Then("I should see the loan message {string}")
    public void message(String expected) {
        String panel = ctx.driver.findElement(By.id("rightPanel")).getText();
        Assert.assertTrue(panel.contains(expected));
    }

    @Then("I should see an error and no loan decision")
    public void errorNoDecision() {
        waiter().until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Error!"));
        String body = ctx.driver.findElement(By.tagName("body")).getText();
        Assert.assertFalse(body.contains("Loan Request Processed"));
        Assert.assertFalse(body.contains("Approved"));
        Assert.assertFalse(body.contains("Denied"));
    }

    private WebDriverWait waiter() {
        return new WebDriverWait(ctx.driver, Duration.ofSeconds(15));
    }
}