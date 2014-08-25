package meristuff.blog.simplesamples.junit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;


public class OrderReportingTest {
  
  @Rule
  public SysoutRule testRule = new SysoutRule("test");

  @ClassRule
  public static SysoutRule classRule = new SysoutRule("class");

  @Before
  public void beforeTest() {
    System.out.println("before test");
  }

  @After
  public void afterTest() {
    System.out.println("after test");
  }

  @BeforeClass
  public static void beforeClass() {
    System.out.println("before class");
  }

  @AfterClass
  public static void afterClass() {
    System.out.println("after class");
  }

  @Test
  public void example() {
    System.out.println("test");
  }

}
