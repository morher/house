# House
Tools for home automation.

House consists of common tools to create adapters between hardware and MQTT through common patterns and conventions. Adapters expose devices to MQTT through one or more entities. The device is identified by a room name and the name of the device itself. An entity is identified by it's device and an entity name. The entities can control the primary functions of the device as well as additional states or report aspect of the device like battery level or signal strength.

## Entities
The entity types are set up to line up with the entity types in Home Assistant. 

| Type   | Use                                  | Home Assistant documentation                                             |
|--------|--------------------------------------|--------------------------------------------------------------------------|
| Lights | Lamps, LED-strips...                 | [MQTT Lights](https://www.home-assistant.io/integrations/light.mqtt/)    |
| Switch | Devices that can be turned on or off | [MQTT Switches](https://www.home-assistant.io/integrations/switch.mqtt/) |


## Adapters
Adapters implement the functionality behind devices and entities, reacting to commands and reporting sensor data.

| Adapter                                      | Description                                                                                         |
|----------------------------------------------|-----------------------------------------------------------------------------------------------------|
| [Buttons](./house-adapters/buttons-adapter/) | Handles button input like light switches etc.                                                       |
| [Modes](./house-adapters/modes-adapter/)     | Creates virtual devices to set states such as guest mode, vacation mode, etc.                       |
| [Wiz](./house-adapters/wizlight-adapter/)    | Control light bulbs from [WiZ](https://www.wizconnected.com/en-gb) through the local UDP interface. |
| [Wled](./house-adapters/wled-adapter/)       | Control [WLED](https://kno.wled.ge/) managed LED strips.                                            |

