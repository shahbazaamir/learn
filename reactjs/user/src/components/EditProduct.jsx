import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setProducts, updateProduct } from '../store/productsSlice';

/**
 * EditProduct
 *
 * Lists all products with inline editing for title and price.
 * Edits are dispatched to Redux — changes are reflected globally
 * (e.g. the cart in AddToCart will show updated prices).
 */
export default function EditProduct() {
  const dispatch  = useDispatch();
  const products  = useSelector(state => state.products.items);

  // Local state — tracks which product is being edited and the draft values
  const [editingId, setEditingId]   = useState(null);
  const [draft, setDraft]           = useState({ title: '', price: '' });
  const [saved, setSaved]           = useState(null); // id of last saved product

  // Fetch products if store is empty
  useEffect(() => {
    if (products.length > 0) return;
    fetch('https://dummyjson.com/products?limit=12')
      .then(res => res.json())
      .then(data => dispatch(setProducts(data.products)));
  }, []);

  const startEdit = (product) => {
    setEditingId(product.id);
    setDraft({ title: product.title, price: product.price });
    setSaved(null);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setDraft(prev => ({ ...prev, [name]: value }));
  };

  const handleSave = (id) => {
    if (!draft.title.trim() || isNaN(draft.price) || Number(draft.price) < 0) return;

    // Dispatch updateProduct action — updates global Redux state
    dispatch(updateProduct({
      id,
      changes: {
        title: draft.title.trim(),
        price: parseFloat(Number(draft.price).toFixed(2)),
      },
    }));

    setEditingId(null);
    setSaved(id);
    setTimeout(() => setSaved(null), 2000);
  };

  const handleCancel = () => {
    setEditingId(null);
    setDraft({ title: '', price: '' });
  };

  return (
    <div className="edit-product-page">
      <h2>Edit Products</h2>
      <p className="edit-product-sub">
        Changes update the global Redux store — prices reflect in the cart immediately.
      </p>

      {products.length === 0 ? (
        <p className="cart-empty">Loading products…</p>
      ) : (
        <table className="employee-table edit-product-table">
          <thead>
            <tr>
              <th>Image</th>
              <th>Title</th>
              <th>Category</th>
              <th>Price ($)</th>
              <th>Stock</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {products.map(product => (
              <tr key={product.id}>
                <td>
                  <img src={product.thumbnail} alt={product.title} className="edit-product-thumb" />
                </td>

                {/* Title — editable */}
                <td>
                  {editingId === product.id ? (
                    <input
                      className="edit-inline-input"
                      name="title"
                      value={draft.title}
                      onChange={handleChange}
                      autoFocus
                    />
                  ) : (
                    <span>{product.title}</span>
                  )}
                </td>

                <td>{product.category}</td>

                {/* Price — editable */}
                <td>
                  {editingId === product.id ? (
                    <input
                      className="edit-inline-input edit-inline-input--price"
                      name="price"
                      type="number"
                      min="0"
                      step="0.01"
                      value={draft.price}
                      onChange={handleChange}
                    />
                  ) : (
                    <span className={saved === product.id ? 'edit-saved-flash' : ''}>
                      ${product.price}
                      {saved === product.id && ' ✅'}
                    </span>
                  )}
                </td>

                <td>{product.stock}</td>

                {/* Actions */}
                <td>
                  {editingId === product.id ? (
                    <div className="edit-actions">
                      <button className="btn-primary emp-btn" onClick={() => handleSave(product.id)}>
                        Save
                      </button>
                      <button className="btn-secondary emp-btn" onClick={handleCancel}>
                        Cancel
                      </button>
                    </div>
                  ) : (
                    <button className="btn-secondary emp-btn" onClick={() => startEdit(product)}>
                      ✏️ Edit
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
