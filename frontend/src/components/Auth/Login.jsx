import { useState } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import { useAuthContext } from "../../context/AuthContext";
import { useCartContext } from "../../context/CartContext";
import FormLayout from "../FormLayout/FormLayout";
import Button from "../Button/Button";

export default function Login(){

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();
    const location = useLocation();
    const { login } = useAuthContext();
    const { addProductToCart } = useCartContext();

    const pendingItem = location.state?.pendingCartItem;
    const from = location.state?.from || "/";
    //const authUrl = `${process.env.REACT_APP_API_URL}`.concat("/api/public/auth/login");
    const authUrl = "https://dummyjson.com/auth/login";

    //keep response simple and align with dummyjson.com
    /* 
    {
        "id": 1,
        "username": "emilys",
        "email": "emily.johnson@x.dummyjson.com",
        "firstName": "Emily",
        "lastName": "Johnson",
        "gender": "female",
        "image": "https://dummyjson.com/icon/emilys/128",
        "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", // JWT accessToken (for backward compatibility) in response and cookies
        "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." // refreshToken in response and cookies
    }
    */

    const handleLogin = async (event)=>{
        event.preventDefault();
        setError("")
        setLoading(true);
        try{
            const response = await fetch(authUrl, {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type":"application/json"
                },
                body: JSON.stringify({
                    username,password
                })
            })
            const result = await response.json().catch(() => ({}));
            if(!response.ok){
                setError(result.error || result.message || `Login failed (${response.status})`);
                return;
            }
            login(result);
            
            // Add user logged in from add to cart redirect then after successful login the item should be added into cart
            if (pendingItem) {
                addProductToCart(pendingItem.product, pendingItem.quantity);
            }
            
            setPassword("");
            setSuccess("Login Successful");
            navigate(from, { replace: true });
        }catch(err){
            setError(err.message || "Unable to connect the server")
        }finally{
            setLoading(false);
        }
    }
    return(
        <FormLayout id="login-page" title="Login" onSubmit={handleLogin}>
        
            <input id="username" type="text" placeholder="Username" value={username} onChange={(event) => setUsername(event.target.value)} required />                 
            <input id="password" type="password" placeholder="Password" value={password} onChange={(event) => setPassword(event.target.value)} required />
            <Button type="submit" size="large" disabled={loading}>{loading ? "Logging in..." : "Login"}</Button>
            {error && <p role="alert">{error}</p>}
            {success && <p role="status">{success}</p>}
        
            <p>Don't have an account? <Link to="/signup">Sign up</Link></p>
                
        </FormLayout>
    );
}