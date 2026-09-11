import { configureStore } from '@reduxjs/toolkit';
import productsReducer from './productsSlice';

/**
 * Redux Store — single source of truth for the entire app.
 *
 * State shape:
 * {
 *   products: {
 *     items: [...],   ← all products
 *     cart:  [...],   ← cart items with quantity
 *   }
 * }
 */
const store = configureStore({
  reducer: {
    products: productsReducer,
  },
});

export default store;
