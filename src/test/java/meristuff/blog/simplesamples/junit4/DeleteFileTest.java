package meristuff.blog.simplesamples.junit4;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;


public class DeleteFileTest {
  
  @Rule
  public DeleteFilesRule toDelete = new DeleteFilesRule();

  @Test
  public void example() throws IOException {
    // register file to be deleted
    toDelete.ensureRemoval("output.css");
    // do the test as usual
    compileFile("input.less");
    checkCorrectess("output.css");
  }

  private void checkCorrectess(String string) {
    throw new RuntimeException("Simulating unexpected exception.");
  }

  private void compileFile(String string) throws IOException {
    File file = new File("output.css");
    file.createNewFile();
  }  
}
