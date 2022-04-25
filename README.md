# House
Tools for home automation.

House consists of common tools to create adapters between hardware and MQTT throuh common patterns and convensions. Adapters expose devices to MQTT through one or more entities. The device is identified by a room name and the name of the device itself. An entity is identified by it's device and an optional entity name. There can only be one entity without a specified name. It is refered to as the devices main entity. named entities are sub-entities that controls additional states or reports aspect of the device like battery level or signal strenth.

## Entities
The entity types are set up to line up with the entity types in Home Assistant. 

| Type   | Use                                  | Home Assistant documentation                                             |
|--------|--------------------------------------|--------------------------------------------------------------------------|
| Lights | Lamps, LED-strips...                 | [MQTT Lights](https://www.home-assistant.io/integrations/light.mqtt/)    |
| Switch | Devices that can be turned on or off | [MQTT Switches](https://www.home-assistant.io/integrations/switch.mqtt/) |


## Adapters
Adapters implement the functionality behind devices and entities, reacting to commands and reporting sensor data.

| Adapter                                   | Description                                                                                         |
|-------------------------------------------|-----------------------------------------------------------------------------------------------------|
| [Modes](./house-adapters/modes-adapter/)  | Creates virtual devices to set states such as guest mode, vacation mode, etc.                       |
| [Wiz](./house-adapters/wizlight-adapter/) | Control light bulbs from [WiZ](https://www.wizconnected.com/en-gb) through the local UDP interface. |

