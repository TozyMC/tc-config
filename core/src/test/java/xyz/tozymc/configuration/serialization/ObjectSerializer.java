package xyz.tozymc.configuration.serialization;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("NullableProblems")
class ObjectSerializer implements TcConfigSerializer<SerializableObject> {
  @Override
  public Map<String, ?> serialize(SerializableObject object) {
    var map = new LinkedHashMap<String, Integer>();
    map.put("x", object.x);
    map.put("y", object.y);
    map.put("z", object.z);
    return map;
  }

  @Override
  public SerializableObject deserialize(Map<String, ?> serialized) {
    return new SerializableObject((int) serialized.get("x"), (int) serialized.get("y"),
        (int) serialized.get("z"));
  }
}
