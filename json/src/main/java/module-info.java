module tcconfig.json {
  requires static org.jetbrains.annotations;

  requires tcconfig.core;
  requires com.google.gson;

  exports xyz.tozymc.configuration.json;
  exports xyz.tozymc.configuration.json.builder;
  exports xyz.tozymc.configuration.json.option;
}