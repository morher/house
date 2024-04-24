package net.morher.house.api.entity.sound;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class SoundCommand {
  private final List<SoundRequest> sounds = new ArrayList<>();

  public SoundCommand addSound(int delay, String soundName) {
    this.sounds.add(new SoundRequest(delay, soundName, null, null));
    return this;
  }

  public SoundCommand addTextToSpeach(int delayMs, String text, String voice) {
    this.sounds.add(new SoundRequest(delayMs, null, text, voice));
    return this;
  }

  @Data
  @RequiredArgsConstructor
  @AllArgsConstructor
  public static class SoundRequest {
    private int delayMs;
    private String sound;
    private String text;
    private String voice;
  }
}
