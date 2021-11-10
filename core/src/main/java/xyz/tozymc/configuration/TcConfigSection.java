package xyz.tozymc.configuration;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.tozymc.configuration.exception.TcConfigException;

/**
 * Represents configuration section.
 *
 * @author TozyMC
 * @since 1.0
 */
public interface TcConfigSection extends DataStorage {
  /**
   * Checks if this section contains the given path and the given path is configuration section.
   *
   * @param path Path to child.
   * @return True if this section contains this path and this path is configuration section.
   */
  boolean hasChild(@NotNull String path);

  /**
   * Creates and returns the child section with specified path.
   *
   * @param path The path where the child section will be created.
   * @return The created child section.
   * @throws TcConfigException Thrown when path is existed.
   */
  @NotNull TcConfigSection createChild(@NotNull String path) throws TcConfigException;

  /**
   * Creates the child section by specified path with given values and returns it.
   *
   * @param path   The path where the child section will be created.
   * @param values The values to set.
   * @return The created child section.
   * @throws TcConfigException Thrown when path is existed.
   */
  @NotNull TcConfigSection createChild(@NotNull String path, Map<String, ?> values)
      throws TcConfigException;

  /**
   * Gets the child section with specified path.
   *
   * <p>Returns {@code null} when child section not found.
   *
   * @param path Path to child.
   * @return The specified child section.
   */
  @Nullable TcConfigSection getChild(@NotNull String path);

  /**
   * Gets the section that contains this section.
   *
   * <p>Returns {@code null} when this section is root.
   *
   * @return The section that contains this section.
   */
  @Nullable TcConfigSection getParent();

  /**
   * Gets the name of this section.
   *
   * @return This section name.
   */
  @NotNull String getName();

  /**
   * Gets the path of this section.
   *
   * <p>The path can be used in the root config to get this section.
   *
   * @return This section path.
   */
  @NotNull String getFullPath();

  /**
   * Gets the root config that contains this section.
   *
   * @return The root config.
   */
  @NotNull TcConfig getRoot();
}
