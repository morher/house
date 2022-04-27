package net.morher.house.buttons.action;

import java.util.List;

import net.morher.house.api.device.Device;
import net.morher.house.api.device.DeviceId;
import net.morher.house.api.device.DeviceManager;
import net.morher.house.api.entity.EntityId;
import net.morher.house.api.entity.light.LightEntity;
import net.morher.house.api.entity.light.LightState;
import net.morher.house.api.entity.light.LightState.PowerState;
import net.morher.house.api.entity.switches.SwitchEntity;
import net.morher.house.buttons.action.ActionBlock.Builder;
import net.morher.house.buttons.config.ActionConfig;
import net.morher.house.buttons.config.ActionConfig.ConditionConfig;
import net.morher.house.buttons.config.ActionConfig.ConditionalActionConfig;
import net.morher.house.buttons.config.ActionConfig.LightConfig;
import net.morher.house.buttons.config.ActionConfig.SwitchConfig;
import net.morher.house.buttons.config.ButtonsConfig.InputConfig;

public class ActionBuilder {
    private final DeviceManager deviceManager;
    private final DeviceDefaults lamps;
    private final DeviceDefaults switches;

    public ActionBuilder(
            DeviceManager deviceManager,
            InputConfig inputConfig) {

        this.deviceManager = deviceManager;

        lamps = new DeviceDefaults(inputConfig.getLamps());
        switches = new DeviceDefaults(inputConfig.getSwitches());
    }

    public Action buildAction(List<ActionConfig> actionConfigs) {
        Builder block = ActionBlock.builder();

        for (ActionConfig actionConfig : actionConfigs) {
            addActions(block, actionConfig);
        }

        return block.build();
    }

    private void addActions(Builder block, ActionConfig actionConfig) {
        addFirstMatch(block, actionConfig.getFirstMatch());
        addLightAction(block, actionConfig.getLight());
        addSwitchAction(block, actionConfig.getSwitchCommand());
    }

    private void addFirstMatch(Builder block, List<ConditionalActionConfig> config) {
        if (config != null) {
            RunFirstMatchAction runFirst = new RunFirstMatchAction();
            for (ConditionalActionConfig conditionalAction : config) {
                runFirst.addAlternative(
                        buildAction(conditionalAction.getAction()),
                        buildCondition(conditionalAction.getCondition()));

            }
            block.add(runFirst);
        }
    }

    private void addLightAction(Builder block, LightConfig config) {
        if (config != null) {
            LightState lightState = buildLightState(config);

            List<DeviceId> devices = lamps.getDevices(config.getLamps(), config.getRefs());
            for (DeviceId deviceId : devices) {
                Device<LightEntity> lamp = deviceManager.lightDevice(deviceId);
                block.add(new CommandEntityAction<>(lamp.getMainEntity(), lightState));
            }
        }
    }

    private void addSwitchAction(Builder block, SwitchConfig config) {
        if (config != null) {
            List<DeviceId> devices = switches.getDevices(config.getSwitches(), config.getRefs());
            for (DeviceId deviceId : devices) {
                SwitchEntity switchEntity = deviceManager.getEntityManager().switchEntity(new EntityId(deviceId, null));
                block.add(new CommandEntityAction<>(switchEntity, config.isPower()));
            }
        }
    }

    private LightState buildLightState(LightConfig config) {
        LightState lightState = new LightState();
        if (config.getPower() != null) {
            lightState = lightState.withState(config.getPower()
                    ? PowerState.ON
                    : PowerState.OFF);
        }
        lightState = lightState.withBrightness(config.getBrightness());
        lightState = lightState.withEffect(config.getEffect());
        return lightState;
    }

    private Condition buildCondition(ConditionConfig config) {
        AllConditions conditions = new AllConditions();
        if (config != null) {
            addAllOfConditions(conditions, config.getAllOf());
            addAnyOfConditions(conditions, config.getAnyOf());
            addLightConditions(conditions, config.getLight());
            addNotCondition(conditions, config.getNot());
            addSwitchCondition(conditions, config.getSwitchCondition());
        }
        return conditions.optimize();
    }

    private void addAllOfConditions(AllConditions conditions, List<ConditionConfig> allOf) {
        if (allOf != null) {
            for (ConditionConfig conditionConfig : allOf) {
                conditions.add(buildCondition(conditionConfig));
            }
        }
    }

    private void addAnyOfConditions(AllConditions conditions, List<ConditionConfig> anyOf) {
        if (anyOf != null) {
            AnyCondition any = new AnyCondition();
            for (ConditionConfig conditionConfig : anyOf) {
                any.add(buildCondition(conditionConfig));
            }
            conditions.add(any.optimize());
        }
    }

    private void addLightConditions(AllConditions conditions, LightConfig config) {
        if (config != null) {
            LightState lightState = buildLightState(config);
            List<DeviceId> devices = lamps.getDevices(config.getLamps(), config.getRefs());
            for (DeviceId deviceId : devices) {
                Device<LightEntity> lamp = deviceManager.lightDevice(deviceId);
                conditions.add(new LightCondition(lamp.getMainEntity(), lightState));
            }
        }
    }

    private void addNotCondition(AllConditions conditions, ConditionConfig not) {
        if (not != null) {
            conditions.add(new NotCondition(buildCondition(not)));
        }
    }

    private void addSwitchCondition(AllConditions conditions, SwitchConfig config) {
        if (config != null) {
            List<DeviceId> devices = switches.getDevices(config.getSwitches(), config.getRefs());
            for (DeviceId deviceId : devices) {
                SwitchEntity switchEntity = deviceManager.getEntityManager().switchEntity(new EntityId(deviceId, null));
                conditions.add(new EntityStateCondition<>(switchEntity, config.isPower()));
            }
        }
    }
}
