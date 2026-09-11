import { useState } from 'react';
import AddressForm from '../components/AddressForm';
import AddressList from '../components/AddressList';

export default function Address() {
  const [addresses, setAddresses] = useState([]);

  const handleSave = (address) => {
    setAddresses((prev) => [...prev, address]);
  };

  const handleDelete = (id) => {
    setAddresses((prev) => prev.filter((a) => a.id !== id));
  };

  return (
    <>
      <AddressForm onSave={handleSave} />
      <AddressList addresses={addresses} onDelete={handleDelete} />
    </>
  );
}
