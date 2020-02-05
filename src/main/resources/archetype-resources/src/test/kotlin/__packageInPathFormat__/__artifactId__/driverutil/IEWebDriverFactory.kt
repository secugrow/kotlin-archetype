package at.co.boris.secuton.driverutil

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.ie.InternetExplorerOptions

class IEWebDriverFactory : WebDriverFactory(){
    override fun createDriver(): WebDriver {

        //FIXME Hardcoded Version!
        WebDriverManager.iedriver().version("3.9.0").setup()
        val options = InternetExplorerOptions()

        webDriver = InternetExplorerDriver(options)
        webDriver.manage().window().size = screenDimension.dimension

        return webDriver
    }

}
