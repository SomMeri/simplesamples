package meristuff.blog.simplesamples.junit4;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DeleteFilesRule implements TestRule  {
  
  private List<File> toDelete;
  
  public void ensureRemoval(String... filenames) {
    for (String filename : filenames) {
      toDelete.add(new File(filename));
    }
  }
  
  public void ensureRemoval(File... files) {
    for (File file : files) {
      toDelete.add(file);
    }
  }
  
  public Statement apply(final Statement base, final Description description) {
    return new Statement() {
      
      @Override
      public void evaluate() throws Throwable {
        toDelete = new ArrayList<File>();
        try {
          base.evaluate();
        } finally {
          removeAll();
        }
      }
    };
  }

  private void removeAll() {
    for (File file : toDelete) {
      if (file.exists())
        file.delete();
    }
  }

}
