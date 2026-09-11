function Product({ product }) {
    return (
        <div>
            <span>{product.id}</span>
            <span>{product.title}</span>
            <span>{product.category}</span>
            <span>{product.price}</span>
        </div>
    );
}

export default Product;