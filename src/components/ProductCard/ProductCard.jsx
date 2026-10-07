import { Link } from "react-router-dom";
import './ProductCard.css';
import { useCart } from "../Cart/CartContext";

function ProductCard({ product }) {
    const carouselId = `productCarousel-${product.id}`;
    const {addToCart}=useCart();
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

                <div className="price-and-action">
                    <p className="product-card-price"> ${product.price.toFixed(2)} </p>
                    <div className="product-card-action" onClick={()=>{
                        addToCart(product,1);
                    }}> Add To Cart </div>
                </div>
            </div>

        </div>
    );
}


export default ProductCard;
