import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public final class WebDriverFactory {

    private WebDriverFactory() {
    }

    public static WebDriver createDriver(String... arguments) {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        String remoteUrl = System.getProperty("remote.url");

        if (remoteUrl != null && !remoteUrl.isBlank()) {
            return createRemoteDriver(remoteUrl, browser, arguments);
        }

        switch (browser) {
            case "firefox": {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                for (String argument : arguments) {
                    options.addArguments(argument);
                }
                return new FirefoxDriver(options);
            }
            case "chrome": {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                for (String argument : arguments) {
                    options.addArguments(argument);
                }
                return new ChromeDriver(options);
            }
            default:
                throw new IllegalArgumentException("Неизвестный браузер: " + browser
                        + ". Допустимые значения: chrome, firefox");
        }
    }

    private static WebDriver createRemoteDriver(String remoteUrl, String browser, String... arguments) {
        try {
            URL url = new URL(remoteUrl);
            switch (browser) {
                case "firefox": {
                    FirefoxOptions options = new FirefoxOptions();
                    for (String argument : arguments) {
                        options.addArguments(argument);
                    }
                    return new RemoteWebDriver(url, options);
                }
                case "chrome":
                default: {
                    ChromeOptions options = new ChromeOptions();
                    for (String argument : arguments) {
                        options.addArguments(argument);
                    }
                    return new RemoteWebDriver(url, options);
                }
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Некорректный remote.url: " + remoteUrl, e);
        }
    }
}
