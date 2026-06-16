import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses(BSTTest.class)
@IncludeTags("advanced")
public class AdvancedSuite {
}