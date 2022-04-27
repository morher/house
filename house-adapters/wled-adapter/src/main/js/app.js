import React, { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route, Link, useParams } from "react-router-dom";
import Dashboard from './dashboard.js';
import ControlPanel from './controlpanel.js';
import AppHeader from './header.js';
import axios from 'axios';

export default function App() {
    const [user, setUser] = useState({userId: null, name: null, ledStrips: []});

    useEffect(() => {
        axios.get("/user")
        .then((response) => { setUser(response.data); });
    }, []);

    return (
        <>
        <BrowserRouter>
            <AppHeader strips={user.ledStrips} />
            <Routes>
                <Route path="/" element= {<Dashboard strips={user.ledStrips} />} />
                <Route path="/d/:deviceId/" element={<ControlPanel />} />
            </Routes>
        </BrowserRouter>
        </>
        );
}
