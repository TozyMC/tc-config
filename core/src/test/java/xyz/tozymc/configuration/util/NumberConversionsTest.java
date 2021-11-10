package xyz.tozymc.configuration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static xyz.tozymc.configuration.util.NumberConversions.toDouble;
import static xyz.tozymc.configuration.util.NumberConversions.toInt;
import static xyz.tozymc.configuration.util.NumberConversions.toLong;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class NumberConversionsTest {
  @Test
  void toInt_wrapper() {
    assertEquals(1, toInt(Byte.valueOf("1")));
    assertEquals(2147483647, toInt(Integer.valueOf("2147483647")));
    assertEquals(-2147483648, toInt(Integer.valueOf("-2147483648")));
  }

  @Test
  void toInt_overRange() {
    assertEquals(-1, toInt(Long.valueOf("9223372036854775807")));
    assertEquals(0, toInt(Long.valueOf("-9223372036854775808")));
  }

  @Test
  void toInt_string() {
    assertEquals(2147483647, toInt("2147483647"));
    assertEquals(-2147483648, toInt("-2147483648"));
  }

  @Test
  void toLong_wrapper() {
    assertEquals(100, toLong(Integer.valueOf("100")));
    assertEquals(9223372036854775807L, toLong(Long.valueOf("9223372036854775807")));
    assertEquals(-9223372036854775808L, toLong(Long.valueOf("-9223372036854775808")));
  }

  @Test
  void toLong_overRange() {
    assertEquals(-1, toLong(new BigInteger("340282366920938463463374607431768211455")));
    assertEquals(0, toLong(new BigInteger("-340282366920938463463374607431768211456")));
  }

  @Test
  void toLong_string() {
    assertEquals(9223372036854775807L, toLong("9223372036854775807"));
    assertEquals(-9223372036854775808L, toLong("-9223372036854775808"));
  }

  @Test
  void toDouble_wrapper() {
    assertEquals(0x1.fffffffffffffP+1023, toDouble(Double.valueOf("0x1.fffffffffffffP+1023")));
    assertEquals(0x0.0000000000001P-1022, toDouble(Double.valueOf("0x0.0000000000001P-1022")));
  }

  @Test
  void toDouble_overRange() {
    assertEquals(Double.POSITIVE_INFINITY, toDouble(new BigDecimal("1.7976931348623157E400")));
    assertEquals(0D, toDouble(new BigDecimal("4.9E-399")));
  }

  @Test
  void toDouble_string() {
    assertEquals(1.7976931348623157E308, toDouble("1.7976931348623157E308"));
    assertEquals(4.9E-324, toDouble("4.9E-324"));
  }
}