package xyz.tozymc.configuration.memory;

import xyz.tozymc.configuration.TcConfig;
import xyz.tozymc.configuration.TcConfigSection;
import xyz.tozymc.configuration.option.TcConfigOptions;

@SuppressWarnings("NullableProblems")
class SimpleConfig extends SimpleConfigSection implements TcConfig {
  private final MemoryOptions options;

  SimpleConfig() {
    super();
    this.options = new MemoryOptions(this);
  }

  @Override
  protected MemoryConfigSection newSection(TcConfigSection parent, String name) {
    return new SimpleConfigSection(parent, name);
  }

  @Override
  public TcConfigOptions getOptions() {
    return options;
  }

  static class MemoryOptions extends TcConfigOptions {
    protected MemoryOptions(TcConfig config) {
      super(config);
    }
  }
}
