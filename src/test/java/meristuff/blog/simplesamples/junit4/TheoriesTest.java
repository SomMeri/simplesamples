package meristuff.blog.simplesamples.junit4;

import org.junit.Assume;
import static org.junit.Assert.*;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class TheoriesTest {

  @DataPoints
  public static int[] integers() {
    return new int[] { -1, -10, -1234567, 1, 10, 1234567 };
  }

  @DataPoints
  public static String[] strings() {
    return new String[] { "aa", "bb" };
  }

  @DataPoints
  public static String[] strings2() {
    return new String[] { "11111", "22222" };
  }

  @Theory
  public void a_plus_b_is_greater_than_a_and_greater_than_b(Integer a, Integer b, String aaa, String ccc) {
    //System.out.println(aaa + " " + ccc  + " " + a + " " + b);
    Assume.assumeTrue(a > 0 && b > 0);
    assertTrue(a + b > a);
    assertTrue(a + b > b);
  }

  @Theory
  public void addition_is_commutative(Integer a, Integer b) {
    assertTrue(a + b == b + a);
  }
}