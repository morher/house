package net.morher.house.api.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import lombok.Data;
import org.junit.Test;

public class ConfigurationLoaderTest {

  @Test
  public void loadValidConfigFile() {
    String validConfigFile =
        Thread.currentThread().getContextClassLoader().getResource("valid-config.yaml").getFile();

    TestConfiguration config =
        new ConfigurationLoader(validConfigFile).loadConfig(TestConfiguration.class);

    assertThat(config, is(not(nullValue())));
    assertThat(config.getTestField(), is(equalTo("This is just a test")));
  }

  @Test(expected = RuntimeException.class)
  public void loadInvalidConfigFile() {
    String validConfigFile =
        Thread.currentThread().getContextClassLoader().getResource("invalid-config.yaml").getFile();

    new ConfigurationLoader(validConfigFile).loadConfig(TestConfiguration.class);
  }

  @Test(expected = RuntimeException.class)
  public void loadUnavailableConfigFile() {
    new ConfigurationLoader("unavailable-config.yaml").loadConfig(TestConfiguration.class);
  }

  @Data
  public static class TestConfiguration {
    private String testField;
  }
}
