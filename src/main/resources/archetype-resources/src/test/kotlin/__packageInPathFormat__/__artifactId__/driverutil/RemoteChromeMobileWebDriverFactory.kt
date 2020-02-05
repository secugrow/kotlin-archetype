#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
package at.co.boris.secuton.driverutil

import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.AndroidElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URI

class RemoteChromeMobileWebDriverFactory : RemoteWebDriverFactory() {

    override fun createDriver(): WebDriver {

        caps.browserName = "chrome"
        caps.version = "mobile-$dollar$curlyOpen getBrowserVersion$bracketOpen$bracketClose$curlyClose"
        caps.setCapability("adbExecTimeout", 120000)

        val options = ChromeOptions()
        options.merge(caps)


        //BUG in Android selenoid Image, 20.06.2019
        options.setCapability("enableVNC", false)

        options.setCapability("sessionTimeout", "15m")

        webDriver = AppiumDriver<AndroidElement>(URI.create("$dollar$curlyOpen getRemoteTestingServer$bracketOpen$bracketClose$curlyClose/wd/hub").toURL(), options)
        webDriver.manage().window().maximize()
        return webDriver
    }

}
