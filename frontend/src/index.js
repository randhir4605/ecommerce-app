import ReactDOM from 'react-dom/client';
import { RouterProvider } from "react-router-dom";
import router from "./Router";
import "bootstrap/js/dist/carousel";
import "./main.css";

const root = ReactDOM.createRoot(document.getElementById('root'));
// don;t add providers here otherwise context will not be part of router 
// and navigate() will not work if you want navigation from context
root.render(<RouterProvider router={router} />);
