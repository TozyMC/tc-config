module tcconfig.core {
  requires static org.jetbrains.annotations;

  exports xyz.tozymc.configuration;
  exports xyz.tozymc.configuration.builder;
  exports xyz.tozymc.configuration.exception;
  exports xyz.tozymc.configuration.file;
  exports xyz.tozymc.configuration.file.option;
  exports xyz.tozymc.configuration.file.builder;
  exports xyz.tozymc.configuration.option;
  exports xyz.tozymc.configuration.serialization;
  exports xyz.tozymc.configuration.serialization.annotation;
}