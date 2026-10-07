import { Link } from "react-router-dom";
import "./Navbar.css";
import { useCart } from "../Cart/CartContext";

export default function Navbar() {
    const { cartItems } = useCart();
    const cartItemCount = cartItems.reduce((total, item) => total + item.quantity, 0);
    return (
        <nav className="navbar">
            <div className="brand brand2">
                <Link to="/">C-Store</Link>
            </div>
            <div className="nav-links">
                <Link to="/">Home</Link>
                <Link to="/products">Products</Link>
            </div>

            <Link to="/cart" className="cart-link" aria-label="Cart">
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                >
                    <circle cx="9" cy="21" r="1" />
                    <circle cx="20" cy="21" r="1" />
                    <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6" />
                </svg>
                {cartItemCount > 0 && <span className="cart-count">{cartItemCount}</span>}
            </Link>
        </nav>
    );
}
