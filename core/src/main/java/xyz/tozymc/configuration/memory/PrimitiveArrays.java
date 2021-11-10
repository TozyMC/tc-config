package xyz.tozymc.configuration.memory;

import java.util.ArrayList;
import java.util.List;

final class PrimitiveArrays {
  private PrimitiveArrays() {}

  static List<Byte> toList(byte[] objects) {
    var list = new ArrayList<Byte>();
    for (var o : objects) {
      list.add(o);
    }
    return list;
  }

  static List<Short> toList(short[] objects) {
    var list = new ArrayList<Short>();
    for (var o : objects) {
      list.add(o);
    }
    return list;
  }

  static List<Integer> toList(int[] objects) {
    var list = new ArrayList<Integer>();
    for (var o : objects) {
      list.add(o);
    }
    return list;
  }

  static List<Long> toList(long[] objects) {
    var list = new ArrayList<Long>();
    for (var o : objects) {
      list.add(o);
    }
    return list;
  }

  static List<Float> toList(float[] objects) {
    var list = new ArrayList<Float>();
    for (var o : objects) {
      list.add(o);
    }
    return list;
  }

  static List<Double> toList(double[] objects) {
    var list = new ArrayList<Double>();
    for (var o : objects) {
      list.add(o);
    }
    return list;
  }

  static List<Character> toList(char[] objects) {
    var list = new ArrayList<Character>();
    for (var o : objects) {
      list.add(o);
    }
    return list;
  }

  static List<Boolean> toList(boolean[] objects) {
    var list = new ArrayList<Boolean>();
    for (var o : objects) {
      list.add(o);
    }
    return list;
  }
}
