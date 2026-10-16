package stepdefinitions;

import context.TestContext;
import io.cucumber.java.en.Given;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CommonSteps {
    private static final String BASE = "https://parabank.parasoft.com/parabank/";
    private final TestContext ctx;

    public CommonSteps(TestContext ctx) { this.ctx = ctx; }

    @Given("I am a registered customer with two accounts")
    public void registerWithTwoAccounts() {
        RuntimeException last = null;
        for (int attempt = 1; attempt <= 3; attempt++) {   // server is flaky, so retry
            try {
                register();
                openSecondAccount();
                return;
            } catch (RuntimeException e) {
                last = e;
            }
        }
        throw last;
    }

    private void register() {
        ctx.username = "cuke" + System.currentTimeMillis();
        ctx.driver.get(BASE + "register.htm");
        type("customer.firstName", "John");
        type("customer.lastName", "Smith");
        type("customer.address.street", "123 Test Street");
        type("customer.address.city", "Melbourne");
        type("customer.address.state", "VIC");
        type("customer.address.zipCode", "3000");
        type("customer.phoneNumber", "0400000000");
        type("customer.ssn", "123-45-6789");
        type("customer.username", ctx.username);
        type("customer.password", ctx.password);
        type("repeatedPassword", ctx.password);
        ctx.driver.findElement(By.cssSelector("input[value='Register']")).click();
        waiter().until(ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("body"), "Your account was created successfully"));
    }

    private void openSecondAccount() {
        ctx.driver.findElement(By.linkText("Open New Account")).click();
        // account dropdown is filled by JavaScript, so wait for options
        waiter().until(d -> !d.findElements(By.cssSelector("#fromAccountId option")).isEmpty());
        ctx.driver.findElement(By.cssSelector("input[value='Open New Account']")).click();
        waiter().until(ExpectedConditions.visibilityOfElementLocated(By.id("newAccountId")));
    }

    private void type(String id, String value) {
        ctx.driver.findElement(By.id(id)).sendKeys(value);
    }

    private WebDriverWait waiter() {
        return new WebDriverWait(ctx.driver, Duration.ofSeconds(15));
    }
}