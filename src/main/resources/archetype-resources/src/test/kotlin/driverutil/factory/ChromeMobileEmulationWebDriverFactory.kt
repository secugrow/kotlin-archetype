package ${package}.driverutil.factory

import ${package}.driverutil.emulatedDevices
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

/**
 * Factory for creating a Chrome WebDriver in mobile emulation mode.
 *
 * This factory creates a ChromeDriver configured to emulate a mobile device.
 * The device can be configured via the system property "emulated.device".
 * Defaults to Pixel 2 if not specified.
 */
class ChromeMobileEmulationWebDriverFactory : WebDriverFactory() {

    override fun createDriver(): WebDriver {
        log.info("Creating Chrome Mobile Emulation WebDriver")

        caps.browserName = "chrome"
        caps.setVersion(getBrowserVersion())

        val chromeOptions = ChromeOptions()
        val deviceName = System.getProperty("emulated.device", emulatedDevices.Pixel_2.phoneName)
        log.info("Emulating device: $deviceName")

        val mobileEmulation = HashMap<String, String>()
        mobileEmulation["deviceName"] = deviceName
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation)

        return runCatching {
            webDriver = ChromeDriver(chromeOptions.merge(caps))
            configureTimeouts(webDriver)
        }.getOrElse { e ->
            log.error("Failed to create Chrome Mobile Emulation WebDriver: ${e.message}")
            throw e
        }
    }
}
