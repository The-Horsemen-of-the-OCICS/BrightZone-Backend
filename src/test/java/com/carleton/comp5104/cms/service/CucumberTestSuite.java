package com.carleton.comp5104.cms.service;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:com/carleton/comp5104/cms/cucumberfeature"},
        glue = {"com.carleton.comp5104.cms.cucumber"})
public class CucumberTestSuite {
}
