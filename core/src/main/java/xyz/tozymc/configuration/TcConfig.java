package xyz.tozymc.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.tozymc.configuration.option.TcConfigOptions;

/**
 * Represents a root configuration section.
 *
 * @author TozyMC
 * @since 1.0
 */
public interface TcConfig extends TcConfigSection {
  /**
   * Gets the options of this configuration.
   *
   * @return Options of this configuration.
   */
  @NotNull TcConfigOptions getOptions();

  @Override
  default @Nullable TcConfigSection getParent() {return null;}
}
