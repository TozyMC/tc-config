package xyz.tozymc.configuration.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.tozymc.configuration.TcConfigSection;

public final class SectionPaths {
  private SectionPaths() {}

  public static @NotNull String createPath(@Nullable TcConfigSection parent, @NotNull String name) {
    if (parent == null) {
      return name;
    }
    var parentPath = parent.getFullPath();
    return parentPath.isBlank() ? name : String.format("%s.%s", parentPath, name);
  }
}
