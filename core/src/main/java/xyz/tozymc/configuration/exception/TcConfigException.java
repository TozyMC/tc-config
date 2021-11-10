package xyz.tozymc.configuration.exception;

/**
 * Exception thrown when operation in configuration.
 *
 * @author TozyMC
 * @since 1.0
 */
public class TcConfigException extends RuntimeException {
  /**
   * Creates new {@link TcConfigException} with detail message.
   *
   * @param message The detail message.
   */
  public TcConfigException(String message) {
    super(message);
  }

  /**
   * Creates new {@link TcConfigSerializationException} with detail message and cause.
   *
   * @param message The detail message.
   * @param cause   The cause.
   */
  public TcConfigException(String message, Throwable cause) {
    super(message, cause);
  }
}
