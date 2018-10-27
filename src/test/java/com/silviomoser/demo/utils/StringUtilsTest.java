package com.silviomoser.demo.utils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.*;

public class StringUtilsTest {

    @DataProvider(name = "testStripAllDp")
    public Object[][] testStripAllDp() {
        return new Object[][]{
                {new String[]{"test1", "test2"}, new String[]{"test1", "test2"}},
                {new String[]{"test1  ", "test2"}, new String[]{"test1", "test2"}},
                {new String[]{"   test1  ", "test2"}, new String[]{"test1", "test2"}},
                {null, null}
        };
    }

    @Test(dataProvider = "testStripAllDp")
    public void testStripAll(String[] input, String[] expectedOutput) {
        assertThat(StringUtils.stripAll(input)).isEqualTo(expectedOutput);
    }

    @DataProvider(name = "isBlankDp")
    public Object[][] isBlankDp() {
        return new Object[][]{
                {"test", false},
                {" ", true},
                {null, true}
        };
    }

    @Test(dataProvider = "isBlankDp")
    public void testIsBlank(String input, boolean expectedResult) {
        assertThat(StringUtils.isBlank(input)).isEqualTo(expectedResult);
    }

    @Test(dataProvider = "isBlankDp")
    public void testIsNotBlank(String input, boolean expectedResult) {
        assertThat(StringUtils.isNotBlank(input)).isNotEqualTo(expectedResult);
    }


    @DataProvider(name = "isValidEmailAddressDp")
    public Object[][] isValidEmailAddressDp() {
        return new Object[][]{
                {"test", false},
                {" ", false},
                {null, false},
                {"test@test", false},
                {"test@test.com", true}
        };
    }
    @Test(dataProvider = "isValidEmailAddressDp")
    public void testIsValidEmailAddress(String input, boolean expected) {
        assertThat(StringUtils.isValidEmailAddress(input)).isEqualTo(expected);
    }
}