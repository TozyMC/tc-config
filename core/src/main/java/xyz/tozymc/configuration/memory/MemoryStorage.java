package xyz.tozymc.configuration.memory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import xyz.tozymc.configuration.exception.TcConfigException;
import xyz.tozymc.configuration.exception.TcConfigSerializationException;
import xyz.tozymc.configuration.serialization.TcConfigSerializations;
import xyz.tozymc.configuration.util.SectionPaths;

class MemoryStorage {
  private static final char PATH_SEPARATOR = '.';

  final LinkedHashMap<String, Object> values = new LinkedHashMap<>();
  private final MemoryConfigSection section;

  private Set<String> keySet;
  private Map<String, ?> cachedValues;

  MemoryStorage(MemoryConfigSection section) {this.section = section;}

  private static int getPathSepIndex(String path) {return path.indexOf(PATH_SEPARATOR);}

  private static String getFirstPathNode(String path, int pathSepInd) {
    return path.substring(0, pathSepInd);
  }

  private static String trimFirstPathNode(String path, int pathSepInd) {
    return path.substring(pathSepInd + 1);
  }

  Object get(String path) {
    if (path.isEmpty()) {
      return null;
    }

    var pathSepInd = getPathSepIndex(path);
    if (pathSepInd < 0) {
      return values.get(path);
    }

    var firstNode = getFirstPathNode(path, pathSepInd);
    var val = values.get(firstNode);
    if (!(val instanceof MemoryConfigSection)) {
      throw new TcConfigException(
          SectionPaths.createPath(section, firstNode) + " is not a section");
    }

    return ((MemoryConfigSection) val).storage.get(trimFirstPathNode(path, pathSepInd));
  }

  Object set(String path, boolean absent, Object newVal) {
    if (path.isEmpty()) {
      return null;
    }

    var pathSepInd = getPathSepIndex(path);
    if (pathSepInd < 0) {
      if (absent) {
        if (values.containsKey(path)) {
          return null;
        }
      }
      return setShallow(path, newVal);
    }

    var firstNode = getFirstPathNode(path, pathSepInd);
    var val = values.get(firstNode);
    if (val == null) {
      val = createShallowSection(path);
    }

    if (!(val instanceof MemoryConfigSection)) {
      throw new TcConfigException(
          SectionPaths.createPath(section, firstNode) + " is not a section");
    }
    return ((MemoryConfigSection) val).storage.set(trimFirstPathNode(path, pathSepInd), absent,
        newVal);
  }

  private Object setShallow(String path, Object value) {
    if (value instanceof Map) {
      //noinspection unchecked
      return createSection(path, true, (Map<String, ?>) value);
    }
    try {
      var serialized = TcConfigSerializations.serializeObject(value);
      return createSection(path, true, serialized);
    } catch (TcConfigSerializationException ignored) {
    }
    if (value.getClass().isArray()) {
      return setArrayValue(path, value);
    }
    return setValue(path, value);
  }

  private Object setValue(String key, Object value) {
    if (value == null) {
      return values.remove(key);
    }
    return values.put(key, value);
  }

  private Object setArrayValue(String key, Object value) {
    if (value == null) {
      return values.remove(key);
    }
    if (value instanceof byte[]) {
      return values.put(key, List.of((byte[]) value));
    }
    if (value instanceof short[]) {
      return values.put(key, List.of((short[]) value));
    }
    if (value instanceof int[]) {
      return values.put(key, List.of((int[]) value));
    }
    if (value instanceof long[]) {
      return values.put(key, List.of((long[]) value));
    }
    if (value instanceof float[]) {
      return values.put(key, List.of((float[]) value));
    }
    if (value instanceof double[]) {
      return values.put(key, List.of((double[]) value));
    }
    if (value instanceof char[]) {
      return values.put(key, List.of((char[]) value));
    }
    if (value instanceof boolean[]) {
      return values.put(key, List.of((boolean[]) value));
    }
    return values.put(key, mappedArrayValue((Object[]) value));
  }

  private List<Object> mappedArrayValue(Object[] values) {
    var list = new LinkedList<>();
    for (var value : values) {
      try {
        var serialized = TcConfigSerializations.serializeObject(value);
        list.add(serialized);
      } catch (TcConfigSerializationException ignored) {
        list.add(value);
      }
    }
    return list;
  }

  MemoryConfigSection createShallowSection(String path) {
    var child = section.newSection(section, path);
    values.put(path, child);
    return child;
  }

  MemoryConfigSection createSection(String path, boolean force, Map<String, ?> initials)
      throws TcConfigException {
    if (path.isEmpty()) {
      return null;
    }

    var pathSepInd = getPathSepIndex(path);
    if (pathSepInd < 0) {
      var val = values.get(path);
      if (force && val != null) {
        throw new TcConfigException(SectionPaths.createPath(section, path) + " is exited");
      }

      var child = createShallowSection(path);
      if (initials != null) {
        initials.forEach((k, v) -> child.storage.set(k, false, v));
      }
      return child;
    }

    var firstNode = getFirstPathNode(path, pathSepInd);
    var val = values.get(firstNode);
    if (force && val != null) {
      throw new TcConfigException(SectionPaths.createPath(section, path) + " is exited");
    }
    var child = createShallowSection(path);
    return child.storage.createSection(trimFirstPathNode(path, pathSepInd), force, initials);
  }

  void clearCache() {
    keySet = null;
    cachedValues = null;
  }

  Set<String> getKeys(boolean deep) {
    if (!deep) {
      return Collections.unmodifiableSet(values.keySet());
    }
    Set<String> ks = keySet;
    if (ks == null) {
      Set<String> keys = new LinkedHashSet<>();
      keySet(keys);
      ks = Collections.unmodifiableSet(keys);
      keySet = ks;
    }
    return ks;
  }

  private void keySet(Set<String> keys) {
    values.forEach((k, v) -> {
      if (v instanceof MemoryConfigSection) {
        ((MemoryConfigSection) v).storage.keySet(keys);
      } else {
        keys.add(SectionPaths.createPath(section, k));
      }
    });
  }

  Map<String, ?> getValues() {
    Map<String, Object> root = new LinkedHashMap<>();
    mapValues(root);
    return root;
  }

  void mapValues(Map<String, Object> mapped) {
    values.forEach((k, v) -> {
      if (v instanceof MemoryConfigSection) {
        Map<String, Object> child = new LinkedHashMap<>();
        mapped.put(k, child);
        ((MemoryConfigSection) v).storage.mapValues(child);
      } else {
        mapped.put(k, v);
      }
    });
  }

  Map<String, ?> cachedValues() {
    if (cachedValues == null) {
      cachedValues = getValues();
      return cachedValues;
    }
    return cachedValues;
  }
}
