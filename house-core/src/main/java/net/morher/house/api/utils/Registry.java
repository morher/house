package net.morher.house.api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

@RequiredArgsConstructor
public class Registry<ID, T> {
  private final Function<ID, T> itemFactory;
  private final Map<ID, T> items = new ConcurrentHashMap<>();

  @Synchronized
  public T get(ID id) {
    if (id == null) {
      return null;
    }
    T item = items.get(id);
    if (item == null) {
      item = itemFactory.apply(id);
      items.put(id, item);
    }
    return item;
  }

  public boolean hasItem(ID id) {
    return items.containsKey(id);
  }

  public List<T> items() {
    return new ArrayList<>(items.values());
  }

  public void remove(ID id) {
    items.remove(id);
  }
}
