import ReactDOM from 'react-dom/client';
import App from "./App";
import { RouterProvider } from "react-router-dom";
import router from "./Router";
import { CartProvider } from "./components/Cart/CartContext";
import "bootstrap/js/dist/carousel";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
      <CartProvider>
            <RouterProvider router={router}>
                  <App />
            </RouterProvider>
      </CartProvider>
);
