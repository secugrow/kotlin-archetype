package ${package}.driverutil.factory

import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions

/**
 * Factory for creating Firefox WebDriver instances.
 *
 * This factory creates a FirefoxDriver with the specified options and capabilities.
 * It supports headless mode and configures the browser window size.
 */
class FirefoxWebDriverFactory : WebDriverFactory() {

    override fun createDriver(): WebDriver {
        log.info("Creating Firefox WebDriver")

        val ffOptions = FirefoxOptions()

        if (headless) {
            log.info("Running Firefox in headless mode")
            ffOptions.addArguments("-headless")
        }

        val linux = System.getProperty("os.name").lowercase().contains("linux")
        if (linux) {
            log.debug("Running on Linux - if Firefox is installed via snap, set TMPDIR to a writable directory in your home directory with the absolute path")
        }

        return runCatching {
            webDriver = FirefoxDriver(ffOptions.merge(caps))
            webDriver.manage().window().size = screenDimension.dimension
            configureTimeouts(webDriver)
        }.getOrElse { e ->
            log.error("Failed to create Firefox WebDriver: ${e.message}")
            throw e
        }
    }
}
