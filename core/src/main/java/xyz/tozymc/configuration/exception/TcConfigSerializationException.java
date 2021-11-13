package xyz.tozymc.configuration.exception;

/**
 * Exception thrown when an error occurs when serializing.
 *
 * @author TozyMC
 * @since 1.0
 */
public class TcConfigSerializationException extends RuntimeException {
  /**
   * Creates new {@link TcConfigSerializationException} with detail message.
   *
   * @param message The detail message.
   */
  public TcConfigSerializationException(String message) {
    super(message);
  }

  /**
   * Creates new {@link TcConfigSerializationException} with detail message and cause.
   *
   * @param message The detail message.
   * @param cause   The cause.
   */
  public TcConfigSerializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
