import { useCart } from "./CartContext";
import "./Cart.css";

function Cart() {
    const { cartItems, removeFromCart } = useCart();

    const total = cartItems.reduce(
        (sum, item) => sum + item.price * item.quantity,
        0
    );

    if (cartItems.length === 0) {
        return <h2 className="empty-cart">Your cart is empty</h2>;
    }

    return (
        <div className="cart">
            <h1>Shopping Cart</h1>

            <div className="cart-items">
                {cartItems.map((item) => (
                    <div className="cart-item" key={item.id}>

                        <img
                            src={item.thumbnail}
                            alt={item.title}
                        />

                        <div>
                            <h3>{item.title}</h3>
                            <p>${item.price}</p>
                            <p>Quantity: {item.quantity}</p>
                        </div>

                        <strong>
                            ${(item.price * item.quantity).toFixed(2)}
                        </strong>

                        <button
                            onClick={() => removeFromCart(item.id)}
                        >
                            Remove
                        </button>

                    </div>
                ))}
            </div>

            <div className="cart-total">
                <h2>Total: ${total.toFixed(2)}</h2>

                <button>Checkout</button>
            </div>
        </div>
    );
}

export default Cart;
