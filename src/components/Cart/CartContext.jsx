import {createContext, useContext, useState} from "react";

const CartContext = createContext();

export function CartProvider({children}){
    const [cartItems, setCartItems] = useState([]);
    function addToCart(product,quantity){
        setCartItems((currentItems)=>{
            const existingItem = currentItems.find((item)=>item.id === product.id);
            if(existingItem){
                return currentItems.map((currentItem)=>
                    currentItem.id === product.id ? {...currentItem, quantity: Math.min(10,currentItem.quantity + quantity)} : currentItem
                );
            }else{
                return [...currentItems, {...product, quantity: quantity}];
            }
        });
    }
    function updateCart(product,quantity){
        setCartItems((currentItems)=>{
            const existingItem = currentItems.find((item)=>item.id === product.id);
            if(existingItem){
                if(existingItem.quantity===0){
                    return currentItems.filter((item)=>item.id !== existingItem.id)
                }
                return currentItems.map((currentItem)=>
                    currentItem.id === product.id ? {...currentItem, quantity: quantity} : currentItem
                );
            }else{
                return [...currentItems, {...product, quantity: quantity}];
            }
        });
    }

    function removeFromCart(product){
        setCartItems((currentItems)=>currentItems.filter((item)=>item.id !== product.id));
    }
    return (
        <CartContext.Provider value={{cartItems, addToCart, updateCart, removeFromCart}}>
            {children}
        </CartContext.Provider>
    );
}

export function useCartContext(){
    return useContext(CartContext);
}