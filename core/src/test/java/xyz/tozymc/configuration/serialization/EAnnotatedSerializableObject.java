package xyz.tozymc.configuration.serialization;

import java.util.Objects;
import xyz.tozymc.configuration.serialization.annotation.AutoSerialization;
import xyz.tozymc.configuration.serialization.annotation.SerializeAs;

@AutoSerialization
class EAnnotatedSerializableObject {
  @SerializeAs("Name") String name;
  @SerializeAs("Description") String description;

  @Override
  public int hashCode() {
    return Objects.hash(name, description);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EAnnotatedSerializableObject)) {
      return false;
    }
    EAnnotatedSerializableObject that = (EAnnotatedSerializableObject) o;
    return Objects.equals(name, that.name) && Objects.equals(description, that.description);
  }
}
