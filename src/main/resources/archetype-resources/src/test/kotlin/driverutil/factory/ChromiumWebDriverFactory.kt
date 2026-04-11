package ${package}.driverutil.factory

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

/**
 * Factory for creating Chromium WebDriver instances.
 *
 * This factory creates a ChromeDriver pointed at a Chromium binary.
 * It supports headless mode and configures the browser window size.
 * The Chromium binary path can be set via the system property "webdriver.chrome.driver".
 */
class ChromiumWebDriverFactory : WebDriverFactory() {

    override fun createDriver(): WebDriver {
        log.info("Creating Chromium WebDriver")

        val options = ChromeOptions()
        options.addArguments("--remote-allow-origins=*")

        if (headless) {
            log.info("Running Chromium in headless mode")
            options.addArguments("--headless=new")
            options.addArguments("--disable-gpu")
        }

        return runCatching {
            webDriver = ChromeDriver(options.merge(caps))
            webDriver.manage().window().size = screenDimension.dimension
            configureTimeouts(webDriver)
        }.getOrElse { e ->
            log.error("Failed to create Chromium WebDriver: ${e.message}")
            throw e
        }
    }
}
