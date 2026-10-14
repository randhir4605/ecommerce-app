import { useEffect, useState, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuthContext } from "../../context/AuthContext";
import { useCartContext } from "../../context/CartContext";
import Button from "../Button/Button";
import FormLayout from "../FormLayout/FormLayout";

export default function Signup(){
    const [username,setUsername]=useState("");
    const [password,setPassword]=useState("");
    const [name,setName]=useState("");
    const [contact,setContact]=useState("");
    const navigate = useNavigate();
    const {login}=useAuthContext();
    const location=useLocation();
    const {addProductToCart} =useCartContext();
    const pendingItem = location.state?.pendingCartItem;
    const usernameRef=useRef(null);

    useEffect(()=>{
        usernameRef.current?.focus();
    });
    const handleSignup = async (event)=>{
        event.preventDefault();
        const response = await fetch(`${process.env.REACT_APP_API_URL}`.concat("/api/public/auth/signup"),
            {
            method: "POST",
                    credentials: "include",
                    headers: {
                        "Content-Type":"application/json"
                    },
                    body: JSON.stringify({
                        username,password,name,contact
                    })  
            }    
        );
        if(!response.ok){alert("Error".concat(response.status))}
        else{
            const profileData=await response.json();
            login(profileData);  //set auth context
            // Add user logged in from add to cart redirect then after successful login the item should be added into cart
            if (pendingItem) {
                addProductToCart(pendingItem.product, pendingItem.quantity);
            }
            setPassword(null);
            navigate(location.state?.from || "/", { replace: true });
        }
    }
    return (
        <FormLayout id="signup-page" onSubmit={handleSignup} title="Create Account">
            <input id="username" name="username" type="text" placeholder="username" ref={usernameRef}/>
            <input name="password" id="password" type="password" placeholder="Password"/>
            <input name="name" id="name" type="text" placeholder="Full Name"/>
            <input name="contact" id="contact" type="tel" placeholder="Mobile Number"/>
            <Button size="large" onSubmit={handleSignup}>Create Account</Button>
        </FormLayout>
    );
}