import { useState, useEffect } from 'react';

const INITIAL_FORM = {
  name: '',
  street: '',
  country: '',
  city: '',
  zip: '',
};

export default function AddressForm({ onSave }) {
  const [countries, setCountries] = useState([]);
  const [cities, setCities] = useState([]);
  const [form, setForm] = useState(INITIAL_FORM);
  const [loadingCountries, setLoadingCountries] = useState(true);
  const [loadingCities, setLoadingCities] = useState(false);
  const [error, setError] = useState('');

  // Fetch country list on mount
  useEffect(() => {
    fetch('https://countriesnow.space/api/v0.1/countries')
      .then((res) => res.json())
      .then((json) => {
        if (!json.error) {
          setCountries(json.data.map((c) => c.country).sort());
        } else {
          setError('Failed to load countries.');
        }
      })
      .catch(() => setError('Failed to load countries.'))
      .finally(() => setLoadingCountries(false));
  }, []);

  // Fetch cities whenever country changes
  useEffect(() => {
    if (!form.country) {
      setCities([]);
      return;
    }
    setLoadingCities(true);
    setForm((prev) => ({ ...prev, city: '' }));
    fetch('https://countriesnow.space/api/v0.1/countries/cities', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ country: form.country }),
      redirect: 'follow',
    })
      .then((res) => res.json())
      .then((json) => {
        if (!json.error) {
          setCities(json.data.sort());
        } else {
          setCities([]);
        }
      })
      .catch(() => setCities([]))
      .finally(() => setLoadingCities(false));
  }, [form.country]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.name || !form.street || !form.country || !form.city) {
      setError('Please fill in all required fields.');
      return;
    }
    onSave({ ...form, id: Date.now() });
    setForm(INITIAL_FORM);
    setCities([]);
    setError('');
  };

  return (
    <form className="address-form" onSubmit={handleSubmit} noValidate>
      <h2>Add New Address</h2>

      {error && <p className="error">{error}</p>}

      <div className="field">
        <label htmlFor="name">Full Name *</label>
        <input
          id="name"
          name="name"
          type="text"
          value={form.name}
          onChange={handleChange}
          placeholder="Jane Doe"
          required
        />
      </div>

      <div className="field">
        <label htmlFor="street">Street Address *</label>
        <input
          id="street"
          name="street"
          type="text"
          value={form.street}
          onChange={handleChange}
          placeholder="123 Main St"
          required
        />
      </div>

      <div className="field">
        <label htmlFor="country">Country *</label>
        <select
          id="country"
          name="country"
          value={form.country}
          onChange={handleChange}
          disabled={loadingCountries}
          required
        >
          <option value="">
            {loadingCountries ? 'Loading countries…' : '— Select a country —'}
          </option>
          {countries.map((c) => (
            <option key={c} value={c}>
              {c}
            </option>
          ))}
        </select>
      </div>

      <div className="field">
        <label htmlFor="city">City *</label>
        <select
          id="city"
          name="city"
          value={form.city}
          onChange={handleChange}
          disabled={!form.country || loadingCities}
          required
        >
          <option value="">
            {loadingCities
              ? 'Loading cities…'
              : form.country
              ? '— Select a city —'
              : '— Select a country first —'}
          </option>
          {cities.map((c) => (
            <option key={c} value={c}>
              {c}
            </option>
          ))}
        </select>
      </div>

      <div className="field">
        <label htmlFor="zip">ZIP / Postal Code</label>
        <input
          id="zip"
          name="zip"
          type="text"
          value={form.zip}
          onChange={handleChange}
          placeholder="110001"
        />
      </div>

      <button type="submit" className="btn-primary">
        Save Address
      </button>
    </form>
  );
}
