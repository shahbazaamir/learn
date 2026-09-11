import { useState, useMemo, useEffect } from 'react';

/**
 * ProductSearch — demonstrates useMemo
 *
 * PROBLEM without useMemo:
 * Every time the user types in the search box, React re-renders this component.
 * On each render, the filtering + sorting runs again over ALL products — even
 * if the search term didn't change (e.g. the re-render was caused by something
 * else like a theme toggle or unrelated state update).
 *
 * useMemo(() => expensiveCalc(), [deps])
 * ─────────────────────────────────────
 * Memoizes (caches) the result of a function.
 * Re-runs ONLY when the listed dependencies change.
 * Returns the cached result on every other render.
 *
 * Three useMemo usages here:
 *  1. filteredProducts  — filter + sort across 100 products
 *  2. stats             — compute min/max/avg price from filtered list
 *  3. categoryOptions   — deduplicate category list from all products
 */

// ── Simulate an expensive computation ────────────────────────────────────────
// In production this would be a large dataset. We fake "expense" with a
// label so you can observe in React DevTools Profiler that it runs only
// when dependencies change.
function filterAndSort(products, search, category, sortBy) {
  console.log('%c[useMemo] Running filter+sort', 'color: #4a4aff; font-weight: bold');

  let result = [...products];

  if (category) {
    result = result.filter(p => p.category === category);
  }

  if (search.trim()) {
    const q = search.toLowerCase();
    result = result.filter(
      p => p.title.toLowerCase().includes(q) ||
           p.description?.toLowerCase().includes(q)
    );
  }

  switch (sortBy) {
    case 'price-asc':  result.sort((a, b) => a.price - b.price);  break;
    case 'price-desc': result.sort((a, b) => b.price - a.price);  break;
    case 'rating':     result.sort((a, b) => b.rating - a.rating); break;
    case 'name':       result.sort((a, b) => a.title.localeCompare(b.title)); break;
    default: break;
  }

  return result;
}

function computeStats(products) {
  console.log('%c[useMemo] Computing stats', 'color: green; font-weight: bold');
  if (!products.length) return null;
  const prices = products.map(p => p.price);
  return {
    count: products.length,
    min:   Math.min(...prices).toFixed(2),
    max:   Math.max(...prices).toFixed(2),
    avg:   (prices.reduce((a, b) => a + b, 0) / prices.length).toFixed(2),
  };
}

// ── Component ─────────────────────────────────────────────────────────────────

export default function ProductSearch() {
  const [allProducts, setAllProducts] = useState([]);
  const [search,      setSearch]      = useState('');
  const [category,    setCategory]    = useState('');
  const [sortBy,      setSortBy]      = useState('');
  // Unrelated state — used to prove useMemo doesn't re-run for unrelated renders
  const [darkMode,    setDarkMode]    = useState(false);

  useEffect(() => {
    fetch('https://dummyjson.com/products?limit=100')
      .then(r => r.json())
      .then(d => setAllProducts(d.products));
  }, []);

  // ── useMemo 1: filter + sort ──────────────────────────────────────────────
  // Only re-runs when search, category, sortBy, or allProducts changes.
  // Toggling darkMode does NOT re-run this — open console to verify.
  const filteredProducts = useMemo(
    () => filterAndSort(allProducts, search, category, sortBy),
    [allProducts, search, category, sortBy]
  );

  // ── useMemo 2: derived stats from filtered list ───────────────────────────
  // Depends on filteredProducts — re-runs only when filtered list changes.
  const stats = useMemo(
    () => computeStats(filteredProducts),
    [filteredProducts]
  );

  // ── useMemo 3: category dropdown options ──────────────────────────────────
  // Derived from allProducts — only recomputes when allProducts loads/changes.
  const categoryOptions = useMemo(
    () => [...new Set(allProducts.map(p => p.category))].sort(),
    [allProducts]
  );

  return (
    <div className={`ps-wrapper ${darkMode ? 'ps-dark' : ''}`}>

      <div className="ps-header">
        <h2>🔍 Product Search</h2>
        <div className="ps-header-right">
          {/* Toggling this re-renders the component but does NOT re-run
              the filter/sort — open browser console to verify */}
          <button
            className="btn-secondary emp-btn"
            onClick={() => setDarkMode(d => !d)}
          >
            {darkMode ? '☀️ Light' : '🌙 Dark'} Mode
          </button>
        </div>
      </div>

      <p className="ps-note">
        Open browser console — <code>[useMemo] Running filter+sort</code> only
        logs when search/category/sort changes, NOT when Dark Mode is toggled.
      </p>

      {/* Filters */}
      <div className="ps-filters">
        <div className="field">
          <label htmlFor="ps-search">Search</label>
          <input
            id="ps-search"
            type="text"
            value={search}
            onChange={e => setSearch(e.target.value)}
            placeholder="Title or description…"
          />
        </div>

        <div className="field">
          <label htmlFor="ps-category">Category</label>
          <select
            id="ps-category"
            value={category}
            onChange={e => setCategory(e.target.value)}
          >
            <option value="">All categories</option>
            {categoryOptions.map(c => (
              <option key={c} value={c}>{c}</option>
            ))}
          </select>
        </div>

        <div className="field">
          <label htmlFor="ps-sort">Sort by</label>
          <select
            id="ps-sort"
            value={sortBy}
            onChange={e => setSortBy(e.target.value)}
          >
            <option value="">Default</option>
            <option value="price-asc">Price: Low → High</option>
            <option value="price-desc">Price: High → Low</option>
            <option value="rating">Rating</option>
            <option value="name">Name A–Z</option>
          </select>
        </div>
      </div>

      {/* Stats — computed from memoized filtered list */}
      {stats && (
        <div className="ps-stats">
          <span>Results: <strong>{stats.count}</strong></span>
          <span>Min: <strong>${stats.min}</strong></span>
          <span>Max: <strong>${stats.max}</strong></span>
          <span>Avg: <strong>${stats.avg}</strong></span>
        </div>
      )}

      {/* Results */}
      <div className="product-grid">
        {filteredProducts.map(p => (
          <div key={p.id} className="product-card">
            <img src={p.thumbnail} alt={p.title} className="product-thumb" />
            <div className="product-card-body">
              <p className="product-card-title">{p.title}</p>
              <p className="product-card-category">{p.category}</p>
              <div className="ps-card-row">
                <span className="product-card-price">${p.price}</span>
                <span className="ps-rating">⭐ {p.rating}</span>
              </div>
            </div>
          </div>
        ))}
      </div>

      {filteredProducts.length === 0 && allProducts.length > 0 && (
        <p className="cart-empty">No products match your filters.</p>
      )}

      {/* Explanation */}
      <div className="otp-explanation" style={{ marginTop: '1.5rem' }}>
        <h3>useMemo in this component</h3>
        <div className="otp-ref-item">
          <code>filteredProducts</code>
          <span>Filters + sorts 100 products. Re-runs only when <code>search</code>, <code>category</code>, <code>sortBy</code>, or <code>allProducts</code> changes. Toggling Dark Mode re-renders the component but the cached result is returned immediately.</span>
        </div>
        <div className="otp-ref-item">
          <code>stats</code>
          <span>min / max / avg price computed from <code>filteredProducts</code>. Re-runs only when the filtered list changes — not on every render.</span>
        </div>
        <div className="otp-ref-item">
          <code>categoryOptions</code>
          <span>Deduplicates categories from all 100 products. Re-runs only once when <code>allProducts</code> loads.</span>
        </div>
        <div className="otp-rule">
          <strong>Rule:</strong> Use <code>useMemo</code> when a calculation is expensive AND its inputs haven't changed. Don't use it for simple operations — the memoization overhead isn't worth it.
        </div>
      </div>
    </div>
  );
}
