
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import "./Navbar.css";
import { useCartContext } from "../../context/CartContext";
import { useAuthContext } from "../../context/AuthContext";

export default function Navbar() {
    const { cartItems } = useCartContext();
    const { user } = useAuthContext();
    const cartItemCount = cartItems.reduce((total, item) => total + item.quantity, 0);
    const [query, setQuery] = useState("");
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        setQuery(new URLSearchParams(location.search).get("q") || "");
    }, [location.search]);

    const handleSearch = (event) => {
        event.preventDefault();
        const searchTerm = query.trim();

        navigate(
            searchTerm
                ? `/products?q=${encodeURIComponent(searchTerm)}`
                : "/products"
        );
    };

    return (
        <nav className="navbar">
            <Link to="/" className="brand"><img src="/icons/c-store-logo.svg"/></Link>
            <div className="nav-links"></div>
            
            <form className="nav-search" role="search" onSubmit={handleSearch}>
                <input
                    type="search"
                    placeholder="Search products..."
                    aria-label="Search products"
                    value={query}
                    onChange={(event) => setQuery(event.target.value)}
                />
                <button type="submit" aria-label="Search">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" aria-hidden="true">
                        <circle cx="11" cy="11" r="7" />
                        <path d="m16 16 4 4" />
                    </svg>
                </button>
            </form>

            <div className="nav-actions">
                <Link to="/cart" className="cart-link" aria-label="Cart">
                    <img src="/icons/shopping-cart.svg" />
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
                        <img src="/icons/user.svg"/>
                    )}
                    {user && <span className="profile-username">{user.username}</span>}
                </Link>
            </div>
        </nav>
    );
}
