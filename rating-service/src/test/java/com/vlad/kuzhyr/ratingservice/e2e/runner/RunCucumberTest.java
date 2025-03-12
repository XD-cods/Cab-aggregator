package com.vlad.kuzhyr.ratingservice.e2e.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/java/com/vlad/kuzhyr/ratingservice/e2e/features",
    glue = {"com.vlad.kuzhyr.ratingservice.e2e.step", "com.vlad.kuzhyr.ratingservice.config"},
    plugin = {"pretty", "html:target/cucumber-reports.html"}
)
public class RunCucumberTest {
}