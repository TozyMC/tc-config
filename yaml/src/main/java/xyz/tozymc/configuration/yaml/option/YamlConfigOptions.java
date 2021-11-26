package xyz.tozymc.configuration.yaml.option;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.LineBreak;
import xyz.tozymc.configuration.file.FileConfig;
import xyz.tozymc.configuration.file.option.FileConfigOptions;
import xyz.tozymc.configuration.yaml.YamlConfig;

/**
 * Various options for {@link YamlConfig}.
 *
 * @author TozyMC
 * @since 1.0
 */
public class YamlConfigOptions extends FileConfigOptions {
  private final DumperOptions dumperOptions = new DumperOptions();

  {
    dumperOptions.setLineBreak(LineBreak.getPlatformLineBreak());
  }

  /**
   * Creates new {@link YamlConfigOptions} for {@link YamlConfig}.
   *
   * @param config Configuration that owned this options.
   */
  public YamlConfigOptions(@NotNull FileConfig config) {
    super(config);
  }

  /**
   * Gets the indent of between parent and child element.
   *
   * @return The indent of between parent and child element.
   */
  public int indent() {
    return dumperOptions.getIndent();
  }

  /**
   * Sets the indent of between parent and child element.
   *
   * @param indent indent of between parent and child element.
   * @return This object, for chaining.
   */
  public @NotNull YamlConfigOptions indent(int indent) {
    this.dumperOptions.setIndent(indent);
    return this;
  }

  /**
   * Checks if YAML document using pretty flow style.
   *
   * @return True if YAML document using pretty flow style.
   */
  public boolean prettyFlow() {
    return dumperOptions.isPrettyFlow();
  }

  /**
   * Force the emitter to produce a pretty YAML document when using the flow style.
   *
   * @param prettyFlow True produce pretty flow YAML document.
   * @return This object, for chaining.
   */
  public @NotNull YamlConfigOptions prettyFlow(boolean prettyFlow) {
    this.dumperOptions.setPrettyFlow(prettyFlow);
    return this;
  }

  /**
   * Gets flow style for YAML document, default is {@link FlowStyle#BLOCK}.
   *
   * @return Flow style for YAML document.
   */
  public FlowStyle flowStyle() {
    return dumperOptions.getDefaultFlowStyle();
  }

  /**
   * Sets flow style for YAML document, default is {@link FlowStyle#BLOCK}.
   *
   * @param flowStyle Flow style for YAML document.
   * @return This object, for chaining.
   */
  public @NotNull YamlConfigOptions flowStyle(FlowStyle flowStyle) {
    this.dumperOptions.setDefaultFlowStyle(flowStyle);
    return this;
  }

  public @NotNull DumperOptions dumperOptions() {
    return dumperOptions;
  }

  @Override
  public @NotNull YamlConfig config() {
    return (YamlConfig) super.config();
  }
}
