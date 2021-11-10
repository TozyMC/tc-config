package xyz.tozymc.configuration.memory;

import xyz.tozymc.configuration.TcConfigSection;

public class SimpleConfigSection extends MemoryConfigSection {
  public SimpleConfigSection() {super();}

  public SimpleConfigSection(TcConfigSection parent, String name) {
    super(parent, name);
  }

  @Override
  protected MemoryConfigSection newSection(TcConfigSection parent, String name) {
    return new SimpleConfigSection(parent, name);
  }
}
