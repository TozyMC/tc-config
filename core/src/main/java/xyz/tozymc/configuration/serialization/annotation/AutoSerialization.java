package xyz.tozymc.configuration.serialization.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a class that is auto-serializable.
 *
 * <p>The annotated class will use {@link SerializeAs} to serialize the fields, not the
 * {@code serialize(T)} method.
 *
 * <p><b>Notes: </b>The annotated class type must have an empty constructor or a constructor with
 * parameters in the same order as the fields annotated by {@link SerializeAs} in the class type.
 *
 * <p>E.g: {@code Location} containing three fields x, y, z. Each field is annotated with {@link
 * SerializeAs} and the value corresponds to the name of the field. The constructor must have 3
 * parameters in the order of x, y, z (the same order of fields).
 *
 * @see SerializeAs
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoSerialization {}
