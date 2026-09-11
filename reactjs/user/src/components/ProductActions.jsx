import { useState, useCallback, useEffect, memo } from 'react';

/**
 * ProductActions — demonstrates useCallback
 *
 * PROBLEM without useCallback:
 * Every render creates a NEW function reference for handlers like
 * handleAddToCart, handleWishlist etc. When these are passed as props
 * to child components, the children see "new props" on every parent
 * render — even though the function logic hasn't changed — so they
 * re-render unnecessarily.
 *
 * useCallback(() => fn, [deps])
 * ─────────────────────────────
 * Returns the SAME function reference between renders unless deps change.
 * Combined with React.memo() on child components, this prevents children
 * from re-rendering when the parent re-renders for unrelated reasons.
 *
 * Three useCallback usages here:
 *  1. handleAddToCart   — stable add-to-cart passed to each ProductRow
 *  2. handleWishlist    — stable wishlist toggle passed to each ProductRow
 *  3. handleSelectAll   — stable bulk-select callback passed to toolbar
 */

// ── Child component — wrapped in React.memo ───────────────────────────────────
// memo() means: only re-render if props actually changed.
// Without useCallback in the parent, onAdd and onWishlist would be new
// function references every render → memo() wouldn't help at all.
const ProductRow = memo(function ProductRow({ product, inCart, inWishlist, onAdd, onWishlist }) {
  console.log(`%c[memo] ProductRow rendered: ${product.title}`, 'color: #e65100');
  return (
    <tr>
      <td>
        <img src={product.thumbnail} alt={product.title} className="edit-product-thumb" />
      </td>
      <td>
        <span className="product-card-title">{product.title}</span><br />
        <span style={{ fontSize: '0.75rem', color: '#888' }}>{product.category}</span>
      </td>
      <td>${product.price}</td>
      <td>⭐ {product.rating}</td>
      <td>
        {/* onAdd is stable (useCallback) — ProductRow only re-renders if
            inCart, inWishlist, or the product itself changes */}
        <button
          className={`btn-primary emp-btn ${inCart ? 'pa-in-cart' : ''}`}
          onClick={() => onAdd(product)}
        >
          {inCart ? '✅ Added' : '+ Cart'}
        </button>
      </td>
      <td>
        <button
          className={`btn-secondary emp-btn ${inWishlist ? 'pa-wishlisted' : ''}`}
          onClick={() => onWishlist(product.id)}
        >
          {inWishlist ? '❤️' : '🤍'}
        </button>
      </td>
    </tr>
  );
});

// ── Toolbar — also wrapped in memo ────────────────────────────────────────────
const CartToolbar = memo(function CartToolbar({ count, onSelectAll, onClearCart }) {
  console.log('%c[memo] CartToolbar rendered', 'color: purple');
  return (
    <div className="pa-toolbar">
      <span>🛒 Cart: <strong>{count}</strong> items</span>
      <button className="btn-secondary emp-btn" onClick={onSelectAll}>Add All</button>
      <button className="btn-delete" style={{ borderRadius: 6, padding: '0.3rem 0.7rem' }} onClick={onClearCart}>Clear</button>
    </div>
  );
});

// ── Parent component ───────────────────────────────────────────────────────────

export default function ProductActions() {
  const [products,  setProducts]  = useState([]);
  const [cart,      setCart]      = useState(new Set());    // Set of product ids
  const [wishlist,  setWishlist]  = useState(new Set());   // Set of product ids
  // Unrelated state — toggling this re-renders the parent but should NOT
  // re-render children because callbacks are stable via useCallback
  const [showNote,  setShowNote]  = useState(true);

  useEffect(() => {
    fetch('https://dummyjson.com/products?limit=10')
      .then(r => r.json())
      .then(d => setProducts(d.products));
  }, []);

  // ── useCallback 1: add to cart ─────────────────────────────────────────────
  // setCart uses the functional updater form — so 'cart' is NOT a dependency.
  // This means the same function reference is returned on every render.
  // Without useCallback, every render would give each ProductRow a new onAdd
  // function → memo() would be useless → all 10 rows re-render together.
  const handleAddToCart = useCallback((product) => {
    setCart(prev => {
      const next = new Set(prev);
      next.add(product.id);
      return next;
    });
  }, []); // ← empty deps: created once, reused forever

  // ── useCallback 2: toggle wishlist ────────────────────────────────────────
  const handleWishlist = useCallback((productId) => {
    setWishlist(prev => {
      const next = new Set(prev);
      next.has(productId) ? next.delete(productId) : next.add(productId);
      return next;
    });
  }, []); // ← empty deps: same logic

  // ── useCallback 3: add all to cart ────────────────────────────────────────
  // products IS a dependency here — it uses the current products list.
  // The function is recreated only when products loads/changes.
  const handleSelectAll = useCallback(() => {
    setCart(new Set(products.map(p => p.id)));
  }, [products]);

  const handleClearCart = useCallback(() => {
    setCart(new Set());
  }, []);

  return (
    <div className="pa-wrapper">
      <div className="ps-header">
        <h2>⚡ Product Actions</h2>
        <button
          className="btn-secondary emp-btn"
          onClick={() => setShowNote(n => !n)}
        >
          Toggle Note
        </button>
      </div>

      {/* Toggle this — open console and notice ProductRow does NOT re-render */}
      {showNote && (
        <p className="ps-note">
          Open browser console — toggling this note re-renders the parent, but
          <code> [memo] ProductRow rendered</code> does NOT log because
          <code> onAdd</code> and <code> onWishlist</code> are stable via <code>useCallback</code>.
        </p>
      )}

      <CartToolbar
        count={cart.size}
        onSelectAll={handleSelectAll}
        onClearCart={handleClearCart}
      />

      {products.length === 0 ? (
        <p className="cart-empty">Loading…</p>
      ) : (
        <table className="employee-table" style={{ marginTop: '1rem' }}>
          <thead>
            <tr>
              <th>Image</th>
              <th>Product</th>
              <th>Price</th>
              <th>Rating</th>
              <th>Cart</th>
              <th>Wishlist</th>
            </tr>
          </thead>
          <tbody>
            {products.map(product => (
              <ProductRow
                key={product.id}
                product={product}
                inCart={cart.has(product.id)}
                inWishlist={wishlist.has(product.id)}
                onAdd={handleAddToCart}       // stable reference ✅
                onWishlist={handleWishlist}   // stable reference ✅
              />
            ))}
          </tbody>
        </table>
      )}

      {/* Explanation */}
      <div className="otp-explanation" style={{ marginTop: '1.5rem' }}>
        <h3>useCallback in this component</h3>
        <div className="otp-ref-item">
          <code>handleAddToCart</code>
          <span>Deps: <code>[]</code> — created once. Uses functional <code>setCart(prev =&gt; ...)</code> so it doesn't need <code>cart</code> as a dependency. Passed to every <code>ProductRow</code> — same reference every render.</span>
        </div>
        <div className="otp-ref-item">
          <code>handleWishlist</code>
          <span>Deps: <code>[]</code> — same pattern. Toggle logic uses <code>prev</code> set so no external dependency needed.</span>
        </div>
        <div className="otp-ref-item">
          <code>handleSelectAll</code>
          <span>Deps: <code>[products]</code> — must read the products array, so it re-creates when products loads. Passed to <code>CartToolbar</code>.</span>
        </div>
        <div className="otp-rule">
          <strong>Rule:</strong> Use <code>useCallback</code> when passing a function as a prop to a <code>memo()</code>-wrapped child. Without <code>memo()</code> on the child, <code>useCallback</code> alone has no effect — the child re-renders regardless.
          <br /><br />
          <strong>useMemo vs useCallback:</strong><br />
          <code>useMemo(() =&gt; value, deps)</code> — memoizes a <em>computed value</em>.<br />
          <code>useCallback(() =&gt; fn, deps)</code> — memoizes a <em>function reference</em>.<br />
          <code>useCallback(fn, deps)</code> is equivalent to <code>useMemo(() =&gt; fn, deps)</code>.
        </div>
      </div>
    </div>
  );
}
