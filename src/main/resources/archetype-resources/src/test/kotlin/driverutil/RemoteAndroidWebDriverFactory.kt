package ${package}.driverutil

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

        caps.browserName = "android"
        caps.setVersion("8.1")
        caps.setCapability("enableVNC", true)
        caps.setCapability("enableVideo", false)
        //caps.setCapability("screenResolution", screenSize)
        caps.setCapability("sessionTimeout", "15m")

        caps.setCapability("noReset", false)
        caps.setCapability("udid", System.getProperty("device.id", "emulator-5554"))//"DEFAULT_ANDROID_DEVICE_ID"))
        caps.setCapability("platformName", "Android")
        caps.setCapability("deviceName", "Android_Device_Appium")
        caps.setCapability("automationName", "UiAutomator2")
        caps.setCapability("newCommandTimeout", 12000)

        webDriver = AndroidDriver(URI.create("${ getRemoteTestingServer()}/wd/hub").toURL(), caps)

        return webDriver

    }
}