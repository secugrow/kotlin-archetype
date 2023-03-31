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

class RemoteOperaWebDriverFactory : RemoteWebDriverFactory() {

    override fun createDriver(): WebDriver {

        //  https://stackoverflow.com/a/29241407/6870790
        val options = ChromeOptions().merge(caps)
        options.setCapability(CapabilityType.BROWSER_NAME, "opera")

        //  https://aerokube.com/selenoid/latest/#_opera
        options.setBinary("/usr/bin/opera")

        webDriver = RemoteWebDriver(URI.create("$dollar$curlyOpen getRemoteTestingServer$bracketOpen$bracketClose$curlyClose/wd/hub").toURL(), options)
        webDriver.manage().window().maximize()

        return webDriver
    }

}
