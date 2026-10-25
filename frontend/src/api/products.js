export const getProducts = async ()=>{
    const response = await fetch("https://dummyjson.com/products");
    const data = await response.json();
    return data;
};

export const getProductCategories = async () => {
    const response = await fetch("https://dummyjson.com/products/categories");
    const data = await response.json();
    return data;
}

export const getProductsByCategory = async (name) => {
    const response = await fetch(`https://dummyjson.com/products/category/${name}`);
    const data = await response.json();
    return data;
}

export const searchProducts = async (query) => {
    const response = await fetch(`https://dummyjson.com/products/search?q=${encodeURIComponent(query)}`);

    if (!response.ok) {
        throw new Error("Failed to search products");
    }

    return response.json();
};

export const testBackend = async ()=>{
    const response = await fetch(`${process.env.REACT_APP_API_URL}`);
    const data = await response.text();
    return data;
};
