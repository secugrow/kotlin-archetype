package at.co.boris.secuton.step_definitions

import at.co.boris.secuton.driverutil.DriverType
import io.cucumber.core.api.Scenario
import java.util.*


class TestDataContainer {
    private val testDataMap: MutableMap<String, Any> = mutableMapOf()

    init {
        testDataMap["testId"] = "init"
    }

    fun getScenario() = testDataMap["scenario"] as Scenario
    fun getTestId() = testDataMap["testId"] as String
    fun getScenarioTags() = getScenario().sourceTagNames

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
        return testDataMap["session.id"] as String
    }

    private fun getBrowser(): String {
        return testDataMap["browser"] as String
    }

    private fun getBrowserType(): DriverType {
        return DriverType.valueOf(testDataMap.get("browser.type") as String)  as DriverType
    }

    private fun getBrowserVersion(): String {
        return testDataMap["browser.version"] as String
    }


    fun isMobile(): Boolean {
        if (getBrowserVersion().contains("mobile")) {
            return true
        }
        if (getBrowser().contains("mobile")) {
            return true
        }

        if (getBrowserType() == DriverType.LOCAL_CHROME_MOBILE_EMULATION) {
            return true
        }

        if (getBrowserType() == DriverType.REMOTE_CHROME_MOBILE_EMULATION) {
            return true
        }

        if (testDataMap["mobileEmulation"] as Boolean) {
            return true
        }

        return false
    }

    fun isMobileEmulation(): Boolean {

        if (testDataMap.containsKey("mobileEmulation")) {
            return testDataMap["mobileEmulation"] as Boolean
        }
        return false
//        return when (getBrowserType()) {
//            DriverType.LOCAL_CHROME_MOBILE_EMULATION -> true
//            DriverType.REMOTE_CHROME_MOBILE_EMULATION -> true
//            else -> false
//        }
    }

    fun needsInitializing(): Boolean {
        val init = (testDataMap["initialized"] as Boolean)
        return !init
    }

    fun isLocalRun(): Boolean {
        return (testDataMap["localRun"] as Boolean)
    }

}