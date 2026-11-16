import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LargeFormPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By FIRST_NAME_INPUT = By.id("first-name");
    private static final By LAST_NAME_INPUT = By.id("last-name");
    private static final By EMAIL_INPUT = By.cssSelector("input[name='email']");
    private static final By PASSWORD_INPUT = By.cssSelector("input[name='password']");
    private static final By NICKNAME_INPUT = By.cssSelector("input[name='nickname']");
    private static final By BIRTHDAY_INPUT = By.id("birthday");
    private static final By AGE_INPUT = By.cssSelector("input[name='age']");
    private static final By COUNTRY_SELECT = By.id("country");
    private static final By LANGUAGES_SELECT = By.cssSelector("select[name='languages[]']");
    private static final By BIO_TEXTAREA = By.id("bio");
    private static final By EXPERIENCE_INPUT = By.id("experience");
    private static final By COLOR_INPUT = By.cssSelector("input[name='color']");
    private static final By TERMS_CHECKBOX = By.id("terms");
    private static final By SUBMIT_BUTTON = By.id("submit");

    public LargeFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME_INPUT));
    }

    public void enterFirstName(String value) {
        find(FIRST_NAME_INPUT).sendKeys(value);
    }

    public void enterLastName(String value) {
        find(LAST_NAME_INPUT).sendKeys(value);
    }

    public void enterEmail(String value) {
        find(EMAIL_INPUT).sendKeys(value);
    }

    public void enterPassword(String value) {
        find(PASSWORD_INPUT).sendKeys(value);
    }

    public void enterNickname(String value) {
        find(NICKNAME_INPUT).sendKeys(value);
    }

    public void enterBirthday(String value) {
        setValueViaJs(BIRTHDAY_INPUT, value);
    }

    public void enterAge(String value) {
        find(AGE_INPUT).sendKeys(value);
    }

    public void selectCountry(String value) {
        new Select(find(COUNTRY_SELECT)).selectByValue(value);
    }

    public void selectLanguages(String... visibleTexts) {
        Select languages = new Select(find(LANGUAGES_SELECT));
        for (String text : visibleTexts) {
            languages.selectByVisibleText(text);
        }
    }

    public void enterBio(String value) {
        find(BIO_TEXTAREA).sendKeys(value);
    }

    public void selectContactByValue(String value) {
        find(By.cssSelector("input[name='contact'][value='" + value + "']")).click();
    }

    public void checkInterest(String value) {
        find(By.cssSelector("input.interest[value='" + value + "']")).click();
    }

    public void setExperience(String value) {
        setValueViaJs(EXPERIENCE_INPUT, value);
    }

    public void setColor(String value) {
        // input type="color" не заполняется sendKeys — задаём значение через JavaScript
        setValueViaJs(COLOR_INPUT, value);
    }

    public void acceptTerms() {
        find(TERMS_CHECKBOX).click();
    }

    public void submit() {
        find(SUBMIT_BUTTON).click();
    }

    private WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void setValueViaJs(By locator, String value) {
        WebElement element = find(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];"
                        + "arguments[0].dispatchEvent(new Event('input', {bubbles: true}));"
                        + "arguments[0].dispatchEvent(new Event('change', {bubbles: true}));",
                element, value);
    }
}
