#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )

package ${package}.driverutil

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URI

class RemoteChromeWebDriverFactory : RemoteWebDriverFactory() {

    override fun createDriver(): WebDriver {

        val options = ChromeOptions().merge(caps)
        options.setCapability(CapabilityType.BROWSER_NAME, "chrome")

        webDriver = RemoteWebDriver(URI.create("${ getRemoteTestingServer()}/wd/hub").toURL(), options.merge(caps))
        webDriver.manage().window().maximize()

        return webDriver
    }
}
