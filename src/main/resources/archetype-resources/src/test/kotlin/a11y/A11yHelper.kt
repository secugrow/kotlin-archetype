package ${package}.a11y

import com.deque.html.axecore.args.AxeRunOptions
import com.deque.html.axecore.results.Rule
import com.deque.html.axecore.selenium.AxeBuilder
import logger
import org.openqa.selenium.WebDriver
import kotlin.system.measureTimeMillis

class A11yHelper {

   companion object {

      private val log by logger()

      fun hasAccessibilityIssues(driver: WebDriver?, a11yExclusions: List<String>): List<Rule> {
         var violations: List<Rule>
         val ruleSet = listOf("wcag2a", "wcag2aa", "wcag21a", "wcag21aa")
         measureTimeMillis {

            val axeRunner = AxeBuilder()
               .withOptions(AxeRunOptions())
               .withTags(ruleSet)
               .disableRules(a11yExclusions)
            violations = axeRunner.analyze(driver).violations
         }.also { log.debug("AXE took $it ms") }

         return if (violations.isNotEmpty()) {
            violations
         } else {
            emptyList()
         }
      }

   }
}
