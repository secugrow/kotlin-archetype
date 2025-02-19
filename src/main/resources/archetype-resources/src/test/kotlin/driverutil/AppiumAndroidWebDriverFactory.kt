#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
package ${package}.driverutil

import io.appium.java_client.android.AndroidDriver
import org.assertj.core.api.Assertions.fail
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.chrome.ChromeOptions
import java.net.URI

class AppiumAndroidWebDriverFactory : RemoteWebDriverFactory() {
    override fun createDriver(): WebDriver {

        // Create ChromeOptions for DevTools
        val chromeOptions = ChromeOptions()
        chromeOptions.addArguments("--disable-extensions")
        chromeOptions.addArguments("--no-sandbox")
        chromeOptions.setExperimentalOption("w3c", false) // Important for Appium support

        caps.setCapability("platformName", "Android")
        caps.setCapability("deviceName", "Appium_Android_Device")
        caps.setCapability("automationName", "UiAutomator2")
        caps.setCapability("browserName", "chrome")
        caps.setCapability("udid", getMobileDeviceId())
        caps.setCapability("noReset", true)
        caps.setCapability("appium:chromeOptions", chromeOptions)
//        caps.setCapability("appium:chromedriverExecutable", webDriverManager.downloadedDriverPath)

        val appiumServer = URI.create(getRemoteTestingServer$bracketOpen$bracketClose).toURL()

        try {
            webDriver = AndroidDriver(appiumServer, caps)

        } catch (e: WebDriverException) {
            fail("Appium error: $appiumServer  exception message: $dollar$curlyOpen e.localizedMessage$curlyClose ::: Appium started?")
        }
        return webDriver
    }

    private fun getMobileDeviceId(): String {
        return System.getProperty("device.id", "emulator-5554")
    }

}
