#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )


package ${package}.driverutil

import io.appium.java_client.AppiumDriver
import io.appium.java_client.remote.MobileCapabilityType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.net.URI

class RemoteChromeMobileWebDriverFactory : RemoteWebDriverFactory() {

    override fun createDriver(): WebDriver {

        //caps.setCapability("adbExecTimeout", 120000)
        val options = ChromeOptions().merge(caps)
        options.setBrowserVersion("mobile-$dollar$curlyOpen getBrowserVersion$bracketOpen$bracketClose$curlyClose")
        options.setCapability(MobileCapabilityType.BROWSER_NAME, "chrome")

        //BUG in Android selenoid Image, 20.06.2019
        options.setCapability("enableVNC", false)
        options.setCapability("sessionTimeout", "15m")

        webDriver = AppiumDriver(URI.create("$dollar$curlyOpen getRemoteTestingServer$bracketOpen$bracketClose$curlyClose/wd/hub").toURL(), options)
        webDriver.manage().window().maximize()
        return webDriver
    }

}
