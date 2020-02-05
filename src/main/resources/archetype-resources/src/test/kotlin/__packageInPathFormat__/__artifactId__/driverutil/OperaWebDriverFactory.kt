package at.co.boris.secuton.driverutil

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.opera.OperaDriver
import org.openqa.selenium.opera.OperaOptions

class OperaWebDriverFactory : WebDriverFactory() {
    override fun createDriver(): WebDriver {

        val driverVersion = System.getProperty("driver.version")

        WebDriverManager.operadriver().version(driverVersion).setup()
        val options = OperaOptions()

        webDriver = OperaDriver(options)
        webDriver.manage().window().size = screenDimension.dimension

        return webDriver
    }

}
