package ${package}.driverutil

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities

class ChromeMobileEmulationWebDriverFactory : WebDriverFactory() {


    override fun createDriver(): WebDriver {
        WebDriverManager.chromedriver().driverVersion(getBrowserVersion()).setup()

        val capabilities = DesiredCapabilities()
        capabilities.browserName = "chrome"
        capabilities.setVersion(getBrowserVersion())

        val chromeOptions = ChromeOptions()


        val mobileEmulation = HashMap<String, String>()
        mobileEmulation["deviceName"] = System.getProperty("emulated.device", emulatedDevices.Pixel_2.phoneName)
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation)
        webDriver = ChromeDriver(chromeOptions.merge(capabilities))
        return webDriver
    }
}
