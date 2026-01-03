import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Dashboard from './pages/Dashboard';
import Devices from './pages/Devices';
import Sensors from './pages/Sensors';

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-dark-900 text-white">
        {/* Content */}
        <div className="relative z-10">
          <Navbar />
          <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/devices" element={<Devices />} />
              <Route path="/sensors" element={<Sensors />} />
            </Routes>
          </main>
        </div>
      </div>
    </Router>
  );
}

export default App;
