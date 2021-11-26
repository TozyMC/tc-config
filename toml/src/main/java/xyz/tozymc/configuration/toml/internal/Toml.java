package xyz.tozymc.configuration.toml.internal;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Map;

public final class Toml {
  static final DateTimeFormatter DATE_FORMATTER;

  static {
    DATE_FORMATTER = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_LOCAL_DATE)
        .optionalStart()
        .appendLiteral('T')
        .append(DateTimeFormatter.ISO_LOCAL_TIME)
        .optionalStart()
        .appendOffsetId()
        .optionalEnd()
        .optionalEnd()
        .toFormatter();
  }

  private Toml() {
  }

  public static String toToml(Map<String, ?> data, int indentSize) {
    var stringWriter = new StringWriter();
    try (var tomlWriter = new TomlWriter(stringWriter, indentSize)) {
      tomlWriter.write(data);
    } catch (IOException e) {
      throw new UncheckedIOException("Error when writing toml string", e);
    }
    return stringWriter.toString();
  }

  public static Map<String, Object> fromToml(Path path) throws IOException {
    var data = Files.readString(path, StandardCharsets.UTF_8);
    return new TomlReader(data, false).read();
  }
}
