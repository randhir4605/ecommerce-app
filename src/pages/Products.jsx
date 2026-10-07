import { useState, useEffect } from "react";
import { getProducts } from "../api/products";
import ProductCard from "../components/ProductCard/ProductCard";

function Products(){
    const [products, setProducts] = useState([]);

    useEffect(()=>{
        getProducts().then((data)=>{
            setProducts(data.products);
        });
    },[]);
    return (
        <div style={style}>
            {products.map((item) => (
                <ProductCard key={item.id} product={item} />
            ))}
        </div>
    );
}

const style = {
    display: "grid",
    gridTemplateColumns: "repeat(4, 1fr)",
    gap: "20px",
    padding: "20px"
};

export default Products;