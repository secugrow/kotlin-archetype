package ${package}.driverutil

import org.openqa.selenium.WebDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.ie.InternetExplorerOptions

class IEWebDriverFactory : WebDriverFactory(){

    override fun createDriver(): WebDriver {
        val options = InternetExplorerOptions()

        webDriver = InternetExplorerDriver(options)
        webDriver.manage().window().size = screenDimension.dimension

        return webDriver
    }
}
