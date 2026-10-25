
import { Link } from "react-router-dom";
import "./Footer.css";

const Footer = () => {
    const year = new Date().getFullYear();

    return (
        <footer className="site-footer">
            <div className="site-footer__inner">
                <div className="site-footer__top">
                    <Link to="/" className="site-footer__brand">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
                            <circle cx="9" cy="21" r="1" />
                            <circle cx="20" cy="21" r="1" />
                            <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6" />
                        </svg>
                        <span>C-Store</span>
                    </Link>

                    <nav className="site-footer__links" aria-label="Footer navigation">
                        <Link to="/about">About</Link>
                        <Link to="/contact">Contact</Link>
                        <Link to="/shipping">Shipping</Link>
                        <Link to="/returns">Returns</Link>
                        <Link to="/privacy">Privacy</Link>
                    </nav>
                </div>

                <div className="site-footer__bottom">
                    <p>© {year} C-Store. All rights reserved.</p>

                    <div className="site-footer__socials" aria-label="Social media">
                        <a href="https://www.instagram.com/" aria-label="Instagram" target="_blank" rel="noreferrer">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
                                <rect x="3" y="3" width="18" height="18" rx="5" />
                                <circle cx="12" cy="12" r="4" />
                                <circle cx="18" cy="6" r="0.5" fill="currentColor" />
                            </svg>
                        </a>
                        <a href="https://www.facebook.com/" aria-label="Facebook" target="_blank" rel="noreferrer">
                            <svg viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
                                <path d="M13.5 21v-8h2.7l.4-3h-3.1V8.1c0-.9.3-1.5 1.6-1.5h1.7V3.9c-.3 0-1.3-.1-2.5-.1-2.5 0-4.2 1.5-4.2 4.3V10H7.3v3h2.8v8h3.4Z" />
                            </svg>
                        </a>
                        <a href="https://www.youtube.com/" aria-label="YouTube" target="_blank" rel="noreferrer">
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
                                <rect x="2" y="4" width="20" height="16" rx="5" />
                                <path d="m10 9 5 3-5 3z" fill="currentColor" stroke="none" />
                            </svg>
                        </a>
                    </div>
                </div>
            </div>
        </footer>
    );
};

export default Footer;
