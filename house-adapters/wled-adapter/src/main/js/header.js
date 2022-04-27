import React from 'react';
import { Navbar, Container, NavDropdown, Nav } from 'react-bootstrap';


export default function AppHeader({strips}) {

    return (
        <Navbar bg="dark" variant="dark">
            <Container>
                <Navbar.Brand href='/'>WLED</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="me-auto">
                    <NavDropdown title="Lamps" id="basic-nav-dropdown">
                        { strips.map((s) => <NavDropdown.Item key={s.id} href={"/d/" + s.id + "/"}>{s.room} - {s.device}</NavDropdown.Item>) }
                    </NavDropdown>
                </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}
