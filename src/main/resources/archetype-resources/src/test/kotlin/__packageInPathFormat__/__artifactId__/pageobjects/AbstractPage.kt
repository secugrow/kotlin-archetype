package ${package}.${artifactId}.pageobjects

import ${package}.${artifactId}.driverutil.WebDriverSession
import org.openqa.selenium.WebDriver


open class AbstractPage(val session: WebDriverSession) {

    protected val webDriver: WebDriver
        get() = session.webDriver


    init {
        session.activate(page = this)
    }


}
