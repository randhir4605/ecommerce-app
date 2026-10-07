import "./ProductCard.css";

function ProductCard({ product }) {
    console.log(product);
    return (
        <div>
            <div className="product-card">
                <div className="product-images">
                    {product.images.map((url) => (
                        <img src={url} alt={product.name} />
                    ))}
                </div>
                <div>{product.title}</div>
            </div>
        </div>
    );
}

export default ProductCard;