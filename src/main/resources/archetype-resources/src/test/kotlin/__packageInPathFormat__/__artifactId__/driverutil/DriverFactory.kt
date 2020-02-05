package at.co.boris.secuton.driverutil

import logger
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.Point
import org.openqa.selenium.WebDriver
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import java.util.concurrent.TimeUnit

object DriverFactory {

    private val log by logger()

    fun createWebDriver(scenarioId: String): WebDriver {

        val webDriver: WebDriver
        val browserName = System.getProperty("browser", DriverType.CHROME.toString()).toUpperCase()
        val driverType = DriverType.valueOf(browserName)


        when (driverType) {
            DriverType.CHROME -> { //checked
                webDriver = ChromeWebDriverFactory().createDriver()
            }
            DriverType.FIREFOX -> { //checked
                webDriver = FirefoxWebDriverFactory().createDriver()
            }
            DriverType.EDGE -> { //checked
                webDriver = EdgeWebDriverFactory().createDriver()
            }
            DriverType.IE -> {
                webDriver = IEWebDriverFactory().createDriver()
            }
            DriverType.OPERA -> {
                webDriver = OperaWebDriverFactory().createDriver()
            }

            DriverType.LOCAL_CHROME_MOBILE_EMULATION -> { //checked
                webDriver = ChromeMobileEmulationWebDriverFactory().createDriver()
            }
            /* REMOTE Implementations */

            DriverType.REMOTE_CHROME_MOBILE_EMULATION -> { //checked
                webDriver = RemoteChromeMobileEmulationWebDriverFactory().createDriver()
            }
            DriverType.REMOTE_OPERA -> { //checked
                webDriver = RemoteOperaWebDriverFactory().createDriver()
            }
            DriverType.REMOTE_CHROME -> { //checked
                webDriver = RemoteChromeWebDriverFactory().createDriver()
            }
            DriverType.REMOTE_FIREFOX -> { //checked
                webDriver = RemoteFirefoxWebDriverFactory().createDriver()
            }
            DriverType.REMOTE_CHROME_MOBILE -> {
                webDriver = RemoteChromeMobileWebDriverFactory().createDriver()
            }
            DriverType.REMOTE_ANDROID -> {
               webDriver = RemoteAndroidWebDriverFactory().createDriver()
            }
            DriverType.APPIUM_ANDROID_DEVICE -> {
                webDriver = AppiumAndroidWebDriverFactory().createDriver()
            }
        }

        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)

        return webDriver
    }

}