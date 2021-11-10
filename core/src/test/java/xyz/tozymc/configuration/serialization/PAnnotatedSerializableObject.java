package xyz.tozymc.configuration.serialization;

import java.util.Objects;
import xyz.tozymc.configuration.serialization.annotation.AutoSerialization;
import xyz.tozymc.configuration.serialization.annotation.SerializeAs;

@AutoSerialization
class PAnnotatedSerializableObject {
  @SerializeAs("Permission") final String permission;
  @SerializeAs("Global") final boolean global;

  PAnnotatedSerializableObject(String permission, boolean global) {
    this.permission = permission;
    this.global = global;
  }

  @Override
  public int hashCode() {
    return Objects.hash(permission, global);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PAnnotatedSerializableObject)) {
      return false;
    }
    PAnnotatedSerializableObject that = (PAnnotatedSerializableObject) o;
    return global == that.global && Objects.equals(permission, that.permission);
  }
}
