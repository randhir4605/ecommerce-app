import { useState, useEffect } from "react";
import ProductCard from "../ProductCard/ProductCard";
import "./Products.css";
import { useLoaderData } from "react-router-dom";

function Products(){
    const {products, categoryName} = useLoaderData();
    const title = categoryName ? categoryName : "All Products";

    return (
        <div className="products-container">
            <h2>{title}</h2>
            <div className="products-grid">
                {products.map((item) => (
                    <ProductCard key={item.id} product={item} />
                ))}
            </div>
        </div>
    );
}

export default Products;