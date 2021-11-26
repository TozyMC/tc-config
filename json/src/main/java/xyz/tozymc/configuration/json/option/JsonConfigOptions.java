package xyz.tozymc.configuration.json.option;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import xyz.tozymc.configuration.file.FileConfig;
import xyz.tozymc.configuration.file.option.FileConfigOptions;
import xyz.tozymc.configuration.json.JsonConfig;

/**
 * Various options for {@link JsonConfig}.
 *
 * @author TozyMC
 * @since 1.0
 */
public class JsonConfigOptions extends FileConfigOptions {
  private Gson gson;
  private boolean prettyPrint = true;

  /**
   * Creates new {@link JsonConfigOptions} for {@link JsonConfig}.
   *
   * @param config Configuration that owned this options.
   */
  public JsonConfigOptions(@NotNull FileConfig config) {
    super(config);
  }

  /**
   * Checks the pretty print is on.
   *
   * @return True is pretty print is on.
   */
  public boolean prettyPrint() {
    return prettyPrint;
  }

  /**
   * Sets the pretty print options.
   *
   * @param prettyPrint New value.
   * @return This object, for chaining.
   */
  public @NotNull JsonConfigOptions prettyPrint(boolean prettyPrint) {
    if (this.prettyPrint != prettyPrint) {
      gson = null;
    }
    this.prettyPrint = prettyPrint;
    return this;
  }

  /**
   * Gets the object for json reading and writing.
   *
   * @return The object for json reading and writing.
   */
  public @NotNull Gson gson() {
    if (gson != null) {
      return gson;
    }
    var builder = new GsonBuilder();
    if (prettyPrint) {
      builder.setPrettyPrinting();
    }
    return gson = builder.create();
  }

  @Override
  public @NotNull JsonConfig config() {
    return (JsonConfig) super.config();
  }
}
