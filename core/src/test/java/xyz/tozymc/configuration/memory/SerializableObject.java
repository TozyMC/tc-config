package xyz.tozymc.configuration.memory;

import java.util.Objects;
import xyz.tozymc.configuration.serialization.annotation.AutoSerialization;
import xyz.tozymc.configuration.serialization.annotation.SerializeAs;

@AutoSerialization
public class SerializableObject {
  @SerializeAs("x") int x;
  @SerializeAs("y") int y;
  @SerializeAs("z") int z;

  SerializableObject(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SerializableObject)) {
      return false;
    }
    SerializableObject that = (SerializableObject) o;
    return x == that.x && y == that.y && z == that.z;
  }
}
