package xyz.tozymc.configuration.file.option;

import xyz.tozymc.configuration.file.FileConfig;

/**
 * The list of reloadable types used for {@link FileConfig}.
 *
 * @author TozyMC
 * @since 1.0
 */
public enum ReloadableType {
  /**
   * The configuration will automatically reload the config file when getting or setting config
   * value.
   *
   * <p><b>Notes: </b>This can slow performance down. Better alternative: {@link #INTELLIGENT}.
   */
  AUTOMATICALLY,
  /**
   * The configuration may reload when the config file is modified or the setter value is not equal
   * to the old value.
   */
  INTELLIGENT,
  /**
   * {@link FileConfig} will be reloaded manually by executing reload method.
   */
  MANUALLY
}
