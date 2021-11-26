package xyz.tozymc.configuration.memory;

import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import xyz.tozymc.configuration.TcConfig;
import xyz.tozymc.configuration.TcConfigSection;
import xyz.tozymc.configuration.exception.TcConfigException;
import xyz.tozymc.configuration.serialization.TcConfigSerializations;
import xyz.tozymc.configuration.util.SectionPaths;
import xyz.tozymc.configuration.util.Validators;

/**
 * The type of {@link TcConfigSection} that is stored in memory.
 *
 * @author TozyMC
 * @since 1.0
 */
public abstract class MemoryConfigSection implements TcConfigSection {
  final MemoryStorage storage = new MemoryStorage(this);
  private final TcConfig root;
  private final TcConfigSection parent;
  private final String name;
  private final String fullPath;

  /**
   * Constructs new root {@link TcConfigSection}.
   */
  protected MemoryConfigSection() {
    if (!(this instanceof TcConfig)) {
      throw new IllegalStateException("Cannot construct a root section when not a TcConfig");
    }
    this.root = (TcConfig) this;
    this.parent = null;
    this.name = "";
    this.fullPath = "";
  }

  /**
   * Constructs new child {@link TcConfigSection}.
   *
   * @param parent Parent containing the child section.
   * @param name   Name of section.
   */
  protected MemoryConfigSection(@NotNull TcConfigSection parent, @NotNull String name) {
    this.root = parent.getRoot();
    this.parent = parent;
    this.name = name;
    this.fullPath = SectionPaths.createPath(parent, name);
  }

  /**
   * @hidden
   */
  protected abstract MemoryConfigSection newSection(TcConfigSection parent, String name);

  protected void reloadSection(Map<String, ?> map) {
    storage.reload(map);
  }

  @Override
  public <T> @Nullable T get(@NotNull String path, @NotNull Class<T> type) {
    var val = get(path);
    if (val instanceof MemoryConfigSection) {
      return TcConfigSerializations.deserializeObject(type,
          ((MemoryConfigSection) val).storage.cachedValues());
    }
    return type.isInstance(val) ? type.cast(val) : null;
  }

  @Override
  public <T> @Nullable T getOrDefault(@NotNull String path, @NotNull Class<T> type,
      @Nullable T def) {
    var val = get(path, type);
    return val != null ? val : def;
  }

  @Override
  public @Nullable Object get(@NotNull String path) {
    Validators.notNullArgs(path, "Path cannot be null");
    return getOrDefault(path, null);
  }

  @Override
  public @Nullable Object getOrDefault(@NotNull String path, @Nullable Object def) {
    Validators.notNullArgs(path, "Path cannot be null");

    var val = storage.get(path);
    return val != null ? val : def;
  }

  @Override
  public @Nullable Object set(@NotNull String path, @Nullable Object value) {
    Validators.notNullArgs(path, "Path cannot be null");
    return storage.set(path, false, value);
  }

  @Override
  public @Nullable Object setIfAbsent(@NotNull String path, @Nullable Object value) {
    Validators.notNullArgs(path, "Path cannot be null");
    return storage.set(path, true, value);
  }

  @Override
  public @UnmodifiableView @NotNull Set<String> getKeys(boolean deep) {
    return storage.getKeys(deep);
  }

  @Override
  public @NotNull Map<String, ?> getValues() {
    return storage.getValues();
  }

  @Override
  public boolean hasChild(@NotNull String path) {
    Validators.notNullArgs(path, "Path cannot be null");
    return getChild(path) != null;
  }

  @Override
  public @NotNull TcConfigSection createChild(@NotNull String path) throws TcConfigException {
    Validators.notNullArgs(path, "Path cannot be null");
    return storage.createSection(path, false, null);
  }

  @Override
  public @NotNull TcConfigSection createChild(@NotNull String path, Map<String, ?> values)
      throws TcConfigException {
    Validators.notNullArgs(path, "Path cannot be null");
    return storage.createSection(path, false, values);
  }

  @Override
  public @Nullable TcConfigSection getChild(@NotNull String path) {
    Validators.notNullArgs(path, "Path cannot be null");
    var val = get(path);
    return val instanceof TcConfigSection ? (TcConfigSection) val : null;
  }

  @Override
  public @Nullable TcConfigSection getParent() {
    return parent;
  }

  @Override
  public @NotNull String getName() {
    return name;
  }

  @Override
  public @NotNull String getFullPath() {
    return fullPath;
  }

  @Override
  public @NotNull TcConfig getRoot() {
    return root;
  }
}
