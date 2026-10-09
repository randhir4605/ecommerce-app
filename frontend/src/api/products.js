export const getProducts = async ()=>{
    const response = await fetch("https://dummyjson.com/products");
    const data = await response.json();
    return data;
};

export const testBackend = async ()=>{
    const response = await fetch(`${process.env.REACT_APP_API_URL}`);
    const data = await response.text();
    return data;
};
