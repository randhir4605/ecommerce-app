import { useEffect, useState } from "react";
import {testBackend} from "../api/products";

const Home = () => {
  const [homeData,setHomeData] = useState("");
  useEffect(()=>{
    testBackend().then((data)=>setHomeData(data));
  },[]);
  
  return <h4>This is a learning app created in react js. Message from backend - {homeData}</h4>;
};

export default Home;
