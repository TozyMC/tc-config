package xyz.tozymc.configuration.toml.internal;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

final class TomlWriter implements Closeable, Flushable {
  private static final char INDENT_CHAR = 0x20;

  private final Writer writer;
  private final int indentSize;
  private final LinkedList<String> tablesNames = new LinkedList<>();
  private int lineBreaks = 0;
  private int indentationLevel = -1;// -1 to prevent indenting the first level

  TomlWriter(Writer writer, int indentSize) {
    this.writer = writer;
    this.indentSize = indentSize;
  }

  static void addEscaped(char c, StringBuilder sb) {
    switch (c) {
      case '\b':
        sb.append("\\b");
        break;
      case '\t':
        sb.append("\\t");
        break;
      case '\n':
        sb.append("\\n");
        break;
      case '\\':
        sb.append("\\\\");
        break;
      case '\r':
        sb.append("\\r");
        break;
      case '\f':
        sb.append("\\f");
        break;
      case '"':
        sb.append("\\\"");
        break;
      default:
        sb.append(c);
        break;
    }
  }

  public void close() throws IOException {
    writer.close();
  }

  public void flush() throws IOException {
    writer.flush();
  }

  void write(Map<String, ?> data) throws IOException {
    writeTableContent(data);
  }

  private void writeTableName() throws IOException {
    Iterator<String> it = tablesNames.iterator();
    while (it.hasNext()) {
      String namePart = it.next();
      writeKey(namePart);
      if (it.hasNext()) {
        write('.');
      }
    }
  }

  private void writeTableContent(Map<String, ?> table) throws IOException {
    writeTableContent(table, true);
    writeTableContent(table, false);
  }

  @SuppressWarnings("unchecked")
  private void writeTableContent(Map<String, ?> table, boolean simpleValues)
      throws IOException {
    for (Map.Entry<String, ?> entry : table.entrySet()) {
      String name = entry.getKey();
      Object value = entry.getValue();
      if (value instanceof Collection) {// array
        Collection<?> c = (Collection<?>) value;
        if (!c.isEmpty() && c.iterator().next() instanceof Map) {// array of tables
          if (simpleValues) {
            continue;
          }
          tablesNames.addLast(name);
          indentationLevel++;
          for (Object element : c) {
            indent();
            write("[[");
            writeTableName();
            write("]]\n");
            Map<String, Object> map = (Map<String, Object>) element;
            writeTableContent(map);
          }
          indentationLevel--;
          tablesNames.removeLast();
        } else {// normal array
          if (!simpleValues) {
            continue;
          }
          indent();
          writeKey(name);
          write(" = ");
          writeArray(c);
        }
      } else if (value instanceof Object[]) {// array
        Object[] array = (Object[]) value;
        if (array.length > 0 && array[0] instanceof Map) {// array of tables
          if (simpleValues) {
            continue;
          }
          tablesNames.addLast(name);
          indentationLevel++;
          for (Object element : array) {
            indent();
            write("[[");
            writeTableName();
            write("]]\n");
            Map<String, Object> map = (Map<String, Object>) element;
            writeTableContent(map);
          }
          indentationLevel--;
          tablesNames.removeLast();
        } else {// normal array
          if (!simpleValues) {
            continue;
          }
          indent();
          writeKey(name);
          write(" = ");
          writeArray(array);
        }
      } else if (value instanceof Map) {// table
        if (simpleValues) {
          continue;
        }
        tablesNames.addLast(name);
        indentationLevel++;

        indent();
        write('[');
        writeTableName();
        write(']');
        newLine();
        writeTableContent((Map<String, Object>) value);

        indentationLevel--;
        tablesNames.removeLast();
      } else {// simple value
        if (!simpleValues) {
          continue;
        }
        indent();
        writeKey(name);
        write(" = ");
        writeValue(value);
      }
      newLine();
    }
    newLine();
  }

  private void writeKey(String key) throws IOException {
    for (int i = 0; i < key.length(); i++) {
      char c = key.charAt(i);
      if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '-'
          || c == '_')) {
        writeString(key);
        return;
      }
    }
    write(key);
  }

  private void writeString(String str) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append('"');
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      addEscaped(c, sb);
    }
    sb.append('"');
    write(sb.toString());
  }

  private void writeArray(Collection<?> c) throws IOException {
    write('[');
    for (Object element : c) {
      writeValue(element);
      write(", ");
    }
    write(']');
  }

  private void writeArray(Object[] array) throws IOException {
    write('[');
    for (Object element : array) {
      writeValue(element);
      write(", ");
    }
    write(']');
  }

  private void writeArray(byte[] array) throws IOException {
    write('[');
    for (byte element : array) {
      write(String.valueOf(element));
      write(", ");
    }
    write(']');
  }

  private void writeArray(short[] array) throws IOException {
    write('[');
    for (short element : array) {
      write(String.valueOf(element));
      write(", ");
    }
    write(']');
  }

  private void writeArray(char[] array) throws IOException {
    write('[');
    for (char element : array) {
      write(String.valueOf(element));
      write(", ");
    }
    write(']');
  }

  private void writeArray(int[] array) throws IOException {
    write('[');
    for (int element : array) {
      write(String.valueOf(element));
      write(", ");
    }
    write(']');
  }

  private void writeArray(long[] array) throws IOException {
    write('[');
    for (long element : array) {
      write(String.valueOf(element));
      write(", ");
    }
    write(']');
  }

  private void writeArray(float[] array) throws IOException {
    write('[');
    for (float element : array) {
      write(String.valueOf(element));
      write(", ");
    }
    write(']');
  }

  private void writeArray(double[] array) throws IOException {
    write('[');
    for (double element : array) {
      write(String.valueOf(element));
      write(", ");
    }
    write(']');
  }

  private void writeValue(Object value) throws IOException {
    if (value instanceof String) {
      writeString((String) value);
    } else if (value instanceof Number || value instanceof Boolean) {
      write(value.toString());
    } else if (value instanceof TemporalAccessor) {
      String formatted = Toml.DATE_FORMATTER.format((TemporalAccessor) value);
      if (formatted.endsWith("T"))// If the last character is a 'T'
      {
        formatted = formatted.substring(0,
            formatted.length() - 1);// removes it because it's invalid.
      }
      write(formatted);
    } else if (value instanceof Collection) {
      writeArray((Collection<?>) value);
    } else if (value instanceof int[]) {
      writeArray((int[]) value);
    } else if (value instanceof byte[]) {
      writeArray((byte[]) value);
    } else if (value instanceof short[]) {
      writeArray((short[]) value);
    } else if (value instanceof char[]) {
      writeArray((char[]) value);
    } else if (value instanceof long[]) {
      writeArray((long[]) value);
    } else if (value instanceof float[]) {
      writeArray((float[]) value);
    } else if (value instanceof double[]) {
      writeArray((double[]) value);
    } else if (value instanceof Map) {// should not happen because an array of tables is detected by
      // writeTableContent()
      throw new IOException("Unexpected value " + value);
    } else {
      throw new TomlException("Unsupported value of type " + value.getClass().getCanonicalName());
    }
  }

  private void newLine() throws IOException {
    if (lineBreaks > 1) {
      return;
    }
    writer.write(System.lineSeparator());
    lineBreaks++;
  }

  private void write(char c) throws IOException {
    writer.write(c);
    lineBreaks = 0;
  }

  private void write(String str) throws IOException {
    writer.write(str);
    lineBreaks = 0;
  }

  private void indent() throws IOException {
    for (int i = 0; i < indentationLevel; i++) {
      for (int j = 0; j < indentSize; j++) {
        write(INDENT_CHAR);
      }
    }
  }
}
