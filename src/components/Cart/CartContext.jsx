import {createContext, useContext, useState} from "react";

const CartContext = createContext();

export function CartProvider({children}){
    const [cartItems, setCartItems] = useState([]);
    function addToCart(product,quantity){
        setCartItems((currentItems)=>{
            const existingItem = currentItems.find((item)=>item.id === product.id);
            if(existingItem){
                return currentItems.map((currentItem)=>
                    currentItem.id === product.id ? {...currentItem, quantity: currentItem.quantity + quantity} : currentItem
                );
            }else{
                return [...currentItems, {...product, quantity: quantity}];
            }
        });
    }
    function removeFromCart(productId){
        setCartItems((currentItems)=>currentItems.filter((item)=>item.id !== productId));
    }
    return (
        <CartContext.Provider value={{cartItems, addToCart, removeFromCart}}>
            {children}
        </CartContext.Provider>
    );
}

export function useCart(){
    return useContext(CartContext);
}