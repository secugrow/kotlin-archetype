package ${package}.webdriversession.webdriverfactory

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

class ChromeWebDriverFactory : WebDriverFactory() {

    override fun createDriver(): WebDriver {

        WebDriverManager.chromedriver().driverVersion(getDriverVersion()).setup()
        val options = ChromeOptions()

        webDriver = ChromeDriver(options.merge(caps))
        webDriver.manage().window().size = screenDimension.dimension

        return webDriver
    }


}
