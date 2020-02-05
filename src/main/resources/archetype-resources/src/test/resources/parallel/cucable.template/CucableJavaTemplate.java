#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"target/parallel/features/[CUCABLE:FEATURE].feature"}, plugin = {"json:target/cucumber-report/[CUCABLE:RUNNER].json"})
public class CucableJavaTemplate {

}