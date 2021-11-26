package xyz.tozymc.configuration.yaml.builder;

import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import xyz.tozymc.configuration.file.builder.AbstractFileConfigBuilder;
import xyz.tozymc.configuration.yaml.YamlConfig;
import xyz.tozymc.configuration.yaml.option.YamlConfigOptions;

/**
 * Builder class to create a new {@link YamlConfig}.
 *
 * @author TozyMC
 * @see YamlConfig
 * @see YamlConfigOptions
 * @since 1.0
 */
public class YamlConfigBuilder extends AbstractFileConfigBuilder<YamlConfig> {
  private int indent = 2;
  private boolean prettyFlow = false;
  private FlowStyle flowStyle = FlowStyle.BLOCK;

  /**
   * Constructs new file configuration builder with config {@link Path}.
   *
   * @param configPath The configuration file.
   */
  public YamlConfigBuilder(Path configPath) {
    super(configPath);
  }

  /**
   * Gets the indent of between parent and child element.
   *
   * @return The indent of between parent and child element.
   */
  public int indent() {
    return indent;
  }

  /**
   * Sets the indent of between parent and child element.
   *
   * @param indent indent of between parent and child element.
   * @return This builder, for chaining.
   */
  public @NotNull YamlConfigBuilder indent(int indent) {
    this.indent = indent;
    return this;
  }

  /**
   * Checks if YAML document using pretty flow style.
   *
   * @return True if YAML document using pretty flow style.
   */
  public boolean prettyFlow() {
    return prettyFlow;
  }

  /**
   * Force the emitter to produce a pretty YAML document when using the flow style.
   *
   * @param prettyFlow True produce pretty flow YAML document.
   * @return This builder, for chaining.
   */
  public @NotNull YamlConfigBuilder prettyFlow(boolean prettyFlow) {
    this.prettyFlow = prettyFlow;
    return this;
  }

  /**
   * Gets flow style for YAML document, default is {@link FlowStyle#BLOCK}.
   *
   * @return Flow style for YAML document.
   */
  public FlowStyle flowStyle() {
    return flowStyle;
  }

  /**
   * Sets flow style for YAML document, default is {@link FlowStyle#BLOCK}.
   *
   * @param flowStyle Flow style for YAML document.
   * @return This build, for chaining.
   */
  public @NotNull YamlConfigBuilder flowStyle(FlowStyle flowStyle) {
    this.flowStyle = flowStyle;
    return this;
  }

  @Override
  protected YamlConfig buildConfig() {
    return new YamlConfig(configPath()).getOptions()
        .indent(indent)
        .prettyFlow(prettyFlow)
        .flowStyle(flowStyle)
        .config();
  }
}
