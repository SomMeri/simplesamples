package meristuff.blog.simplesamples.placeholderedkeystorage;

import static org.junit.Assert.*;

import meristuff.blog.simplesamples.placeholderedkeystorage.KeyValueStorage.ValuePlaceholder;

import org.junit.Test;

public class KeyValueStorageTest {

  @Test
  public void testEmpty() {
    KeyValueStorage<String, String> storage = new KeyValueStorage<String, String>();
    assertNull(storage.getValue("unavailable"));
  }

  @Test
  public void testAddGet() {
    KeyValueStorage<String, String> storage = new KeyValueStorage<String, String>();
    storage.add("first", "rewritten");
    storage.add("second", "only one");
    storage.add("first", "winner");
    assertEquals("winner", storage.getValue("first"));
    assertEquals("only one", storage.getValue("second"));
  }

  @Test
  public void testAddGetPlaceholder() {
    KeyValueStorage<String, String> storage = new KeyValueStorage<String, String>();
    storage.add("first", "rewritten");
    storage.add("second", "only one");
    ValuePlaceholder<String, String> placeholder = storage.createPlaceholder();
    storage.add("first", "winner");
    
    //add data into placeholder
    storage.add(placeholder, "first", "placeholder");
    storage.add(placeholder, "second", "placeholder");

    //the value associated with "first" key was added after placeholder 
    assertEquals("winner", storage.getValue("first"));
    //the value associated with "second" key was added before placeholder 
    assertEquals("placeholder", storage.getValue("second"));
  }

  @Test
  public void testContains() {
    KeyValueStorage<String, String> storage = new KeyValueStorage<String, String>();
    storage.add("available", "value");
    assertFalse(storage.contains("unavailable"));
    assertTrue(storage.contains("available"));
  }


  @Test
  public void testPlaceholderTracking() {
    KeyValueStorage<String, String> storage = new KeyValueStorage<String, String>();
    storage.add("first", "1");
    storage.createPlaceholder();
    storage.add("second", "1");
    storage.createPlaceholder();
    
    //read data 
    assertEquals("1", storage.getValue("first"));
    assertEquals("1", storage.getValue("second"));

    //add data into placeholder and close it
    storage.addToFirstPlaceholder("first", "placeholder");
    storage.addToFirstPlaceholder("second", "placeholder");
    storage.closeFirstPlaceholder();
    
    //read data
    assertEquals("placeholder", storage.getValue("first"));
    assertEquals("1", storage.getValue("second"));
    
    //add data into placeholder and close it
    storage.addToFirstPlaceholder("first", "second placeholder");
    storage.addToFirstPlaceholder("second", "second placeholder");

    //read data
    assertEquals("second placeholder", storage.getValue("first"));
    assertEquals("second placeholder", storage.getValue("second"));
  }

  @Test
  public void testReplacePlaceholder() {
    // create storage with one placeholder
    KeyValueStorage<String, String> other = new KeyValueStorage<String, String>();
    other.add("under", "other placeholder");
    other.createPlaceholder();
    other.add("above", "other placeholder");
    
    // create main storage with one placeholder
    KeyValueStorage<String, String> main = new KeyValueStorage<String, String>();
    ValuePlaceholder<String, String> holder = main.createPlaceholder();

    //replace main placeholder
    main.replacePlaceholder(holder, other);
    //data from other storage are availabe inside main
    assertEquals("other placeholder", main.getValue("under"));
    assertEquals("other placeholder", main.getValue("above"));

    //placeholder from other storage is available
    main.addToFirstPlaceholder("under", "later on");
    main.addToFirstPlaceholder("above", "later on");
    assertEquals("later on", main.getValue("under"));
    assertEquals("other placeholder", main.getValue("above"));
  }

  @Test
  public void testReplacePlaceholderFull() {
    // create storage with one placeholder
    KeyValueStorage<String, String> other = new KeyValueStorage<String, String>();
    other.add("under", "other placeholder");
    other.createPlaceholder();
    other.add("above", "other placeholder");
    
    // create main storage with one placeholder
    KeyValueStorage<String, String> main = new KeyValueStorage<String, String>();
    main.add("under", "in main");
    ValuePlaceholder<String, String> holder = main.createPlaceholder();
    assertEquals("in main", main.getValue("under"));
    
    // replace placeholder in main storage and test data
    main.replacePlaceholder(holder, other);
    assertEquals("other placeholder", main.getValue("under"));
    assertEquals("other placeholder", main.getValue("above"));

    //check that the placeholder from other storage is still available
    main.addToFirstPlaceholder("under", "later on");
    main.addToFirstPlaceholder("above", "later on");
    assertEquals("later on", main.getValue("under"));
    assertEquals("other placeholder", main.getValue("above"));
  }


  @Test
  public void testClone() {
    // create storage with one placeholder
    KeyValueStorage<String, String> original = new KeyValueStorage<String, String>();
    original.add("key", "original");
    original.createPlaceholder();
    
    //clone contains the same key value pairs as original storage
    KeyValueStorage<String, String> clone = original.clone();
    assertEquals("original", clone.getValue("key"));
    
    //modifying original storage will not modify clone
    original.add("key", "modified");
    original.addToFirstPlaceholder("key", "placeholder");
    assertEquals("original", clone.getValue("key"));
    
    //placeholder was cloned too
    clone.addToFirstPlaceholder("key", "clone has placeholder");
    assertEquals("clone has placeholder", clone.getValue("key"));
  }
}
