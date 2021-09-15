package ${package}.driverutil

import logger
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.Point
import org.openqa.selenium.WebDriver
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import java.util.concurrent.TimeUnit

object DriverFactory {

    private const val TIMEOUT : Long = 10
    private val log by logger()

    fun createWebDriver(scenarioId: String): WebDriver {

        val browserName = System.getProperty("browser", DriverType.CHROME.toString()).toUpperCase()
        val driverType = DriverType.valueOf(browserName)


        val webDriver = when (driverType) {
            DriverType.CHROME -> ChromeWebDriverFactory().createDriver()
            DriverType.FIREFOX -> FirefoxWebDriverFactory().createDriver()
            DriverType.EDGE -> EdgeWebDriverFactory().createDriver()

            DriverType.IE -> IEWebDriverFactory().createDriver()
            DriverType.OPERA -> OperaWebDriverFactory().createDriver()
            DriverType.LOCAL_CHROME_MOBILE_EMULATION -> ChromeMobileEmulationWebDriverFactory().createDriver()

            /* Appium Implementations */
            DriverType.APPIUM_NATIVE_APP_ANDROID -> AppiumNativeAppAndroidDriverFactory().createDriver(2)

            /* REMOTE Implementations */

            DriverType.REMOTE_CHROME_MOBILE_EMULATION -> RemoteChromeMobileEmulationWebDriverFactory().createDriver()
            DriverType.REMOTE_OPERA -> RemoteOperaWebDriverFactory().createDriver()
            DriverType.REMOTE_CHROME -> RemoteChromeWebDriverFactory().createDriver()
            DriverType.REMOTE_FIREFOX -> RemoteFirefoxWebDriverFactory().createDriver()
            DriverType.REMOTE_CHROME_MOBILE -> RemoteChromeMobileWebDriverFactory().createDriver()
            DriverType.REMOTE_ANDROID -> RemoteAndroidWebDriverFactory().createDriver()
            DriverType.APPIUM_ANDROID_DEVICE -> AppiumAndroidWebDriverFactory().createDriver()
        }

        webDriver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS)
        return webDriver
    }

}
