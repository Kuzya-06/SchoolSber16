package ru.sbrf.course;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Categories.class)
@IncludeCategory(SlowTests.class)
@SuiteClasses({CalculatorTest.class, OrderControllerTest.class})
public class SlowTestsSuite {
    // OrderControllerTest.testAlwaysCheckItemDiscount + all tests in CalculatorTest
}
