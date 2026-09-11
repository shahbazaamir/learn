import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setProducts, addToCart, removeFromCart, decrementCart, clearCart } from '../store/productsSlice';

/**
 * AddToCart
 *
 * Reads products and cart from the Redux store.
 * Dispatches actions to add/remove items and clear the cart.
 *
 * useSelector  — reads state from the store (like a getter)
 * useDispatch  — sends actions to the store (like a setter)
 */
export default function AddToCart() {
  const dispatch = useDispatch();

  // useSelector — subscribe to the slice of state we need
  const products = useSelector(state => state.products.items);
  const cart     = useSelector(state => state.products.cart);

  // Fetch products and load into Redux store on mount
  useEffect(() => {
    if (products.length > 0) return; // already loaded
    fetch('https://dummyjson.com/products?limit=12')
      .then(res => res.json())
      .then(data => dispatch(setProducts(data.products)));
  }, []);

  const cartTotal    = cart.reduce((sum, i) => sum + i.price * i.quantity, 0);
  const cartCount    = cart.reduce((sum, i) => sum + i.quantity, 0);
  const cartMap      = Object.fromEntries(cart.map(i => [i.id, i.quantity]));

  return (
    <div className="cart-page">

      {/* ── Cart Summary ── */}
      <div className="cart-summary">
        <h2>🛒 Cart ({cartCount} items)</h2>
        {cart.length === 0 ? (
          <p className="cart-empty">Your cart is empty. Add products below.</p>
        ) : (
          <>
            <ul className="cart-list">
              {cart.map(item => (
                <li key={item.id} className="cart-item">
                  <img src={item.thumbnail} alt={item.title} className="cart-thumb" />
                  <div className="cart-item-info">
                    <span className="cart-item-title">{item.title}</span>
                    <span className="cart-item-price">${item.price} × {item.quantity}</span>
                  </div>
                  <div className="cart-item-controls">
                    <button onClick={() => dispatch(decrementCart(item.id))} className="qty-btn">−</button>
                    <span className="qty-value">{item.quantity}</span>
                    <button onClick={() => dispatch(addToCart(item))} className="qty-btn">+</button>
                    <button onClick={() => dispatch(removeFromCart(item.id))} className="btn-delete">✕</button>
                  </div>
                </li>
              ))}
            </ul>
            <div className="cart-footer">
              <strong>Total: ${cartTotal.toFixed(2)}</strong>
              <button className="btn-secondary" onClick={() => dispatch(clearCart())}>
                Clear Cart
              </button>
            </div>
          </>
        )}
      </div>

      {/* ── Product Grid ── */}
      <div className="product-grid-header">
        <h2>Products</h2>
      </div>

      {products.length === 0 ? (
        <p className="cart-empty">Loading products…</p>
      ) : (
        <div className="product-grid">
          {products.map(product => (
            <div key={product.id} className="product-card">
              <img src={product.thumbnail} alt={product.title} className="product-thumb" />
              <div className="product-card-body">
                <p className="product-card-title">{product.title}</p>
                <p className="product-card-category">{product.category}</p>
                <p className="product-card-price">${product.price}</p>
              </div>
              <button
                className="btn-primary product-add-btn"
                onClick={() => dispatch(addToCart(product))}
              >
                {cartMap[product.id] ? `In Cart (${cartMap[product.id]})` : 'Add to Cart'}
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
