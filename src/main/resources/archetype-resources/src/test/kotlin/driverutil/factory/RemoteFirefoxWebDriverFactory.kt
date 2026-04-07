#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
package ${package}.driverutil.factory

import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URI

/**
 * Factory for creating remote Firefox WebDriver instances.
 *
 * This factory creates a RemoteWebDriver for Firefox with the specified options and capabilities.
 * It connects to a remote Selenium server specified by the system property "selenium.grid".
 * It supports headless mode.
 */
class RemoteFirefoxWebDriverFactory : RemoteWebDriverFactory() {

    override fun createDriver(): WebDriver {
        log.info("Creating Remote Firefox WebDriver")

        val options = FirefoxOptions()
        options.setCapability(CapabilityType.BROWSER_NAME, "firefox")

        if (headless) {
            log.info("Running Firefox in headless mode")
            options.addArguments("-headless")
        }

        return runCatching {
            val remoteUrl = URI.create("$dollar$curlyOpen getRemoteTestingServer$bracketOpen$bracketClose$curlyClose/wd/hub").toURL()
            log.info("Connecting to remote server at: $remoteUrl")

            webDriver = RemoteWebDriver(remoteUrl, options.merge(caps))
            webDriver.manage().window().maximize()

            configureTimeouts(webDriver)
        }.getOrElse { e ->
            log.error("Failed to create Remote Firefox WebDriver: $dollar$curlyOpen e.message$curlyClose")
            throw e
        }
    }
}
