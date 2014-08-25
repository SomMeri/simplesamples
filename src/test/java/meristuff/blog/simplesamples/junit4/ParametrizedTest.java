package meristuff.blog.simplesamples.junit4;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParametrizedTest {
  
  private String less;
  private String expectedCss;

  public ParametrizedTest(String less, String expectedCss, String name) {
    this.less = less;
    this.expectedCss = expectedCss;
  }
  
  @Parameters(name="Name: {2}")
  public static Iterable<Object[]> generateParameters() {
    List<Object[]> result = new ArrayList<Object[]>();
    result.add(new Object[] {"less", "css", "pass"});
    result.add(new Object[] {"less", "error", "fail"});
    return result;
  }
  
  @Test
  public void testCss() {
    String actualCss = compile(less);
    assertEquals(expectedCss, actualCss);
  }

  @Test
  public void testSourceMap() {
    String actualCss = compile(less);
    assertEquals(expectedCss, actualCss);
  }

  //dummy compile method
  private String compile(String less) {
    return "css"; 
  }  
}
