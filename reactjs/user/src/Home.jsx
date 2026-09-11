import { Link, Outlet, useLocation } from 'react-router-dom';
import './App.css';

export default function Home() {
  const { pathname } = useLocation();

  return (
    <div className="app">
      <header>
        <h1>  App</h1>
        <nav className="nav">
          <Link to="/" className={pathname === '/' ? 'nav-link active' : 'nav-link'}>
             Home
          </Link>
          <Link to="/address" className={pathname.startsWith('/address') ? 'nav-link active' : 'nav-link'}>
             Address
          </Link>
          <Link to="/employee" className={pathname.startsWith('/employee') ? 'nav-link active' : 'nav-link'}>
              Employee
          </Link>
          <Link to="/otp" className={pathname.startsWith('/otp') ? 'nav-link active' : 'nav-link'}>
            🔐 OTP
          </Link>
          <Link to="/products" className={pathname.startsWith('/products') ? 'nav-link active' : 'nav-link'}>
            📦 Products
          </Link>
          <Link to="/cart" className={pathname.startsWith('/cart') ? 'nav-link active' : 'nav-link'}>
            🛒 Cart
          </Link>
          <Link to="/edit-products" className={pathname.startsWith('/edit-products') ? 'nav-link active' : 'nav-link'}>
            ✏️ Edit Products
          </Link>
          <Link to="/product-search" className={pathname.startsWith('/product-search') ? 'nav-link active' : 'nav-link'}>
            🔍 useMemo
          </Link>
          <Link to="/product-actions" className={pathname.startsWith('/product-actions') ? 'nav-link active' : 'nav-link'}>
            ⚡ useCallback
          </Link>
        </nav>
      </header>

      <main>
        <Outlet />
      </main>
    </div>
  );
}
