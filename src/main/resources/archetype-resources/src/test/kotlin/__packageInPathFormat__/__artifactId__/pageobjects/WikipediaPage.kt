package ${package}.${artifactId}.pageobjects

import ${package}.${artifactId}.driverutil.WebDriverSession
import ${package}.${artifactId}.pageobjects.AbstractPage
import ${package}.${artifactId}.pageobjects.MainPage
import org.openqa.selenium.By

class WikipediaPage(session: WebDriverSession) : MainPage(session)  {

    fun isSearchbarPresent(): Boolean {
        return webDriver.findElements(By.id("searchInput")).size >= 1
    }


}