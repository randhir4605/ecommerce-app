import "./ProductCard.css";
import { Link } from "react-router-dom";

function ProductCard({ product }) {
    console.log(product);

    return (
        <div>
            <div className="product-card">
                <Link to={`/product/${product.id}`} state={{ product }}>
                    <div className="product-images">
                        {product.images.map((url) => (
                            <img src={url} alt={product.name} />
                        ))}
                    </div>
                    <div>{product.title}</div>
                </Link>
            </div>
        </div>
    );
}

export default ProductCard;

