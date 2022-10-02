package ${package}.step_definitions

import ${package}.driverutil.DriverType
import io.cucumber.java.Scenario
import java.util.*


@Suppress("UNCHECKED_CAST")
class TestDataContainer {
    private val testDataMap: MutableMap<String, Any> = mutableMapOf()

    init {
        testDataMap["testId"] = "init"
        testDataMap["initialized"] = false
    }

    fun getScenario() = testDataMap["scenario"] as Scenario
    fun getTestId() = testDataMap["testId"] as String
    fun getScenarioTags() = getScenario().sourceTagNames
    fun doA11YCheck() = getAs<Boolean>("skipA11Y").not()

    fun setScenario(scenario: Scenario) {
        testDataMap["scenario"] = scenario
        testDataMap["testId"] = extractTestIdFromScenarioName(scenario.name)
    }

    fun setTestData(key: String, value: Any) {
        testDataMap[key] = value
    }

    fun getTestData(key: String): Any? {
        if (testDataMap.containsKey(key)) {
            return testDataMap[key]!!
        }
        return null
    }

    fun getCurrentSessionId(): String {
        return getAs("session.id")
    }

    private fun getBrowser(): String {
        return getAs("browser")
    }

    private fun getBrowserType(): DriverType {
        return getAs("browser.type")
    }

    private fun getBrowserVersion(): String {
        return getAs("browser.version")
    }


    fun isMobile(): Boolean {
        return when {
            getBrowserVersion().contains("mobile") -> true
            getBrowser().contains("mobile") -> true
            getBrowserType() == DriverType.LOCAL_CHROME_MOBILE_EMULATION -> true
            getBrowserType() == DriverType.REMOTE_CHROME_MOBILE_EMULATION -> true
            getAs<Boolean>("mobileEmulation") -> true
            else -> false
        }
    }

    fun isMobileEmulation(): Boolean {
        return when (getBrowserType()) {
            DriverType.LOCAL_CHROME_MOBILE_EMULATION -> true
            DriverType.REMOTE_CHROME_MOBILE_EMULATION -> true
            else -> false
        }
    }

    fun needsInitializing(): Boolean = getAs("initialized")

    fun isLocalRun(): Boolean = getAs("localRun")


    fun <T> getAs(key: String): T {
        return if (testDataMap.containsKey(key)) {
            testDataMap[key] as T
        } else {
            throw IllegalArgumentException("given $key not available in testDataMap")
        }
    }

    fun getSoftAssertionObject(): SoftAssertions {
        return testDataMap["softAssertion.object"] as SoftAssertions
    }

}
