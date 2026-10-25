import Navbar from "./components/Navbar/Navbar";
import Footer from './components/Footer/Footer'
import { Outlet } from "react-router-dom";
import './App.css'

function App() {
    return (
        <div className="app-layout">
            <Navbar />
            <main className="app-main">
                <Outlet />
            </main>
            <Footer />
        </div>
    );
}

export default App;
