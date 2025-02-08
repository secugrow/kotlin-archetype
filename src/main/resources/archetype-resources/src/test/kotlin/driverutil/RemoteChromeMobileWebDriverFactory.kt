#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )


package ${package}.driverutil

import io.appium.java_client.AppiumDriver
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.net.URI

class RemoteChromeMobileWebDriverFactory : RemoteWebDriverFactory() {

    override fun createDriver(): WebDriver {

        //caps.setCapability("adbExecTimeout", 120000)
        val options = ChromeOptions().merge(caps)
        options.setBrowserVersion("mobile-${ getBrowserVersion()}")
        options.setCapability("browserName", "chrome")

        //BUG in Android selenoid Image, 20.06.2019
        options.setCapability("enableVNC", false)
        options.setCapability("sessionTimeout", "15m")

        webDriver = AppiumDriver(URI.create("${ getRemoteTestingServer()}/wd/hub").toURL(), options)
        webDriver.manage().window().maximize()
        return webDriver
    }

}
