#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.pageobjects;

import ${package}.driverutil.WebDriverSession;
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions

class WikipediaContentPage(session: WebDriverSession) : MainPage(session) {
    val header: String
        get() = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading"))).getText()
}