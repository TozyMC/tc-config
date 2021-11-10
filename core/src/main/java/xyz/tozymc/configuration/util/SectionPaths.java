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
    return parentPath.isBlank()
        ? name
        : String.format("%s%s%s", parentPath, getPathSeparator(parent), name);
  }

  public static @NotNull String createPath(@NotNull TcConfigSection parent,
      @NotNull TcConfigSection relativeTo, @NotNull String name) {
    var sep = getPathSeparator(parent);
    var builder = new StringBuilder();
    for (var curr = parent; curr != null && curr != relativeTo; curr = curr.getParent()) {
      if (builder.length() > 0) {
        builder.insert(0, sep);
      }
      builder.insert(0, curr.getName());
    }
    if (builder.length() > 0) {
      builder.append(sep);
    }
    return builder.append(name).toString();
  }

  private static char getPathSeparator(TcConfigSection section) {
    return section.getRoot().getOptions().pathSeparator();
  }
}
