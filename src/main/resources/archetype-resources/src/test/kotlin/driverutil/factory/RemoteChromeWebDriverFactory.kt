#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
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
 * It supports headless mode.
 */
class RemoteChromeWebDriverFactory : RemoteWebDriverFactory() {

    override fun createDriver(): WebDriver {
        log.info("Creating Remote Chrome WebDriver")

        val options = ChromeOptions()
        options.setCapability(CapabilityType.BROWSER_NAME, "chrome")

        if (headless) {
            log.info("Running Chrome in headless mode")
            options.addArguments("--headless=new")
            options.addArguments("--disable-gpu")
        }

        return runCatching {
            val remoteUrl = java.net.URI.create("$dollar$curlyOpen getRemoteTestingServer$bracketOpen$bracketClose$curlyClose/wd/hub").toURL()
            log.info("Connecting to remote server at: $remoteUrl")

            webDriver = RemoteWebDriver(remoteUrl, options.merge(caps))
            webDriver.manage().window().maximize()

            configureTimeouts(webDriver)
        }.getOrElse { e ->
            log.error("Failed to create Remote Chrome WebDriver: $dollar$curlyOpen e.message$curlyClose")
            throw e
        }
    }
}
