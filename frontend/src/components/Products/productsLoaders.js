import {getProducts, getProductsByCategory, searchProducts} from "../../api/products";

/* this loader will work for all products as well as search result */
export async function productsLoader({ request }) {
    const url = new URL(request.url);
    const searchTerm = url.searchParams.get("q")?.trim() || "";

    const data = searchTerm
        ? await searchProducts(searchTerm)
        : await getProducts();

    return {
        products: data.products,
        categoryName: null,
        searchTerm
    };
}

export async function categoryProductsLoader({ params }) {
    const data = await getProductsByCategory(params.name);
    const products = data.products;
    return {
        products,
        categoryName: params.name
    };
}