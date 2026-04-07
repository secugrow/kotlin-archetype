#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
package ${package}.driverutil.factory

import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.WebDriver
import java.net.URI

/**
 * Factory for creating a remote Android WebDriver via Selenoid.
 *
 * This factory creates an AndroidDriver connecting to a remote Selenoid grid.
 * It connects to a remote server specified by the system property "selenium.grid".
 * The device UDID can be set via the system property "device.id".
 */
class RemoteAndroidWebDriverFactory : RemoteWebDriverFactory() {

    override fun createDriver(): WebDriver {
        log.info("Creating Remote Android WebDriver")

        caps.apply {
            browserName = "android"
            setVersion("8.1")
            setCapability("enableVNC", true)
            setCapability("enableVideo", false)
            setCapability("sessionTimeout", "15m")
            setCapability("noReset", false)
            setCapability("udid", System.getProperty("device.id", "emulator-5554"))
            setCapability("platformName", "Android")
            setCapability("deviceName", "Android_Device_Appium")
            setCapability("automationName", "UiAutomator2")
            setCapability("newCommandTimeout", 12000)
        }

        return runCatching {
            val remoteUrl = URI.create("$dollar$curlyOpen getRemoteTestingServer$bracketOpen$bracketClose$curlyClose/wd/hub").toURL()
            log.info("Connecting to remote server at: $remoteUrl")

            webDriver = AndroidDriver(remoteUrl, caps)
            configureTimeouts(webDriver)
        }.getOrElse { e ->
            log.error("Failed to create Remote Android WebDriver: $dollar$curlyOpen e.message$curlyClose")
            throw e
        }
    }
}
