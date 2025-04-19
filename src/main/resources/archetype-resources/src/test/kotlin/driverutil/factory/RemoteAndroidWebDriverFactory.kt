package ${package}.driverutil.factory

#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )

import io.appium.java_client.android.AndroidDriver
import org.openqa.selenium.WebDriver
import java.net.URI

class RemoteAndroidWebDriverFactory : RemoteWebDriverFactory() {
    override fun createDriver(): WebDriver {

        caps.apply {
            browserName = "android"
            setVersion("8.1")
            setCapability("enableVNC", true)
            setCapability("enableVideo", false)
            setCapability("sessionTimeout", "15m")
            setCapability("noReset", false)
            setCapability("udid", System.getProperty("device.id", "emulator-5554"))//"DEFAULT_ANDROID_DEVICE_ID"))
            setCapability("platformName", "Android")
            setCapability("deviceName", "Android_Device_Appium")
            setCapability("automationName", "UiAutomator2")
            setCapability("newCommandTimeout", 12000)
        }

        webDriver = AndroidDriver(URI.create("${ getRemoteTestingServer()}/wd/hub").toURL(), caps)

        return webDriver

    }
}