package com.silviomoser.demo.utils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StaticFileUtilsTest {

    @DataProvider(name = "testAddTrailingSlashDp")
    public Object[][] testAddTrailingSlashDp() {
        return new Object[][]{
                {null, "/"},
                {"", "/"},
                {"  ", "/"},
                {"/test", "/test/"},
                {"/test/", "/test/"}
        };
    }

    @Test(dataProvider = "testAddTrailingSlashDp")
    public void testAddTrailingSlash(String input, String expected) {
        assertThat(StaticFileUtils.addTrailingSlash(input)).isEqualTo(expected);
    }

}