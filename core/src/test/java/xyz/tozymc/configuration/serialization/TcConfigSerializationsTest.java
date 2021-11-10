package xyz.tozymc.configuration.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static xyz.tozymc.configuration.serialization.TcConfigSerializations.deserializeObject;
import static xyz.tozymc.configuration.serialization.TcConfigSerializations.serializeObject;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TcConfigSerializationsTest {
  static {
    TcConfigSerializations.registerSerializer(new ObjectSerializer());
  }

  @Test
  void serialize_withSerializer() {
    var serializableObject = new SerializableObject(102, 64, -110);
    var map = Map.of("x", 102, "y", 64, "z", -110);
    assertEquals(map, serializeObject(serializableObject));
    assertEquals(serializableObject, deserializeObject(SerializableObject.class, map));
  }

  @Test
  void serialize_withAnnotationAndEmptyConstructor() {
    var eAnnotatedSerializableObject = new EAnnotatedSerializableObject();
    eAnnotatedSerializableObject.name = "TcConfig";
    eAnnotatedSerializableObject.description = "Configuration library for Java programming";
    var map = Map.of("Name", "TcConfig", "Description",
        "Configuration library for Java programming");
    assertEquals(map, serializeObject(eAnnotatedSerializableObject));
    assertEquals(eAnnotatedSerializableObject,
        deserializeObject(EAnnotatedSerializableObject.class, map));
  }

  @Test
  void serialize_withAnnotationAndParamsConstructor() {
    var pAnnotatedSerializableObject = new PAnnotatedSerializableObject("tc-config.admin", true);
    var map = new LinkedHashMap<String, Object>();
    map.put("Permission", "tc-config.admin");
    map.put("Global", true);

    assertEquals(map, serializeObject(pAnnotatedSerializableObject));
    assertEquals(pAnnotatedSerializableObject,
        deserializeObject(PAnnotatedSerializableObject.class, map));
  }
}