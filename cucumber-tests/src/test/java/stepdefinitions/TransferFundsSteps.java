package stepdefinitions;

import context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TransferFundsSteps {
    private final TestContext ctx;

    public TransferFundsSteps(TestContext ctx) { this.ctx = ctx; }

    @When("I transfer {string} from my first account to my second account")
    public void transfer(String amount) {
        ctx.driver.findElement(By.linkText("Transfer Funds")).click();
        WebDriverWait wait = new WebDriverWait(ctx.driver, Duration.ofSeconds(15));
        wait.until(d -> d.findElements(By.cssSelector("#toAccountId option")).size() >= 2);

        // first account holds the money, second is the new one
        new Select(ctx.driver.findElement(By.id("fromAccountId"))).selectByIndex(0);
        new Select(ctx.driver.findElement(By.id("toAccountId"))).selectByIndex(1);
        ctx.driver.findElement(By.id("amount")).sendKeys(amount);
        ctx.driver.findElement(By.cssSelector("input[value='Transfer']")).click();
    }

    @Then("I should see a transfer complete confirmation for {string}")
    public void confirmation(String amount) {
        new WebDriverWait(ctx.driver, Duration.ofSeconds(15)).until(
                ExpectedConditions.textToBePresentInElementLocated(By.id("showResult"), "Transfer Complete!"));
        String text = ctx.driver.findElement(By.id("showResult")).getText();
        Assert.assertTrue(text.contains(String.format("$%.2f", Double.parseDouble(amount))));
    }

    @Then("I should see an error and no transfer confirmation")
    public void errorShown() {
        new WebDriverWait(ctx.driver, Duration.ofSeconds(15)).until(
                ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "Error!"));
        String body = ctx.driver.findElement(By.tagName("body")).getText();
        Assert.assertFalse(body.contains("Transfer Complete!"));
    }
}