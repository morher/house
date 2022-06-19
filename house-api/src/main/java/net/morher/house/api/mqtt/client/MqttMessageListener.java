package net.morher.house.api.mqtt.client;

import java.util.function.Consumer;

import net.morher.house.api.mqtt.payload.PayloadFormat;

public interface MqttMessageListener {
    void onMessage(String topic, byte[] data, int qos, boolean retained);

    public interface ParsedMqttMessageListener<T> {
        void onMessage(String topic, T data, int qos, boolean retained);
    }

    public static class ParsingMqttMessageListener<T> implements MqttMessageListener {
        private final PayloadFormat<T> mapper;
        private final ParsedMqttMessageListener<? super T> listener;

        public ParsingMqttMessageListener(PayloadFormat<T> mapper, ParsedMqttMessageListener<? super T> listener) {
            this.mapper = mapper;
            this.listener = listener;
        }

        @Override
        public void onMessage(String topic, byte[] data, int qos, boolean retained) {
            listener.onMessage(topic, mapper.deserialize(data), qos, retained);
        }
    }

    public static class MessageForwarder<T> implements ParsedMqttMessageListener<T> {
        private final Consumer<T> delegate;

        public MessageForwarder(Consumer<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void onMessage(String topic, T data, int qos, boolean retained) {
            delegate.accept(data);
        }
    }

    public static class ParsingMqttMessageListenerBuilder<T> {
        private final PayloadFormat<T> mapper;

        public ParsingMqttMessageListenerBuilder(PayloadFormat<T> mapper) {
            this.mapper = mapper;
        }

        public MqttMessageListener thenNotify(ParsedMqttMessageListener<? super T> listener) {
            return new ParsingMqttMessageListener<>(mapper, listener);
        }

        public MqttMessageListener thenNotify(Consumer<? super T> listener) {
            return new ParsingMqttMessageListener<>(mapper, new MessageForwarder<>(listener));
        }
    }

    public static <T> ParsingMqttMessageListenerBuilder<T> map(PayloadFormat<T> mapper) {
        return new ParsingMqttMessageListenerBuilder<>(mapper);
    }
}
