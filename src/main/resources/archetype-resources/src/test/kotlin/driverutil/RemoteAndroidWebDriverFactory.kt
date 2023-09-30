package ${package}.driverutil

#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )


import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.remote.MobileCapabilityType
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
        caps.setCapability(MobileCapabilityType.UDID, System.getProperty("device.id", "emulator-5554"))//"DEFAULT_ANDROID_DEVICE_ID"))
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android")
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "Android_Device_Appium")
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2")
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 12000)

        webDriver = AndroidDriver(URI.create("$dollar$curlyOpen getRemoteTestingServer$bracketOpen$bracketClose$curlyClose/wd/hub").toURL(), caps)

        return webDriver

    }

}
