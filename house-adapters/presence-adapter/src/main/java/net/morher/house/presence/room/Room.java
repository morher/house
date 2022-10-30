package net.morher.house.presence.room;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.morher.house.api.devicetypes.RoomSensorDevice;
import net.morher.house.api.entity.Device;
import net.morher.house.api.entity.sensor.BinarySensorEntity;
import net.morher.house.api.entity.sensor.BinarySensorOptions;
import net.morher.house.api.entity.sensor.BinarySensorType;
import net.morher.house.api.schedule.DelayedTrigger;
import net.morher.house.api.schedule.NamedTask;
import net.morher.house.api.state.StateEvent;

@RequiredArgsConstructor
@Setter
public class Room {
    // private final ScheduledExecutorService scheduler;
    private final BinarySensorEntity motionEntity;
    private final BinarySensorEntity presenceEntity;
    private final DelayedTrigger updateMotionStateTask;
    private final DelayedTrigger updatePresenceTask;
    private Duration cooldown = Duration.ofSeconds(0);
    private boolean allExitsObserved;
    private final List<MotionSensor> motionSensors = new ArrayList<>();
    private StateEvent<Boolean> motionReport;
    private Instant lastExitOpertunity;
    private boolean currentPresence;

    public Room(ScheduledExecutorService scheduler, Device device) {
        // this.scheduler = scheduler;
        updateMotionStateTask = new DelayedTrigger(new NamedTask(this::updateState, "Update motion state"), scheduler);
        updatePresenceTask = new DelayedTrigger(new NamedTask(this::updateState, "Update presence state"), scheduler);
        motionEntity = device.entity(RoomSensorDevice.MOTION, new BinarySensorOptions(BinarySensorType.MOTION));
        presenceEntity = device.entity(RoomSensorDevice.PRESENCE, new BinarySensorOptions(BinarySensorType.PRESENCE));
    }

    public Room cooldown(Duration duration) {
        this.cooldown = duration;
        return this;
    }

    public Room allExitsObserved(boolean allExitsObserved) {
        this.allExitsObserved = allExitsObserved;
        return this;
    }

    public void addMotionSensor(MotionSensor motionSensor) {
        motionSensors.add(motionSensor);
        motionSensor.addListener(this::onMotionStateChanged);
    }

    private void onMotionStateChanged(boolean motionState) {
        updateMotionStateTask.runNow();
    }

    private void updateState() {
        updateMotionState();
        // TODO: updateExitOpertunityState()
        updatePresenceState();
    }

    private void updateMotionState() {
        boolean motionState = isMotionDetected();
        if (motionReport == null || motionReport.getState() != motionState) {
            motionReport = new StateEvent<>(motionState, Instant.now());
            motionEntity.state().publish(isMotionDetected());
        }
    }

    private void updatePresenceState() {
        if (motionReport == null || motionReport.getState() == currentPresence) {
            return;

        } else if (motionReport.getState()) {
            currentPresence = true;
            presenceEntity.state().publish(true);

        } else if (hasExitOpertunityOccured()) {
            Instant cooldownTime = motionReport.getEventTime().plus(cooldown);
            if (cooldownTime.isAfter(Instant.now())) {
                updatePresenceTask.runAt(cooldownTime);

            } else {
                currentPresence = false;
                presenceEntity.state().publish(false);
            }
        } else {
            // TODO: Timeout...

        }
    }

    private boolean hasExitOpertunityOccured() {
        if (!allExitsObserved || motionReport == null) {
            return true;
        }
        return lastExitOpertunity == null
                && lastExitOpertunity.isAfter(motionReport.getEventTime());
    }

    private boolean isMotionDetected() {
        for (MotionSensor sensor : motionSensors) {
            if (sensor.isMotionDetected()) {
                return true;
            }
        }
        return false;
    }

    @Data
    private static class MotionReport {
        private final boolean motionDetected;
        private final Instant reportTime;
    }
}
