#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )

package ${package}

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URI

class RemoteOperaWebDriverFactory : RemoteWebDriverFactory() {

    override fun createDriver(): WebDriver {

        caps.browserName = "opera"

        //  https://stackoverflow.com/a/29241407/6870790
        val options = ChromeOptions()

        //  https://aerokube.com/selenoid/latest/#_opera
        options.setBinary("/usr/bin/opera")

        options.merge(caps)

        webDriver = RemoteWebDriver(URI.create("$dollar$curlyOpen getRemoteTestingServer$bracketOpen$bracketClose$curlyClose/wd/hub").toURL(), options)
        webDriver.manage().window().maximize()

        return webDriver
    }

}
