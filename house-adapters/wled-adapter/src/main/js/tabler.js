import React from 'react';

export function Page({children}) {
    return (
        <div className="page">
            { children }
        </div>
    );
};

export function Content({children}) {
    return (
        <div className="page-wrapper">
            { children }
        </div>
    );

}

export function SideMenu() {
    return (
        <aside className="navbar navbar-vertical navbar-expand-lg navbar-dark">
        <div className="container-fluid">
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbar-menu">
            <span className="navbar-toggler-icon"></span>
          </button>
          <h1 className="navbar-brand navbar-brand-autodark">
            <a href=".">
            </a>
          </h1>
          <div className="navbar-nav flex-row d-lg-none">
            <div className="nav-item d-none d-lg-flex me-3">
              <div className="btn-list">
                <a href="https://github.com/tabler/tabler" className="btn" target="_blank" rel="noreferrer">
                  Source code
                </a>
                <a href="https://github.com/sponsors/codecalm" className="btn" target="_blank" rel="noreferrer">
                  Sponsor
                </a>
              </div>
            </div>
            <div className="nav-item dropdown">
              <a href="#" className="nav-link d-flex lh-1 text-reset p-0" data-bs-toggle="dropdown" aria-label="Open user menu">
                <div className="d-none d-xl-block ps-2">
                  <div>Paweł Kuna</div>
                  <div className="mt-1 small text-muted">UI Designer</div>
                </div>
              </a>
              <div className="dropdown-menu dropdown-menu-end dropdown-menu-arrow">
                <a href="#" className="dropdown-item">Set status</a>
                <a href="#" className="dropdown-item">Profile &amp; account</a>
                <a href="#" className="dropdown-item">Feedback</a>
                <div className="dropdown-divider"></div>
                <a href="#" className="dropdown-item">Settings</a>
                <a href="#" className="dropdown-item">Logout</a>
              </div>
            </div>
          </div>
          <div className="collapse navbar-collapse" id="navbar-menu">

          </div>
        </div>
      </aside>
    );
}

export function Header({preTitle=null, title}) {
    return (
        <div className="container-xl">
          <div className="page-header d-print-none">
            <div className="row align-items-center">
              <div className="col">
                <div className="page-pretitle">
                  {preTitle}
                </div>
                <h2 className="page-title">
                  {title}
                </h2>
              </div>
              <div className="col-auto ms-auto d-print-none">
                <div className="btn-list">
                  <span className="d-none d-sm-inline">
                    <a href="#" className="btn btn-white">
                      New view
                    </a>
                  </span>
                  <a href="#" className="btn btn-primary d-none d-sm-inline-block" data-bs-toggle="modal" data-bs-target="#modal-report">
                    Create new report
                  </a>
                  <a href="#" className="btn btn-primary d-sm-none btn-icon" data-bs-toggle="modal" data-bs-target="#modal-report" aria-label="Create new report">
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
    )
}

export function Cards({children}) {
    return (
        <div className="page-body">
            <div className="container-xl">
                <div className="row row-deck row-cards">
                    {children}
                </div>
            </div>
        </div>
    )
}

export function Card({children}) {
    return (
        <div className="col-sm-6 col-lg-6">
            <div className="card">
                {children}
            </div>
        </div>
    );
}

export function CardTitle({children}) {
    return (
        <div className="card-header">
            <h3 className="card-title">{children}</h3>
        </div>
    );
}

export function CardBody({children}) {
    return (
        <div className="card-body">
            {children}
        </div>
    );
}

