package ${package}.pageobjects

import ${package}.driverutil.WebDriverSession
import ${package}.pageobjects.AbstractPage
import ${package}.pageobjects.MainPage
import org.openqa.selenium.By

class WikipediaPage(session: WebDriverSession) : MainPage(session)  {

    fun isSearchbarPresent(): Boolean {
        return webDriver.findElements(By.id("searchInput")).size >= 1
    }


}
