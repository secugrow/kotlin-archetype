//package at.co.boris.secuton.pageobjects
package ${groupId}

import ${package}.driverutil.WebDriverSession
import org.openqa.selenium.WebDriver


open class AbstractPage(val session: WebDriverSession) {

    protected val webDriver: WebDriver
        get() = session.webDriver


    init {
        session.activate(page = this)
    }


}