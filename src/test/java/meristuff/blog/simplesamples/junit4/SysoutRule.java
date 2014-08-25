package meristuff.blog.simplesamples.junit4;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class SysoutRule implements TestRule  {
  
  private String name;

  public SysoutRule(String name) {
    this.name = name;
  }

  public Statement apply(final Statement base, final Description description) {
    return new Statement() {
      
      @Override
      public void evaluate() throws Throwable {
        System.out.println("Before Rule: " + name);
        try {
          base.evaluate();
        } finally {
          System.out.println("After Rule: " + name);
        }
      }
    };
  }

}
