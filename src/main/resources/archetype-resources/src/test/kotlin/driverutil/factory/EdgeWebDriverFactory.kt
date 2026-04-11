package ${package}.driverutil.factory

import org.openqa.selenium.WebDriver
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions

/**
 * Factory for creating Edge WebDriver instances.
 *
 * This factory creates an EdgeDriver with the specified options and capabilities.
 * It supports headless mode and configures the browser window size.
 * Note: Edge is only supported on Windows.
 */
class EdgeWebDriverFactory : WebDriverFactory() {

    override fun createDriver(): WebDriver {
        log.info("Creating Edge WebDriver")

        checkEnvironment()

        val options = EdgeOptions()

        if (headless) {
            log.info("Running Edge in headless mode")
            options.addArguments("--headless=new")
            options.addArguments("--disable-gpu")
        }

        return runCatching {
            webDriver = EdgeDriver(options.merge(caps))
            webDriver.manage().window().size = screenDimension.dimension
            configureTimeouts(webDriver)
        }.getOrElse { e ->
            log.error("Failed to create Edge WebDriver: ${e.message}")
            throw e
        }
    }

    private fun checkEnvironment() {
        if (!System.getProperty("os.name").contains("Windows")) {
            log.warn("Your OS seems not to be Windows")
        }

        if (!System.getenv("Path").lowercase().contains("edge")) {
            log.warn("Your SystemPath seems not to contain edge binary")
        }
    }
}
