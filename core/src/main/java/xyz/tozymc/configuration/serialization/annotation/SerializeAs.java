package xyz.tozymc.configuration.serialization.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

/**
 * A field annotated with SerializeAs will be serialized and stored under the specified name.
 *
 * @see AutoSerialization
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SerializeAs {
  /**
   * @return The name by which the field will be serialized and stored under that name.
   */
  @NotNull String value();
}
