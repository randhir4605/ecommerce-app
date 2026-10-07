import { Link } from "react-router-dom";
import './ProductCard.css';
import { useCartContext } from "../Cart/CartContext";

function ProductCard({ product }) {
    const carouselId = `productCarousel-${product.id}`;
    const {cartItems,addToCart,updateCart, removeFromCart}=useCartContext();
    const existingCartItem = cartItems.find((item) => item.id === product.id);
    return (
        <div className="product-card">
            
            {/* Product Images */}
            <div
                id={carouselId}
                className="carousel slide"
                data-bs-touch="true"
            >
                <div className="carousel-inner">
                    {product.images?.map((url, index) => (
                        <div
                            className={`carousel-item ${index === 0 ? "active" : ""}`}
                            key={url}
                        >
                            <Link to={`/product/${product.id}`} state={{ product }} >
                                <img src={url} alt={`${product.title} ${index + 1}`} className="product-image" />
                            </Link>
                        </div>
                    ))}
                </div>

                {/* Previous Button */}
                {product.images?.length > 1 && (
                    <>
                        <button
                            className="carousel-control-prev"
                            type="button"
                            data-bs-target={`#${carouselId}`}
                            data-bs-slide="prev"
                            aria-label="Previous image"
                        >
                            <span className="carousel-control-prev-icon" aria-hidden="true" />
                        </button>

                        <button
                            className="carousel-control-next"
                            type="button"
                            data-bs-target={`#${carouselId}`}
                            data-bs-slide="next"
                            aria-label="Next image"
                        >
                            <span className="carousel-control-next-icon" aria-hidden="true" />
                        </button>
                    </>
                )}
            </div>

            {/* Product Details */}

            <div className="product-card-body">
                <Link to={`/product/${product.id}`} state={{ product }} className="product-card-details">
                    <h5 className="product-card-title"> {product.title} </h5>
                </Link>

                <div className="price-and-action" >
                    <div className="product-card-price"> ${product.price.toFixed(2)} </div>
                    {existingCartItem ?(
                    
                        <div className="cart-controls">
                        <div className="quantity-buttons">
                            <button onClick={()=>{updateCart(product, Math.max(1,existingCartItem.quantity - 1))}}>-</button>
                            <span>{existingCartItem.quantity}</span>
                            <button onClick={()=>{updateCart(product, Math.min(10,existingCartItem.quantity + 1))}}>+</button>
                        </div>
                        <button className="cart-action remove-action" onClick={() => removeFromCart(product, 0)}>Remove</button>
                    </div>
                    ):(
                    
                        <button className="cart-action add-action" onClick={()=>{
                            addToCart(product,1);
                        }}>Add To Cart </button>
                    
                    )}
                </div>
            </div>

        </div>
    );
}


export default ProductCard;
