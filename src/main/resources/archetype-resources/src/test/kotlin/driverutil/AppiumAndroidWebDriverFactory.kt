#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
package ${package}.driverutil

import assertk.fail
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidElement
import io.appium.java_client.remote.MobileCapabilityType
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import java.io.File
import java.net.URL

class AppiumAndroidWebDriverFactory : RemoteWebDriverFactory() {
    override fun createDriver(): WebDriver {
        WebDriverManager.chromedriver().driverVersion(getBrowserVersion()).setup()

        val path2chromeDriver = WebDriverManager.chromedriver().downloadedDriverPath.substringBeforeLast(File.separator)


        caps.setCapability("chromedriverExecutableDir", path2chromeDriver)
        caps.setCapability(MobileCapabilityType.APPIUM_VERSION, "1.13")
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android")
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Appium_Android_Device")
        //caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator1")
        caps.setCapability("browserName", "chrome")
        caps.setCapability(MobileCapabilityType.UDID, getMobileDeviceId())

        caps.setCapability("noReset", true)

        val appiumServer = URL("$dollar$curlyOpen getRemoteTestingServer$bracketOpen$bracketClose$curlyClose/wd/hub")

        try {
            webDriver = AndroidDriver<AndroidElement>(appiumServer, caps)

        } catch (e: WebDriverException) {
            fail("Appium error: $appiumServer ::: Appium started?")
        }


        return webDriver
    }

    private fun getMobileDeviceId(): String {
        return System.getProperty("device.id", "emulator-5554")
    }

}
