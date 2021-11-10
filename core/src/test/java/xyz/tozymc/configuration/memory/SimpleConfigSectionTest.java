package xyz.tozymc.configuration.memory;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class SimpleConfigSectionTest {
  private static final SimpleConfig config = new SimpleConfig();

  @Test
  @Order(0)
  void set_scalarObject() {
    assertAll(() -> assertNull(config.set("string", "This is string")),
        () -> assertNull(config.set("int", 1000)),
        () -> assertNull(config.set("long", 0x7fffffffffffffffL)),
        () -> assertNull(config.set("boolean", false)));
  }

  @Test
  @Order(1)
  void set_structureObject() {
    assertAll(() -> assertNull(config.set("structure.array", new int[]{1, 2, 3, 45})),
        () -> assertNull(config.set("structure.map.map", Map.of("k1", "v1", "k2", "v3"))),
        () -> assertNull(config.set("structure.map.arrayMap", new Map[]{
            Map.of("k1", "v1", "k2", "v3"), Map.of("k1", "v1", "k2", "v3"),
            Map.of("k1", "v1", "k2", "v3")
        })));
  }

  @Test
  @Order(2)
  void set_serializableObject() {
    assertAll(() -> assertNull(config.set("serializable", new SerializableObject(1, 2, 3))),
        () -> assertNull(config.set("arraySerializable", new SerializableObject[]{
            new SerializableObject(100, 0, -101), new SerializableObject(43, -1, 30),
            new SerializableObject(-101, 20, 44)
        })));
  }

  @Test
  @Order(3)
  void setIfAbsent() {
    assertAll(() -> assertNull(config.setIfAbsent("absent", true)),
        () -> assertNull(config.setIfAbsent("absent", false)));
  }

  @Test
  @Order(4)
  void createChild() {
    assertAll(() -> assertNotNull(config.createChild("emptyChild")),
        () -> assertNotNull(config.createChild("child", Map.of("k1", "v1"))));
  }

  @Test
  @Order(5)
  void get_scalarObject() {
    assertAll(() -> assertNotNull(config.get("string")),
        () -> assertEquals(1000, config.getInt("int")),
        () -> assertEquals(0x7fffffffffffffffL, config.getLong("long")),
        () -> assertFalse(config.getBoolean("boolean")));
  }

  @Test
  @Order(6)
  void get_structureObject() {
    assertAll(() -> assertInstanceOf(List.class, config.get("structure.array")),
        () -> assertIterableEquals(List.of(1, 2, 3, 45), config.getIntegerList("structure.array")),
        () -> assertInstanceOf(SimpleConfigSection.class, config.get("structure.map.map")),
        () -> assertEquals(3, config.getMapList("structure.map.arrayMap").size()));
  }

  @Test
  @Order(7)
  void get_serializableObject() {
    assertAll(() -> assertInstanceOf(SerializableObject.class,
            config.get("serializable", SerializableObject.class)),
        () -> assertEquals(new SerializableObject(1, 2, 3),
            config.get("serializable", SerializableObject.class)),
        () -> assertEquals(3, config.getList("arraySerializable").size()),
        () -> assertIterableEquals(
            List.of(new SerializableObject(100, 0, -101), new SerializableObject(43, -1, 30),
                new SerializableObject(-101, 20, 44)),
            config.getList("arraySerializable", SerializableObject.class)));
  }

  @Test
  @Order(8)
  void getChild() {
    assertAll(() -> assertNotNull(config.getChild("emptyChild")), () -> config.getChild("child"));
  }

  @Test
  @Order(9)
  void getKeys() {
    assertAll(() -> assertEquals(10, config.getKeys().size()),
        () -> assertEquals(Set.of("map", "map.k2", "map.k1", "arrayMap"),
            Objects.requireNonNull(config.getChild("structure.map")).getKeys(true)));
  }

  @Test
  @Order(10)
  void getValues() {
    var structureMap = Map.of("map", Map.of("k1", "v1", "k2", "v3"), "arrayMap",
        List.of(Map.of("k1", "v1", "k2", "v3"), Map.of("k1", "v1", "k2", "v3"),
            Map.of("k1", "v1", "k2", "v3")));
    assertAll(() -> assertEquals(Map.of("k1", "v1"),
            Objects.requireNonNull(config.getChild("child")).getValues()),
        () -> assertEquals(structureMap,
            Objects.requireNonNull(config.getChild("structure.map")).getValues()));
  }
}