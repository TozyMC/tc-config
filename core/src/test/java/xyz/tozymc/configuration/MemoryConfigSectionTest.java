package xyz.tozymc.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class MemoryConfigSectionTest {
  private static final MemoryConfigSection root = new MemoryConfigSection(null);

  @AfterAll
  static void afterAll() {
    assertEquals("Test section", root.getString("title"));
    assertInstanceOf(TcConfigSection.class, root.getChild("owner"));
    assertInstanceOf(TcConfigSection.class, root.getChild("database"));
  }

  @Test
  @Order(1)
  void set() {
    root.set("title", "Test section");
  }

  @Test
  @Order(2)
  void createSection_withMap() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("name", "TozyMC");
    map.put("job", "Java Developer");
    root.createChild("owner", map);
  }

  @Test
  @Order(3)
  void createSection_withSet() {
    var child = root.createChild("database");
    child.set("enable", true);
    child.set("server", "127.0.0.1");
    child.set("port", 3306);
    child.set("pool", 20);
  }

  @Test
  @Order(4)
  void getKeys_deep() {
    var expected = Set.of("title", "owner.name", "owner.job", "database.enable", "database.server",
        "database.port", "database.pool");
    var actual = root.getKeys(true);
    assertEquals(expected, actual);
  }

  @Test
  @Order(5)
  void getKeys_shallow() {
    var expected = Set.of("title", "owner", "database");
    var actual = root.getKeys(false);
    assertEquals(expected, actual);
  }

  @Test
  @Order(6)
  void getValues() {
    Map<String, Object> expected = new LinkedHashMap<>();
    expected.put("title", "Test section");
    Map<String, Object> owner = new LinkedHashMap<>();
    owner.put("name", "TozyMC");
    owner.put("job", "Java Developer");
    expected.put("owner", owner);
    Map<String, Object> database = new LinkedHashMap<>();
    database.put("enable", true);
    database.put("server", "127.0.0.1");
    database.put("port", 3306);
    database.put("pool", 20);
    expected.put("database", database);

    assertEquals(expected, root.getValues());
  }
}