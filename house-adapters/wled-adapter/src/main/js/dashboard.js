import React from 'react';
import { Container } from 'react-bootstrap';
import { Link } from "react-router-dom";

export default function Dashboard({strips}) {
    return (
        <>
            <Container>
                <h1>WLED Dashboard</h1>
                { strips.map((s) => <p key={s.id}><Link to={"/d/" + s.id + "/"}>{s.room} - {s.device}</Link></p>) }
            </Container>
        </>);
}
