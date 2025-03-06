package com.vlad.kuzhyr.driverservice.e2e.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/com/vlad/kuzhyr/driverservice/e2e/features",
    glue = {"com.vlad.kuzhyr.driverservice.e2e.steps",
        "com.vlad.kuzhyr.driverservice"},
    plugin = {"pretty", "html:target/cucumber-reports.html"}
)
public class RunCucumberTest {
}