import { useState } from 'react';
import AddressForm from './components/AddressForm';
import AddressList from './components/AddressList';
import './App.css';

export default function App() {
  const [addresses, setAddresses] = useState([]);

  const handleSave = (address) => {
    setAddresses((prev) => [...prev, address]);
  };

  const handleDelete = (id) => {
    setAddresses((prev) => prev.filter((a) => a.id !== id));
  };

  return (
    <div className="app">
      <header>
        <h1>Address Book</h1>
        <p>Store and manage your addresses</p>
      </header>

      <main>
        <AddressForm onSave={handleSave} />
        <AddressList addresses={addresses} onDelete={handleDelete} />
      </main>
    </div>
  );
}
