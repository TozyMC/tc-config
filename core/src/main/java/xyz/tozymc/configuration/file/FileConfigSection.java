package xyz.tozymc.configuration.file;

import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.UnmodifiableView;
import xyz.tozymc.configuration.TcConfigSection;
import xyz.tozymc.configuration.exception.TcConfigException;
import xyz.tozymc.configuration.memory.MemoryConfigSection;

/**
 * Type of {@link TcConfigSection} that is store and handle with file.
 *
 * @author TozyMC
 * @since 1.0
 */
public class FileConfigSection extends MemoryConfigSection {
  /**
   * @hidden
   */
  @TestOnly
  protected FileConfigSection(Object unused) {super(unused);}

  protected FileConfigSection() {super();}

  protected FileConfigSection(@NotNull TcConfigSection parent, @NotNull String name) {
    super(parent, name);
  }

  @Override
  protected FileConfigSection newSection(TcConfigSection parent, String name) {
    return new FileConfigSection(parent, name);
  }

  @Override
  public @Nullable Object getOrDefault(@NotNull String path, @Nullable Object def) {
    getRoot().reloadIfNeeded();
    return super.getOrDefault(path, def);
  }

  @Override
  public @Nullable Object set(@NotNull String path, @Nullable Object value) {
    var old = super.set(path, value);
    getRoot().save();
    return old;
  }

  @Override
  public @Nullable Object setIfAbsent(@NotNull String path, @Nullable Object value) {
    var old = super.setIfAbsent(path, value);
    getRoot().save();
    return old;
  }

  @Override
  public @UnmodifiableView @NotNull Set<String> getKeys(boolean deep) {
    getRoot().reloadIfNeeded();
    return super.getKeys(deep);
  }

  @Override
  public @NotNull Map<String, ?> getValues() {
    getRoot().reloadIfNeeded();
    return super.getValues();
  }

  @Override
  public @NotNull TcConfigSection createChild(@NotNull String path) throws TcConfigException {
    var child = super.createChild(path);
    getRoot().save();
    return child;
  }

  @Override
  public @NotNull TcConfigSection createChild(@NotNull String path, Map<String, ?> values)
      throws TcConfigException {
    var child = super.createChild(path, values);
    getRoot().save();
    return child;
  }

  @Override
  public @NotNull FileConfig getRoot() {
    return (FileConfig) super.getRoot();
  }
}
