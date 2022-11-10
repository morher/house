package net.morher.house.api.mqtt.client;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StateCommandPair {
    private final Topic<Integer> state;
    private final Topic<Integer> command;

    public Topic<Integer> state() {
        return state;
    }

    public Topic<Integer> command() {
        return command;
    }
}