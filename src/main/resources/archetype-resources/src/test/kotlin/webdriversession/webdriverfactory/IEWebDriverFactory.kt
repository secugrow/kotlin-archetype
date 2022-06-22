package ${package}.webdriversession.webdriverfactory

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.ie.InternetExplorerOptions

class IEWebDriverFactory : WebDriverFactory(){

    override fun createDriver(): WebDriver {
        WebDriverManager.iedriver().driverVersion(getDriverVersion()).setup()
        val options = InternetExplorerOptions()

        webDriver = InternetExplorerDriver(options)
        webDriver.manage().window().size = screenDimension.dimension

        return webDriver
    }

}
