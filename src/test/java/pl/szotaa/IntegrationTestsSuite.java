package pl.szotaa;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory(IntegrationTest.class)
@Suite.SuiteClasses({AllTestsSuite.class})
public class IntegrationTestsSuite {
}
