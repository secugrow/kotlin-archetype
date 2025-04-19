package ${package}.driverutil.factory

import ${package}.driverutil.emulatedDevices
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

class ChromeMobileEmulationWebDriverFactory : WebDriverFactory() {

    override fun createDriver(): WebDriver {
        caps.browserName = "chrome"
        caps.setVersion(getBrowserVersion())

        val chromeOptions = ChromeOptions()

        val mobileEmulation = HashMap<String, String>()
        mobileEmulation["deviceName"] = System.getProperty("emulated.device", emulatedDevices.Pixel_2.phoneName)
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation)
        webDriver = ChromeDriver(chromeOptions.merge(caps))
        return webDriver
    }
}
