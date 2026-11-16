import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object страницы результата отправки формы (/large_form после POST).
 * Данные выводятся в таблице submitted-data: имя поля -> введённое значение.
 */
public class SubmittedFormPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By HEADING = By.xpath("//h3[text()='Form Submitted']");
    private static final By SUBMITTED_TABLE = By.id("submitted-data");

    public SubmittedFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getSubmittedValue(String fieldName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(HEADING));
        return driver.findElement(By.xpath(
                "//table[@id='submitted-data']//td[text()='" + fieldName + "']/following-sibling::td"
        )).getText();
    }
}
