import { createBrowserRouter } from "react-router-dom";
import App from "./App";
import Home from "./pages/Home";
import Cart from "./pages/Cart";
import Checkout from "./pages/Checkout";
import Products from "./pages/Products";
import ProductDetails from "./pages/ProductDetail";

const router = createBrowserRouter([
    {
        path:"/",
        element: <App/>,
        children: [
            {                
                element: <Home/>,
                index:true
            },
            {
                path:"products",
                element: <Products/>
            },
            {
                path:"product/:id",
                element: <ProductDetails/>
            },
            {
                path:"cart",
                element: <Cart/>
            },
            {
                path:"checkout",
                element: <Checkout/>
            }
        ]
    }
]);

export default router;
