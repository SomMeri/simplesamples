package meristuff.blog.simplesamples.junit4;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.rules.ExternalResource;

public class DeleteFilesRule2 extends ExternalResource {
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
  
  private void removeAll() {
    for (File file : toDelete) {
      if (file.exists())
        file.delete();
    }
  }

  @Override
  protected void before() throws Throwable {
    toDelete = new ArrayList<File>();
  }

  @Override
  protected void after() {
    removeAll();
  }

}
