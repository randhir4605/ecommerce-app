import "./Home.css";
import { useEffect, useRef, useState } from "react";
import {getProducts, getProductCategories, testBackend} from "../../api/products";
import { Link } from "react-router-dom";
import ProductCard from "../../components/ProductCard/ProductCard";

const Home = () => {
  const [homeData,setHomeData] = useState("");
  const [products, setProducts] = useState([]);
  const [productCategories, setProductCategories] = useState([]);
  const featuredGridRef = useRef(null);
  const [featuredVisibleCount, setFeaturedVisibleCount] = useState(2);

  useEffect(()=>{
    //testBackend().then((data)=>setHomeData(data));
    getProducts().then((data)=>setProducts(data.products));
    getProductCategories().then((data)=>setProductCategories(data));
  },[]);

  /* determine the size of container and calculate how much product cart can be fit */
  useEffect(() => {
    const grid = featuredGridRef.current;
    if (!grid) return;

    const updateCount = () => {
        let count;

        if (window.matchMedia("(max-width: 47.999rem)").matches) {
            count = 2;
        } else if (window.matchMedia("(max-width: 67.999rem)").matches) {
            count = 4;
        } else {
            const width = grid.clientWidth;
            const styles = getComputedStyle(grid);
            const gap = parseFloat(styles.columnGap) || 0;
            const rootSize = parseFloat(getComputedStyle(document.documentElement).fontSize) || 16;
            const minCardWidth = 15 * rootSize;
            count = Math.max(1, Math.floor((width + gap) / (minCardWidth + gap)));
        }
        setFeaturedVisibleCount(previous => previous === count ? previous : count);
    };

    const observer = new ResizeObserver(updateCount);
    observer.observe(grid);
    window.addEventListener("resize", updateCount);
    updateCount();

    return () => {
        observer.disconnect();
        window.removeEventListener("resize", updateCount);
    };
}, []);
  
// we can't use categories from dummyjson because icon is not available. When you will create in own database then you can create proper table with required details.
  const categories = [
    { id: 1, name: "Electronics", slug: "electronics", icon: "laptop.svg", path: "/laptops" },
    { id: 2, name: "Mobiles", slug: "mobiles", icon: "smartphone.svg", path: "/smartphones" },
    { id: 3, name: "Fashion", slug: "fashion", icon: "shirt.svg", path: "/mens-shirts" },
    { id: 4, name: "Home & Living", slug: "home", icon: "home.svg", path: "/home-decoration" },
    { id: 5, name: "Beauty", slug: "beauty", icon: "beauty.svg", path: "/beauty" },
    { id: 6, name: "Sports", slug: "sports", icon: "sports.svg" , path: "/sports-accessories" },
    { id: 7, name: "Toys", slug: "toys", icon: "toys.svg", path: "" },
    { id: 8, name: "Books", slug: "books", icon: "book-open.svg", path: "" }
  ];


  return (
    <main className="home-page-layout">
      
        <section className="hero-banner">

        </section>
        <section className="category-section">
          { /* <div className="section-heading"><h2>Shop By Category</h2></div> */ }
          <div className="category-grid">
              {categories.map(item => (
                <Link to={`/products/category${item.path}`}>
                    <div className="category-card" key={item.id}>
                        <div className={`category-icon category-icon--${item.slug}`}>  {/* to provide sepearate class names */}
                            <img src={`/icons/${item.icon}`} alt="" />
                        </div>
                        <span>{item.name}</span>
                    </div>
                  </Link>
              ))}
          </div>
        </section>

        {/* Featured Products */}
        <section className="featured-section">
            <div className="section-heading">
                <h2>Featured Products</h2>
                <Link to="/products">View All</Link>
            </div>

            <div className="products-grid featured-preview-grid" ref={featuredGridRef}>
              {products.slice(0, featuredVisibleCount).map(product => (
                <ProductCard key={product.id} product={product} />
              ))}
            </div>
        </section>
        
        {/* Special Offer */}
        <section className="promotion-section">
            <div className="promotion-content">
                <p className="promotion-label">LIMITED TIME OFFER</p>
                <h2>Up to 50% Off</h2>
                <p>On selected items</p>
                <Link to="/products" className="promotion-button">
                    Shop Deals <span aria-hidden="true">→</span>
                </Link>
            </div>
        </section>

        <section className="best-selling-section">
          <h2>Best Sellers</h2>
          <div className="best-selling-grid">

          </div>
        </section>
  
    </main>
  )
};

export default Home;
