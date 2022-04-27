# Wled Adapter

Control [WLED](https://kno.wled.ge/) managed LED strips through MQTT.

This adapter support sharing one ESP on multiple separate LED strips by exposing the individual segments as separate lamps.

## Presets
Presets define the effect and palette to be used on a LED strip. The preset style sets the three colors, the palette, intensity, speed and effect.

## Configuration
The adapter supports sharing configuration files with other adapters by using the namespace `wled`.
Within `lamps` each lamp is listed, identified by its device name and id. The MQTT base-`topic` for the WLED instance must also be provided.   

### Example
```yaml
wled:
   lamps:      
    - device:
         room: 'Living room'
         name: 'Mood lamp'
      id: livingroom-mood-lamp
      topic: 'wled/living-room-mood-lamp'
      segment: 0

   presets:
      'Auto':
         style:
            colors: ['#fff', '#000', '#000']
            palette: 0
            intensity: 0
            speed: 0
            effect: 0

      'Colorful':
         style:
            colors: ['#000', '#000', '#000']
            palette: 2
            intensity: 128
            speed: 81
            effect: 63
```

In this example we have created one lamp that will show up as a device and an entity in Home Assistant.
