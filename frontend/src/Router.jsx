import { createBrowserRouter } from "react-router-dom";
import App from "./App";
import Home from "./pages/Home";
import Cart from "./components/Cart/Cart";
import Checkout from "./components/Checkout/Checkout";
import Products from "./components/Products/Products";
import ProductDetails from "./components/ProductDetail/ProductDetail";

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
