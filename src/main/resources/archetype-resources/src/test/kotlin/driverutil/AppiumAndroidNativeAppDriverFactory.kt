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

class AppiumAndroidNativeAppDriverFactory(private val proxyPort: Int? = null): AppiumNativeAppCommonWebDriverFactory() {
    override fun createDriver(): WebDriver {
        if (proxyPort != null) setProxy(proxyPort)
        return createDriver(2)
    }

    fun createDriver(automatorVersion: Int): WebDriver {

        WebDriverManager.chromedriver().driverVersion(getDriverVersion()).setup()
        val path2chromeDriver = WebDriverManager.chromedriver().downloadedDriverPath.substringBeforeLast(File.separator)


        val noResetApp = System.getProperty("noResetApp", "false").toBoolean()

        caps.setCapability("chromedriverExecutableDir", path2chromeDriver)
        caps.setCapability("noReset", noResetApp)
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android")
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Appium_Android_Device")
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator$automatorVersion")
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "600") //for debugging = 10 min.
        caps.setCapability(MobileCapabilityType.UDID, getMobileDeviceId())
        caps.setCapability(MobileCapabilityType.APP, System.getProperty("apk.path", "missing parameter for path to apk 'apk.path'"))

        val appiumServer = URL("$dollar$curlyOpen getRemoteTestingServer$bracketOpen$bracketClose$curlyClose/wd/hub")
        try {
            webDriver = AndroidDriver<AndroidElement>(appiumServer, caps)
        } catch (e: WebDriverException) {
            fail("Appium error: $appiumServer  exception message: ${e.localizedMessage} ::: Appium started?")
        }
        return webDriver
    }
}
