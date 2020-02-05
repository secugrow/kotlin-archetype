package at.co.boris.secuton.driverutil

import logger
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.DesiredCapabilities


abstract class WebDriverFactory {

    lateinit var webDriver: WebDriver
    abstract fun createDriver(): WebDriver
    protected val log by logger()
    val caps = DesiredCapabilities()
    lateinit var screenDimension: ScreenDimension

    init {
        val screenSize = System.getProperty("screen.size", ScreenDimension.Desktop_1080.toString())

        if (screenSize != null) {
            screenDimension = ScreenDimension.valueOf(screenSize)
        }
    }


    fun getDriverVersion(): String? {
        return System.getProperty("driver.version")
    }

    fun getBrowserVersion(): String? {
        return System.getProperty("browser.version")
    }


}