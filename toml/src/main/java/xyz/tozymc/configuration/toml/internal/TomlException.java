package xyz.tozymc.configuration.toml.internal;

class TomlException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  TomlException(String message, Throwable cause) {
    super(message, cause);
  }

  TomlException(String message) {
    super(message);
  }
}
