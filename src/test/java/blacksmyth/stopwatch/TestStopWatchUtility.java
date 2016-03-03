package blacksmyth.stopwatch;

import junit.framework.*;

/**
 *
 * @author  Lindsay Bradford
 * @version 1.0
 *
 */

public class TestStopWatchUtility extends TestCase {

  public TestStopWatchUtility(String pName) {
    super(pName);
  }
  
  public static void main(String args[]) {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {
    TestSuite vFullSuite = new TestSuite("All \"StopWatch Utility\" Tests");
    return vFullSuite;
  }
}
