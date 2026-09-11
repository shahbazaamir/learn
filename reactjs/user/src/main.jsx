import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import store from './store/store.js';
import { EmployeesProvider } from './context/EmployeesContext.jsx';
import Home from './Home.jsx';
import HomePage from './pages/HomePage.jsx';
import Address from './pages/Address.jsx';
import Employee from './pages/Employee.jsx';
import EmployeeDetail from './pages/EmployeeDetail.jsx';
import Otp from './pages/Otp.jsx';
import Products from './components/Products.jsx';
import AddToCart from './components/AddToCart.jsx';
import EditProduct from './components/EditProduct.jsx';
import ProductSearchPage from './pages/ProductSearchPage.jsx';
import ProductActionsPage from './pages/ProductActionsPage.jsx';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    {/* Provider makes the Redux store available to every component */}
    <Provider store={store}>
      <BrowserRouter>
        <EmployeesProvider>
          <Routes>
            <Route path="/" element={<Home />}>
              <Route index element={<HomePage />} />
              <Route path="address" element={<Address />} />
              <Route path="employee" element={<Employee />} />
              <Route path="employee/:id" element={<EmployeeDetail />} />
              <Route path="otp" element={<Otp />} />
              <Route path="cart" element={<AddToCart />} />
              <Route path="products" element={<Products />} />
              <Route path="edit-products" element={<EditProduct />} />
              <Route path="product-search" element={<ProductSearchPage />} />
              <Route path="product-actions" element={<ProductActionsPage />} />
            </Route>
          </Routes>
        </EmployeesProvider>
      </BrowserRouter>
    </Provider>
  </StrictMode>
);
