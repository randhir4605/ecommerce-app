import { useState, useEffect } from "react";
import { getProducts } from "../../api/products";
import ProductCard from "../ProductCard/ProductCard";
import "./Products.css";

function Products(){
    const [products, setProducts] = useState([]);

    useEffect(()=>{
        getProducts().then((data)=>{
            setProducts(data.products);
        });
    },[]);
    return (
        <div className="products-container">
            <div className="products-grid">
                {products.map((item) => (
                    <ProductCard key={item.id} product={item} />
                ))}
            </div>
        </div>
    );
}

export default Products;