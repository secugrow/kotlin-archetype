#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
package ${package}.driverutil.factory

import io.appium.java_client.AppiumDriver
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.net.URI

/**
 * Factory for creating a remote Chrome WebDriver for mobile browsers via Selenoid.
 *
 * This factory creates an AppiumDriver for mobile Chrome on a remote Selenoid grid.
 * It connects to a remote server specified by the system property "selenium.grid".
 * The browser version should be set to a mobile version (e.g. "86.0").
 */
class RemoteChromeMobileWebDriverFactory : RemoteWebDriverFactory() {

    override fun createDriver(): WebDriver {
        log.info("Creating Remote Chrome Mobile WebDriver")

        val options = ChromeOptions().merge(caps)
        options.browserVersion = "mobile-$dollar$curlyOpen getBrowserVersion$bracketOpen$bracketClose$curlyClose"
        options.setCapability("browserName", "chrome")
        options.setCapability("enableVNC", false)
        options.setCapability("sessionTimeout", "15m")

        return runCatching {
            val remoteUrl = URI.create("$dollar$curlyOpen getRemoteTestingServer$bracketOpen$bracketClose$curlyClose/wd/hub").toURL()
            log.info("Connecting to remote server at: $remoteUrl")

            webDriver = AppiumDriver(remoteUrl, options)
            webDriver.manage().window().maximize()

            configureTimeouts(webDriver)
        }.getOrElse { e ->
            log.error("Failed to create Remote Chrome Mobile WebDriver: $dollar$curlyOpen e.message$curlyClose")
            throw e
        }
    }
}
