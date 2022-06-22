package ${package}.webdriversession.webdriverfactory

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

class ChromiumWebDriverFactory : WebDriverFactory() {

    override fun createDriver(): WebDriver {

        WebDriverManager.chromiumdriver().driverVersion(getDriverVersion()).setup()
        val options = ChromeOptions()

        webDriver = ChromeDriver(options)
        webDriver.manage().window().size = screenDimension.dimension

        return webDriver
    }


}
