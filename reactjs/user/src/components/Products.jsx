import {useState,useEffect} from "react"
import Product from "./Product"

function Products (){

    const [products ,setProducts] = useState([]);

    useEffect(() => {
        fetch("https://dummyjson.com/products")
        .then(res => res.json())             // Bug 2 fixed: json() not json
        .then(data => setProducts(data.products));
    },[]);

    return (                                 // Bug 1 fixed: () not {}
        <div>
        <div>Products List</div>
        <div>
            {products.map(product => (
                <Product key={product.id} product={product} />  // Bug 3 fixed: pass props
            ))}
        </div>
        </div>
    );
}

export default Products;