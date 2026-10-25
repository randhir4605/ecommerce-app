import { createBrowserRouter, Outlet } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { CartProvider } from "./context/CartContext";
import App from "./App";
import Home from "./pages/home/Home";
import Cart from "./components/Cart/Cart";
import Checkout from "./components/Checkout/Checkout";
import Products from "./components/Products/Products";
import ProductDetails from "./components/ProductDetail/ProductDetail";
import Login from "./components/Auth/Login";
import Signup from "./components/Auth/Signup";
import Profile from "./components/Profile/Profile";
import UpdateProfile from "./components/Profile/UpdateProfile";
import ProfileLayout from "./components/Profile/ProfileLayout";
import { productsLoader, categoryProductsLoader } from "./components/Products/productsLoaders";

function AppProviders() {
     // the order / wrap will be like which provider is using which one. 
      // CartProvider is using AuthProvider in CartContext sCartProvider will wrapped in AuthProvider
    return (
        <AuthProvider>
            <CartProvider>
                <Outlet />
            </CartProvider>
        </AuthProvider>
    );
}

const router = createBrowserRouter([
    {
        element: <AppProviders/>,
        children: [
            {
                path:"/",
                element: <App />,
                children:[
                    { index: true, element: <Home /> },
                    { path: "products", element: <Products />, loader: productsLoader },
                    { path: "products/category/:name", element: <Products />, loader: categoryProductsLoader },
                    { path: "product/:id", element: <ProductDetails /> },
                    { path: "cart", element: <Cart /> },
                    { path: "checkout", element: <Checkout /> },
                    { path: "login", element: <Login /> },
                    { path: "signup", element: <Signup /> },
                    { path: "profile", element: <ProfileLayout />, children: [
                        { index:true, element: <Profile /> },
                        { path:"edit", element: <UpdateProfile/>}] }
                ]
            } 
        ]
    }
]);

export default router;
