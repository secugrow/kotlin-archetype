#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
package ${package}.driverutil

import org.assertj.core.api.Assertions.fail
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.remote.MobileCapabilityType
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import java.net.URI

class AppiumAndroidWebDriverFactory : RemoteWebDriverFactory() {
    override fun createDriver(): WebDriver {
        val webDriverManager = WebDriverManager.chromedriver().driverVersion(getBrowserVersion())
        webDriverManager.setup()

        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android")
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Appium_Android_Device")
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2")
        caps.setCapability("browserName", "chrome")
        caps.setCapability(MobileCapabilityType.UDID, getMobileDeviceId())
        caps.setCapability("appium:chromeOptions", mutableMapOf(Pair("w3c", false)));
        caps.setCapability("noReset", true)
        caps.setCapability("appium:chromedriverExecutable", webDriverManager.downloadedDriverPath)

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
