
import { Link } from "react-router-dom";
import "./Navbar.css";
import { useCartContext } from "../../context/CartContext";
import { useAuthContext } from "../../context/AuthContext";

export default function Navbar() {
    const { cartItems } = useCartContext();
    const { user } = useAuthContext();
    const cartItemCount = cartItems.reduce((total, item) => total + item.quantity, 0);

    return (
        <nav className="navbar">
            <div className="brand brand2"><Link to="/">C-Store</Link></div>
            <div className="nav-links"><Link to="/">Home</Link><Link to="/products">Products</Link></div>
            <div className="nav-actions">
                <Link to="/cart" className="cart-link" aria-label="Cart">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <circle cx="9" cy="21" r="1" /><circle cx="20" cy="21" r="1" />
                        <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6" />
                    </svg>
                    {cartItemCount > 0 && <span className="cart-count">{cartItemCount}</span>}
                </Link>

                <Link to={user ? "/profile" : "/login"} className="profile-button" aria-label={user ? "User profile" : "Login"} title={user ? "User profile" : "Login"}>
                    {user?.profilePicture ? (
                        <img className="profile-picture" src={user.profilePicture} alt="User profile" />
                    ) : user ? (
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-label="User">
                            <circle cx="12" cy="8" r="4" />
                            <path d="M5 21v-2a7 7 0 0 1 14 0v2" />
                        </svg>
                    ) : (
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-label="User">
                            <circle cx="12" cy="8" r="4" />
                            <path d="M5 21v-2a7 7 0 0 1 14 0v2" />
                        </svg>
                    )}
                    {user && <span className="profile-username">{user.username}</span>}
                </Link>
            </div>
        </nav>
    );
}
