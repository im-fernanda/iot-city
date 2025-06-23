import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Navbar.css';

const Navbar: React.FC = () => {
  const location = useLocation();

  const isActive = (path: string) => {
    return location.pathname === path;
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <Link to="/" className="navbar-logo">
          🌆 IoT City by Gilneide Fernanda
        </Link>
      </div>
      
      <div className="navbar-menu">
        <Link 
          to="/" 
          className={`navbar-item ${isActive('/') ? 'active' : ''}`}
        >
          📊 Dashboard
        </Link>
        
        <Link 
          to="/devices" 
          className={`navbar-item ${isActive('/devices') ? 'active' : ''}`}
        >
          📱 Dispositivos
        </Link>
        
        <Link 
          to="/sensors" 
          className={`navbar-item ${isActive('/sensors') ? 'active' : ''}`}
        >
          📈 Sensores
        </Link>
      </div>
    </nav>
  );
};

export default Navbar; 