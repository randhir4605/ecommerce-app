import Navbar from "./components/Navbar/Navbar";
import Footer from './components/Footer/Footer'
import { Outlet } from "react-router-dom";
function App() {
  return (
    
    <div>
      <Navbar></Navbar>
        <Outlet/>
      <Footer></Footer>
    </div>
  );
}

export default App;
