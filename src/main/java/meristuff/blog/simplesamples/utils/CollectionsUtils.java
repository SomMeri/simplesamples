package meristuff.blog.simplesamples.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class CollectionsUtils {

  public static <Q> void replace(List<Q> inList, Q oldElement, List<Q> newElements) {
    int level = inList.indexOf(oldElement);
    inList.remove(level);
    inList.addAll(level, newElements);
  }

  @SuppressWarnings("unchecked")
  public static <T extends PubliclyCloneable> List<T> deeplyClonedList(List<T> list) {
    List<T> result = new ArrayList<T>();
    for (T t : list) {
      result.add((T)t.clone());
    }
    return result;
  }

  public static <K, T> Map<K, List<T>> deeplyClonedMapOfList(Map<K, List<T>> map) {
    Map<K, List<T>> result = new HashMap<K, List<T>>();
    for (Entry<K, List<T>> t : map.entrySet()) {
      result.put(t.getKey(), new ArrayList<T>(t.getValue()));
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public static <T extends PubliclyCloneable> LinkedList<T> deeplyClonedLinkedList(LinkedList<T> list) {
    LinkedList<T> result = new LinkedList<T>();
    for (T t : list) {
      result.add((T)t.clone());
    }
    return result;
  }

}
