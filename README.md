# House
Tools for home automation.

House consists of common tools to create adapters between hardware and MQTT through common patterns and conventions. Adapters expose devices to MQTT through one or more entities. The device is identified by a room name and the name of the device itself. An entity is identified by it's device and an entity name. The entities can control the primary functions of the device as well as additional states or report aspect of the device like battery level or signal strength.

## Entities
The entity types are set up to line up with the entity types in Home Assistant. 

| Type          | Use                                  | Home Assistant documentation                                                           |
|---------------|--------------------------------------|----------------------------------------------------------------------------------------|
| Binary sensor | Motion sensors, door sensors...      | [MQTT Binary Snesor](https://www.home-assistant.io/integrations/binary_sensor.mqtt/)   |
| Cover         | Window blinds, awning...             | [MQTT Cover](https://www.home-assistant.io/integrations/cover.mqtt/)                   |
| Lights        | Lamps, LED-strips...                 | [MQTT Lights](https://www.home-assistant.io/integrations/light.mqtt/)                  |
| Sensor        | Temperature sensors...               | [MQTT Sensor](https://www.home-assistant.io/integrations/sensor.mqtt/)                 |
| Switch        | Devices that can be turned on or off | [MQTT Switches](https://www.home-assistant.io/integrations/switch.mqtt/)               |
| Trigger       | Remote controls, buttons...          | [MQTT Device Trigger](https://www.home-assistant.io/integrations/device_trigger.mqtt/) |
