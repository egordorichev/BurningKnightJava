package org.rexcellentgames.burningknight.util;

public class MathUtils {
  public static float clamp(float min, float max, float val) {
    return Math.max(min, Math.min(max, val));
  }

  public static float map(float x, float in_min, float in_max, float out_min, float out_max) {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
  }
}