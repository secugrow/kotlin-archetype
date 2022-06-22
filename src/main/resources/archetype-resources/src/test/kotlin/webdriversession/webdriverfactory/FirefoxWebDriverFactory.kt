package ${package}.driverutil

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions

class FirefoxWebDriverFactory : WebDriverFactory() {


    override fun createDriver(): WebDriver {

        WebDriverManager.firefoxdriver().driverVersion(getDriverVersion()).setup()
        val ffOptions = FirefoxOptions()

        webDriver = FirefoxDriver(ffOptions.merge(caps))
        webDriver.manage().window().size = screenDimension.dimension

        return webDriver
    }

}
