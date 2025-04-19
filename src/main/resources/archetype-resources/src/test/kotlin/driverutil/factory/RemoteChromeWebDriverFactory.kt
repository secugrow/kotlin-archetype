package ${package}.driverutil.factory

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.RemoteWebDriver

/**
 * Factory for creating remote Chrome WebDriver instances.
 * 
 * This factory creates a RemoteWebDriver for Chrome with the specified options and capabilities.
 * It connects to a remote Selenium server specified by the system property "selenium.grid".
 */
class RemoteChromeWebDriverFactory : RemoteWebDriverFactory() {

    override fun createDriver(): WebDriver {
        log.info("Creating Remote Chrome WebDriver")

        val options = ChromeOptions()
        options.setCapability(CapabilityType.BROWSER_NAME, "chrome")

        // Configure headless mode if enabled
        if (headless) {
            log.info("Running Chrome in headless mode")
            options.addArguments("--headless=new")
            options.addArguments("--disable-gpu")
        }

        return runCatching {
            // Merge options with capabilities from parent class  
            val mergedOptions = options.merge(caps)

            val remoteUrl = java.net.URI.create("${ getRemoteTestingServer() } /wd/hub").toURL()
            log.info("Connecting to remote server at: $remoteUrl")

            webDriver = RemoteWebDriver(remoteUrl, mergedOptions)
            webDriver.manage().window().maximize()

            // Configure timeouts
            configureTimeouts(webDriver)
        }.getOrElse { e ->
            log.error("Failed to create Remote Chrome WebDriver: ${e.message}")
            throw e
        }
    }
}
