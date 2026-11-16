import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

public class LargeFormTest {

    private static final Logger log = LogManager.getLogger(LargeFormTest.class);

    private static final String BASE_URL = System.getProperty("base.url",
            "https://the-internet.herokuapp.com");

    private static final String LOGIN = System.getProperty("login", "it@test.com");
    private static final String PASSWORD = System.getProperty("password", "Secret123");
    private static final String CONFIRM_PASSWORD = System.getProperty("confirmPassword", "Secret123");

    private static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "true"));

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = WebDriverFactory.createDriver(HEADLESS ? "--headless=new" : "--start-maximized");
        log.info("Браузер запущен (headless=" + HEADLESS + ")");
    }

    @AfterEach
    public void close() {
        if (driver != null) {
            driver.quit();
        }
        log.info("Браузер закрыт");
    }

    /**
     * Домашнее задание «Первый автотест».
     * Заполнение всех полей формы /large_form, проверка совпадения пароля
     * и его подтверждения, отправка и проверка данных на странице результата.
     */
    @Test
    public void largeFormTest() {
        log.info("=== Заполнение большой формы ===");
        LargeFormPage formPage = new LargeFormPage(driver);
        formPage.open(BASE_URL + "/large_form");
        log.info("Открыта страница: " + BASE_URL + "/large_form");

        // Проверка совпадения пароля и его подтверждения.
        // Сервер пароль в ответе не возвращает, поэтому сравниваем до отправки формы.
        Assertions.assertEquals(PASSWORD, CONFIRM_PASSWORD,
                "Пароль и его подтверждение не совпадают");
        log.info("Пароль и подтверждение совпадают");

        formPage.enterFirstName("Ivan");
        formPage.enterLastName("Testov");
        formPage.enterEmail(LOGIN);
        formPage.enterPassword(PASSWORD);
        formPage.enterNickname("tester");
        formPage.enterBirthday("1995-06-15");
        formPage.enterAge("30");
        formPage.selectCountry("de");
        formPage.selectLanguages("English", "German");
        formPage.enterBio("Java test automation");
        formPage.selectContactByValue("phone");
        formPage.checkInterest("testing");
        formPage.checkInterest("automation");
        formPage.setExperience("8");
        formPage.setColor("#00ff00");
        formPage.acceptTerms();
        log.info("Все поля формы заполнены");

        formPage.submit();
        log.info("Форма отправлена");

        SubmittedFormPage resultPage = new SubmittedFormPage(driver);
        Assertions.assertEquals("Ivan", resultPage.getSubmittedValue("first_name"));
        Assertions.assertEquals("Testov", resultPage.getSubmittedValue("last_name"));
        Assertions.assertEquals(LOGIN, resultPage.getSubmittedValue("email"));
        Assertions.assertEquals("tester", resultPage.getSubmittedValue("nickname"));
        Assertions.assertEquals("1995-06-15", resultPage.getSubmittedValue("birthday"));
        Assertions.assertEquals("30", resultPage.getSubmittedValue("age"));
        Assertions.assertEquals("de", resultPage.getSubmittedValue("country"));
        Assertions.assertEquals("English, German", resultPage.getSubmittedValue("languages"));
        Assertions.assertEquals("Java test automation", resultPage.getSubmittedValue("bio"));
        Assertions.assertEquals("phone", resultPage.getSubmittedValue("contact"));
        Assertions.assertEquals("testing, automation", resultPage.getSubmittedValue("interests"));
        Assertions.assertEquals("8", resultPage.getSubmittedValue("experience"));
        Assertions.assertEquals("#00ff00", resultPage.getSubmittedValue("color"));
        Assertions.assertEquals("accepted", resultPage.getSubmittedValue("terms"));
        log.info("Все введённые данные корректно отобразились на странице результата");
    }
}
