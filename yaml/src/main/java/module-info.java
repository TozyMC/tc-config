module tcconfig.yaml {
  requires static org.jetbrains.annotations;

  requires tcconfig.core;
  requires org.yaml.snakeyaml;

  exports xyz.tozymc.configuration.yaml;
  exports xyz.tozymc.configuration.yaml.builder;
  exports xyz.tozymc.configuration.yaml.option;
}