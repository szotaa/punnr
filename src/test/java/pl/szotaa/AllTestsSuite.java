package pl.szotaa;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pl.szotaa.punnr.config.MvcConfigTest;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                MvcConfigTest.class
        }
)
public class AllTestsSuite {
}
