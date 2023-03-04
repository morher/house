package net.morher.house.api.mqtt.payload;

import java.util.function.Function;

public class EnumMessage {

  public static <E extends Enum<E>> PayloadFormat<E> asDeclared(Class<E> enumType) {
    return new EnumMapper<>(enumType, (e) -> e.name().getBytes());
  }

  public static <E extends Enum<E>> PayloadFormat<E> lowercase(Class<E> enumType) {
    return new EnumMapper<>(enumType, (e) -> e.name().toLowerCase().getBytes());
  }

  private static class EnumMapper<E extends Enum<E>> implements PayloadFormat<E> {
    private final Class<E> enumType;
    private final Function<E, byte[]> formatter;

    public EnumMapper(Class<E> enumType, Function<E, byte[]> formatter) {
      this.enumType = enumType;
      this.formatter = formatter;
    }

    @Override
    public byte[] serialize(E value) {
      return formatter.apply(value);
    }

    @Override
    public E deserialize(byte[] payload) {
      String value = new String(payload);
      for (E constant : enumType.getEnumConstants()) {
        if (constant.name().equalsIgnoreCase(value)) {
          return constant;
        }
      }
      throw new IllegalArgumentException("Invalid value " + value);
    }
  }
}
