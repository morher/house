package net.morher.house.api.utils.color;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Color {
  private static final Pattern HEX_PATTERN = Pattern.compile("\\#[0-9a-fA-F]{6}");
  private final int red;
  private final int green;
  private final int blue;

  @JsonCreator
  public static Color fromHex(String hexColor) {
    String fullHex = toLongHex(hexColor);
    return new Color(
        Integer.parseInt(fullHex, 1, 3, 16),
        Integer.parseInt(fullHex, 3, 5, 16),
        Integer.parseInt(fullHex, 5, 7, 16));
  }

  public int[] toArray() {
    return new int[] {red, green, blue};
  }

  @JsonValue
  public String toHex() {
    return String.format("#%02x%02x%02x", red, green, blue);
  }

  @Override
  public String toString() {
    return toHex();
  }

  public static String toLongHex(String hexColor) {
    if (hexColor == null) {
      return hexColor;
    }
    hexColor = hexColor.toLowerCase();
    if (hexColor.length() == 4) {
      hexColor =
          new StringBuilder()
              .append(hexColor.charAt(0))
              .append(hexColor.charAt(1))
              .append(hexColor.charAt(1))
              .append(hexColor.charAt(2))
              .append(hexColor.charAt(2))
              .append(hexColor.charAt(3))
              .append(hexColor.charAt(3))
              .toString();
    }
    validateHex(hexColor);
    return hexColor;
  }

  public static void validateHex(String longHex) {
    if (!HEX_PATTERN.matcher(longHex).matches()) {
      throw new IllegalArgumentException("'" + longHex + "' is not a valif hex color");
    }
  }
}
