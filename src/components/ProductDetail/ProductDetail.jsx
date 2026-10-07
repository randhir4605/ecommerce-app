import "./ProductDetail.css";
import { useLocation } from "react-router-dom";
import { useState } from "react";
import { useCart } from "../Cart/CartContext";

const ProductDetail = () => {
  const location = useLocation();
  const product = location.state?.product;
  const [quantity, setQuantity] = useState(1);
  const { addToCart } = useCart();

  return (
        <div className="product-page">

            <h1>{product.title}</h1>

            <div className="product-main">

                {/* Images */}
                <div className="product-images">
                    {product.images.map((image) => (
                        <img
                            key={image}
                            src={image}
                            alt={product.title}
                        />
                    ))}
                </div>

                {/* Details */}
                <div className="product-info">

                    <p>{product.description}</p>

                    <h2>${product.price}</h2>

                    <p>
                        ⭐ {product.rating}
                    </p>

                    <p>
                        {product.availabilityStatus}
                    </p>

                    <div className="quantity">
                        <button onClick={()=> setQuantity(Math.max(1,quantity-1))}>-</button>
                        <span>{quantity}</span>
                        <button onClick={()=> setQuantity(Math.min(10,quantity+1))}>+</button>
                    </div>

                    <button className="cart-button" onClick={()=>addToCart(product,quantity)}>
                        Add to Cart
                    </button>

                    <p>
                        🚚 {product.shippingInformation}
                    </p>

                    <p>
                        ↩️ {product.returnPolicy}
                    </p>

                </div>
            </div>

            {/* Reviews */}
            <div className="reviews">

                <h2>Reviews</h2>

                {product.reviews.map((review, index) => (
                    <div className="review" key={index}>
                        <strong>
                            ⭐ {review.rating}
                        </strong>

                        <span>{review.comment}</span>

                        <small>
                            {review.reviewerName}
                        </small>
                    </div>
                ))}

            </div>
        </div>
    );
};

export default ProductDetail;
