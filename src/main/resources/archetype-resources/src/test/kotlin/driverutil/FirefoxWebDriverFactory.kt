package ${package}.driverutil

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions

class FirefoxWebDriverFactory : WebDriverFactory() {


    override fun createDriver(): WebDriver {

        WebDriverManager.firefoxdriver().driverVersion(getDriverVersion()).setup()

        webDriver = FirefoxDriver(FirefoxOptions())
        webDriver.manage().window().size = screenDimension.dimension

        return webDriver
    }

}
