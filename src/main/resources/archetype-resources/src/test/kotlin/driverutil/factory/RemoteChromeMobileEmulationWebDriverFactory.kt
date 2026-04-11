#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
package ${package}.driverutil.factory

import ${package}.driverutil.emulatedDevices
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URI

/**
 * Factory for creating a remote Chrome WebDriver in mobile emulation mode.
 *
 * This factory creates a RemoteWebDriver for Chrome configured to emulate a mobile device.
 * It connects to a remote Selenium server specified by the system property "selenium.grid".
 * The device can be configured via the system property "emulated.device".
 */
class RemoteChromeMobileEmulationWebDriverFactory : RemoteWebDriverFactory() {

    override fun createDriver(): WebDriver {
        log.info("Creating Remote Chrome Mobile Emulation WebDriver")

        caps.browserName = "chrome"
        caps.setVersion(getBrowserVersion())

        val options = ChromeOptions().merge(caps)
        options.setCapability(CapabilityType.BROWSER_NAME, "chrome")
        options.setCapability(CapabilityType.BROWSER_VERSION, getBrowserVersion()!!)

        val deviceName = System.getProperty("emulated.device", emulatedDevices.Pixel_2.phoneName)
        log.info("Emulating device: $deviceName")

        val mobileEmulation = HashMap<String, String>()
        mobileEmulation["deviceName"] = deviceName
        options.setExperimentalOption("mobileEmulation", mobileEmulation)

        return runCatching {
            val remoteUrl = URI.create("$dollar$curlyOpen getRemoteTestingServer$bracketOpen$bracketClose$curlyClose/wd/hub").toURL()
            log.info("Connecting to remote server at: $remoteUrl")

            webDriver = RemoteWebDriver(remoteUrl, options)
            configureTimeouts(webDriver)
        }.getOrElse { e ->
            log.error("Failed to create Remote Chrome Mobile Emulation WebDriver: $dollar$curlyOpen e.message$curlyClose")
            throw e
        }
    }
}
