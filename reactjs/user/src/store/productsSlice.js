import { createSlice } from '@reduxjs/toolkit';

/**
 * Redux Slice — manages two pieces of global state:
 *
 *  products  []   — list of all products (fetched from API, editable)
 *  cart      []   — items added to cart, each with a quantity
 *
 * A "slice" combines:
 *  - initial state
 *  - reducer functions (how state changes)
 *  - auto-generated action creators
 */
const productsSlice = createSlice({
  name: 'products',
  initialState: {
    items: [],       // all products from API
    cart: [],        // { id, title, price, thumbnail, quantity }
  },
  reducers: {

    // Called once after API fetch — loads all products into store
    setProducts(state, action) {
      state.items = action.payload;
    },

    // Edit a product's price or title in the global store
    updateProduct(state, action) {
      const { id, changes } = action.payload;
      const product = state.items.find(p => p.id === id);
      if (product) Object.assign(product, changes);
    },

    // Add a product to cart (or increment quantity if already there)
    addToCart(state, action) {
      const product = action.payload;
      const existing = state.cart.find(item => item.id === product.id);
      if (existing) {
        existing.quantity += 1;
      } else {
        state.cart.push({ ...product, quantity: 1 });
      }
    },

    // Remove a single item from cart
    removeFromCart(state, action) {
      state.cart = state.cart.filter(item => item.id !== action.payload);
    },

    // Decrease quantity — remove if it hits 0
    decrementCart(state, action) {
      const item = state.cart.find(i => i.id === action.payload);
      if (item) {
        item.quantity -= 1;
        if (item.quantity === 0) {
          state.cart = state.cart.filter(i => i.id !== action.payload);
        }
      }
    },

    // Clear entire cart
    clearCart(state) {
      state.cart = [];
    },
  },
});

export const {
  setProducts,
  updateProduct,
  addToCart,
  removeFromCart,
  decrementCart,
  clearCart,
} = productsSlice.actions;

export default productsSlice.reducer;
