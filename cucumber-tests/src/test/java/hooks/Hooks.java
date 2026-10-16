package hooks;

import context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class Hooks {
    private final TestContext ctx;

    public Hooks(TestContext ctx) {
        this.ctx = ctx;
    }

    @Before
    public void setUp() {
        ctx.driver = new ChromeDriver();
        ctx.driver.manage().window().maximize();
        ctx.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @After
    public void tearDown() {
        if (ctx.driver != null) {
            ctx.driver.quit();
        }
    }
}