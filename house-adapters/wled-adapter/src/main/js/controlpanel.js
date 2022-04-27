import axios from 'axios';
import React from 'react';
import { useState, useEffect } from 'react';
import { Col, Container, Row, ToggleButton } from 'react-bootstrap';
import { Link, useParams } from "react-router-dom";
import { Page, Header, Cards, Card, CardTitle, CardBody, SideMenu, Content } from "./tabler.js";

export default function ControlPanel() {
    const { deviceId } = useParams();

    const [ledState, setLedState] = useState({color1: '#ffffff', color2: '#000000', color3: '#000000', effect: 0, palette: 0, speed: 128, intensity: 128});
    const [effects, setEffects] = useState([]);
    const [palettes, setPalettes] = useState([]);

    useEffect(() => {
        console.log("Load data for: ", deviceId);
        axios.get('effects').then(({data}) => { setEffects(data.sort((a, b) => a.name.localeCompare(b.name)))});
        axios.get('palettes').then(({data}) => { setPalettes(data.sort((a, b) => a.name.localeCompare(b.name)))});
        axios.get('state').then(({data}) => { setLedState(data); });

    }, [deviceId]);

    function changeLedState(state) {
        setLedState(state);
        axios.post('state', state);
    }

    console.info(ledState);

    const paletteList = palettes.map((palette) => {
        return <Col key={palette.id}>
            <LedPalette
                palette={palette}
                selected={palette.id == ledState.palette}
                onSelect={(paletteId) => {changeLedState({... ledState, palette: paletteId}); }} />
        </Col>
    });

    const effectList = effects.map((effect) => {
        return <LedEffect
            key={effect.id}
            effect={effect}
            selected={effect.id == ledState.effect}
            onSelect={(effectId) => {changeLedState({... ledState, effect: effectId}); }} />
    });

    return (
        <Container>
                <Header preTitle="Control Panel" title={deviceId}></Header>
                <Cards>
                    <Card>
                        <CardTitle>Colors</CardTitle>
                        <CardBody>
                            <ColorPicker name="Color 1" color={ledState.color1} onChange={(color) => {changeLedState({...ledState, color1: color})}} />
                            <ColorPicker name="Color 2" color={ledState.color2} onChange={(color) => {changeLedState({...ledState, color2: color})}} />
                            <ColorPicker name="Color 3" color={ledState.color3} onChange={(color) => {changeLedState({...ledState, color3: color})}} />
                            <div className="palettes">
                                { paletteList }
                            </div>
                        </CardBody>
                    </Card>
                    <Card>
                        <CardTitle>Effects</CardTitle>
                        <CardBody>
                            <input type="range" min="0" max="255"
                                value={ledState.speed} onChange={(event) => { changeLedState({...ledState, speed: 1 * event.target.value}) }}
                                className="slider" id="speed" />
                            <input type="range" min="0" max="255"
                                value={ledState.intensity} onChange={(event) => { changeLedState({...ledState, intensity: 1 * event.target.value}) }}
                                className="slider" id="intensity" />
                            <ul className="effects">{ effectList }</ul>
                        </CardBody>
                    </Card>
                </Cards>
        </Container>
    );
 
}

function createPalettePreview(palette) {
    const preview = 'linear-gradient(to right, '
      + palette.style.map((style) => {
          if (Array.isArray(style)) {
              return 'rgb(' + style[1] + ',' + style[2] + ',' + style[3] + ') ' + (style[0] * 100 / 255) + '%'
          }
          return '';
      })
      + ')';
      return preview;
}

function LedPalette({palette, selected, onSelect=function(){}}) {
    return (
        <ToggleButton
            className='palette'
            size="sm"
            type="checkbox"
            variant="outline-primary"
            checked={selected}
            value={palette.id}
            onClick={(e) => { onSelect(palette.id); }} >
            {palette.name}<div className='palette-preview' style={{ background: createPalettePreview(palette)}} />
        </ToggleButton>
    );
}

function LedEffect({effect, selected=false, onSelect=function(){}}) {
    // return <li
    //     onClick={ function() { onSelect(effect.id); } }
    //     className={selected ? 'selected' : ''}>
    //     {effect.name}
    // </li>;
    return (
        <ToggleButton
            className='effect'
            size="sm"
            type="checkbox"
            variant="outline-primary"
            checked={selected}
            value={effect.id}
            onClick={(e) => { onSelect(effect.id); }}>
            {effect.name}
        </ToggleButton>
    );
}

function ColorPicker({name, color='#000000', onChange=function(){} }) {

    function onColorSelected(event) {
        onChange(event.target.value);
    };

    return <div>
            <input type="color" value={color} onChange={onColorSelected} />&nbsp;
            <label >{name}</label>
        </div>
}

