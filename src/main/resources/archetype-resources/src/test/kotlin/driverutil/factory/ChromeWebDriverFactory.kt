package ${package}.driverutil.factory

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

/**
 * Factory for creating Chrome WebDriver instances.
 *
 * This factory creates a ChromeDriver with the specified options and capabilities.
 * It supports headless mode and configures the browser window size.
 */
class ChromeWebDriverFactory : WebDriverFactory() {

    override fun createDriver(): WebDriver {
        log.info("Creating Chrome WebDriver")

        val options = ChromeOptions()
        options.addArguments("--remote-allow-origins=*")

        // Configure headless mode if enabled
        if (headless) {
            log.info("Running Chrome in headless mode")
            options.addArguments("--headless=new")
            options.addArguments("--disable-gpu")
        }

        try {
        webDriver = ChromeDriver(options.merge(caps))
        webDriver.manage().window().size = screenDimension.dimension

            // Configure timeouts
            return configureTimeouts(webDriver)
        } catch (e: Exception) {
            log.error("Failed to create Chrome WebDriver: ${e.message}")
            throw e
    }
    }
}
