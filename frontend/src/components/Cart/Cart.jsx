import { Link, useLocation, Navigate } from "react-router-dom";
import "./Cart.css";
import { useCartContext } from "../../context/CartContext";
import { useAuthContext } from "../../context/AuthContext";
import Button from "../Button/Button";

function Cart() {
    const { cartItems, updateCart, removeFromCart } = useCartContext();
    const { user } = useAuthContext();
    const location = useLocation();
    const total = cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0);

    
    if(!user){
        return <Navigate to="/login" state={{from:location}} replace />
    }
    if (cartItems.length === 0) {
        return <h2 className="empty-cart">Your cart is empty</h2>;
    }

    return (
        <div className="cart">
            <h2>Shopping Cart</h2>

            <div className="cart-items">
                {cartItems.map((item) => (
                    <div className="cart-item" key={item.id}>
                        <Link to={`/product/${item.id}`} state={{ product: item }} >
                            <img src={item.thumbnail} alt={item.title} />
                        </Link>
                        <div>
                            <text>{item.title}</text>
                            <p>${item.price}</p>
                            <text>Quantity: </text>
                            <div className="quantity-buttons">
                                <button onClick={()=> updateCart(item, Math.max(1, item.quantity - 1))}>-</button>
                                <span>{item.quantity}</span>
                                <button onClick={()=> updateCart(item, Math.min(10, item.quantity + 1))}>+</button>    
                            </div>
                            
                        </div>
                        <strong> ${(item.price * item.quantity).toFixed(2)} </strong>
                        <Button variant="secondary" onClick={() => removeFromCart(item)} > Remove </Button>
                    </div>
                ))}
            </div>

            <div className="cart-total">
                <h2>Total: ${total.toFixed(2)}</h2>
                <Button size="medium">Checkout</Button>
            </div>
        </div>
    );
}

export default Cart;
