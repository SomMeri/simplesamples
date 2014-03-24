package meristuff.blog.simplesamples.placeholderedkeystorage;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import meristuff.blog.simplesamples.placeholderedkeystorage.KeyListStorage;
import meristuff.blog.simplesamples.placeholderedkeystorage.KeyListStorage.ListPlaceholder;

import org.junit.Test;

public class KeyListStorageTest {

  @Test
  public void testEmpty() {
    KeyListStorage<String, String> storage = new KeyListStorage<String, String>();
    assertTrue(storage.getValues("unavailable").isEmpty());
  }

  @Test
  public void testAddGet() {
    KeyListStorage<String, String> storage = new KeyListStorage<String, String>();
    storage.add("first", "1");
    storage.add("second", "only one");
    storage.add("first", "2");
    assertListsEquals(Arrays.asList("1", "2"), storage.getValues("first"));
    assertOneMemberList("only one", storage.getValues("second"));
  }

  @Test
  public void testAddGetPlaceholder() {
    KeyListStorage<String, String> storage = new KeyListStorage<String, String>();
    storage.add("first", "1");
    storage.add("second", "only one");
    ListPlaceholder<String, String> placeholder = storage.createPlaceholder();
    storage.add("first", "2");

    //add data into placeholder
    storage.add(placeholder, "first", Arrays.asList("placeholder"));
    storage.add(placeholder, "second", Arrays.asList("placeholder"));

    //test content 
    assertListsEquals(Arrays.asList("1", "placeholder", "2"), storage.getValues("first"));
    assertListsEquals(Arrays.asList("only one", "placeholder"), storage.getValues("second"));
  }

  @Test
  public void testContains() {
    KeyListStorage<String, String> storage = new KeyListStorage<String, String>();
    storage.add("available", "value");
    assertFalse(storage.contains("unavailable"));
    assertTrue(storage.contains("available"));
  }

  @Test
  public void testPlaceholderTracking() {
    KeyListStorage<String, String> storage = new KeyListStorage<String, String>();
    storage.add("first", "1");
    storage.createPlaceholder();
    storage.add("second", "1");
    storage.createPlaceholder();

    //read data 
    assertOneMemberList("1", storage.getValues("first"));
    assertOneMemberList("1", storage.getValues("second"));

    //add data into placeholder and close it
    storage.addToFirstPlaceholder("first", "placeholder");
    storage.addToFirstPlaceholder("second", "placeholder");
    storage.closeFirstPlaceholder();

    //read data
    assertListsEquals(Arrays.asList("1", "placeholder"), storage.getValues("first"));
    assertListsEquals(Arrays.asList("placeholder", "1"), storage.getValues("second"));

    //add data into placeholder and close it
    storage.addToFirstPlaceholder("first", "second placeholder");
    storage.addToFirstPlaceholder("second", "second placeholder");

    //read data
    assertListsEquals(Arrays.asList("1", "placeholder", "second placeholder"), storage.getValues("first"));
    assertListsEquals(Arrays.asList("placeholder", "1", "second placeholder"), storage.getValues("second"));
  }

  @Test
  public void testReplacePlaceholder() {
    // create storage with one placeholder
    KeyListStorage<String, String> other = new KeyListStorage<String, String>();
    other.add("under", "other placeholder");
    other.createPlaceholder();
    other.add("above", "other placeholder");

    // create main storage with one placeholder
    KeyListStorage<String, String> main = new KeyListStorage<String, String>();
    ListPlaceholder<String, String> holder = main.createPlaceholder();

    //replace main placeholder
    main.replacePlaceholder(holder, other);
    //data from other storage are availabe inside main
    assertOneMemberList("other placeholder", main.getValues("under"));
    assertOneMemberList("other placeholder", main.getValues("above"));

    //placeholder from other storage is available
    main.addToFirstPlaceholder("under", "later on");
    main.addToFirstPlaceholder("above", "later on");
    assertListsEquals(Arrays.asList("other placeholder", "later on"), main.getValues("under"));
    assertListsEquals(Arrays.asList("later on", "other placeholder"), main.getValues("above"));
  }

  @Test
  public void testReplacePlaceholderFull() {
    // create storage with one placeholder
    KeyListStorage<String, String> other = new KeyListStorage<String, String>();
    other.add("under", "other placeholder");
    other.createPlaceholder();
    other.add("above", "other placeholder");

    // create main storage with one placeholder
    KeyListStorage<String, String> main = new KeyListStorage<String, String>();
    main.add("under", "in main");
    ListPlaceholder<String, String> holder = main.createPlaceholder();
    assertOneMemberList("in main", main.getValues("under"));

    // replace placeholder in main storage and test data
    main.replacePlaceholder(holder, other);
    assertListsEquals(Arrays.asList("in main", "other placeholder"), main.getValues("under"));
    assertListsEquals(Arrays.asList("other placeholder"), main.getValues("above"));

    //check that the placeholder from other storage is still available
    main.addToFirstPlaceholder("under", "later on");
    main.addToFirstPlaceholder("above", "later on");
    assertListsEquals(Arrays.asList("in main", "other placeholder", "later on"), main.getValues("under"));
    assertListsEquals(Arrays.asList("later on", "other placeholder"), main.getValues("above"));
  }

  @Test
  public void testClone() {
    // create storage with one placeholder
    KeyListStorage<String, String> original = new KeyListStorage<String, String>();
    original.createPlaceholder();
    original.add("key", "original");

    //clone contains the same key value pairs as original storage
    KeyListStorage<String, String> clone = original.clone();
    assertListsEquals(original.getValues("key"), clone.getValues("key"));

    //modifying original storage will not modify clone
    original.add("key", "modified");
    original.addToFirstPlaceholder("key", "placeholder");
    assertOneMemberList("original", clone.getValues("key"));
    assertListsEquals(Arrays.asList("placeholder", "original", "modified"), original.getValues("key"));

    //placeholder was cloned too
    clone.addToFirstPlaceholder("key", "clone has placeholder");
    assertListsEquals(Arrays.asList("clone has placeholder", "original"), clone.getValues("key"));
  }

  @SuppressWarnings("rawtypes")
  static public void assertOneMemberList(Object member, List list) {
    assertListsEquals(Arrays.asList(member), list);
  }

  @SuppressWarnings("rawtypes")
  static public void assertListsEquals(List expected, List actual) {
    if (expected == null || actual == null)
      if (expected != null || actual != null)
        fail("One list is null, other is not.");

    assertEquals("lists length", expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
  }

  @Test
  public void testPlaceholdersManagement() {
    // create structure with two placeholders
    KeyListStorage<String, String> storage = new KeyListStorage<String, String>();
    storage.add("key", "1");
    storage.createPlaceholder();
    storage.add("lock", "1");
    storage.createPlaceholder();

    //add data into first placeholder
    storage.addToFirstPlaceholder("key", "placeholder");
    storage.addToFirstPlaceholder("lock", "placeholder");

    //read data
    assertListsEquals(Arrays.asList("1", "placeholder"), storage.getValues("key"));
    assertListsEquals(Arrays.asList("placeholder", "1"), storage.getValues("lock"));

    // close first placeholder - its data are still available
    storage.closeFirstPlaceholder();
    assertListsEquals(Arrays.asList("1", "placeholder"), storage.getValues("key"));

    //add data into next open placeholder
    storage.addToFirstPlaceholder("lock", "second placeholder");
    assertListsEquals(Arrays.asList("placeholder", "1", "second placeholder"), storage.getValues("lock"));
  }

  @Test
  public void testBulkModifications() {
    // create storage with one placeholder
    KeyListStorage<String, String> other = 
        new KeyListStorage<String, String>();
    other.add("under", "other placeholder");
    other.createPlaceholder();
    other.add("above", "other placeholder");

    // create main storage with one placeholder
    KeyListStorage<String, String> target = 
        new KeyListStorage<String, String>();
    ListPlaceholder<String, String> holder = target.createPlaceholder();

    //replace target placeholder
    target.replacePlaceholder(holder, other);
    //data from other storage are availabe inside main
    assertOneMemberList("other placeholder", target.getValues("under"));
    assertOneMemberList("other placeholder", target.getValues("above"));

    //placeholder from other storage is available
    target.addToFirstPlaceholder("other placeholder", "later on");
    target.addToFirstPlaceholder("above", "later on");
    assertListsEquals(Arrays.asList("other placeholder"), target.getValues("under"));
    assertListsEquals(Arrays.asList("later on", "other placeholder"), target.getValues("above"));
  }

}
