import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useEmployees } from '../context/EmployeesContext';

export default function EmployeeDetail() {
  const { id } = useParams();
  const { employees, updateEmployee } = useEmployees();

  const original = employees.find((e) => e.id === id);

  const [isEditing, setIsEditing] = useState(false);
  const [form, setForm] = useState(original ?? null);
  const [countries, setCountries] = useState([]);
  const [cities, setCities] = useState([]);
  const [loadingCountries, setLoadingCountries] = useState(false);
  const [loadingCities, setLoadingCities] = useState(false);
  const [saved, setSaved] = useState(false);

  // Fetch countries when edit mode is turned on
  useEffect(() => {
    if (!isEditing || countries.length > 0) return;
    setLoadingCountries(true);
    fetch('https://countriesnow.space/api/v0.1/countries')
      .then((res) => res.json())
      .then((json) => {
        if (!json.error) setCountries(json.data.map((c) => c.country).sort());
      })
      .catch(() => {})
      .finally(() => setLoadingCountries(false));
  }, [isEditing]);

  // Fetch cities when country changes in edit mode
  useEffect(() => {
    if (!isEditing || !form?.country) {
      setCities([]);
      return;
    }
    setLoadingCities(true);
    fetch('https://countriesnow.space/api/v0.1/countries/cities', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ country: form.country }),
      redirect: 'follow',
    })
      .then((res) => res.json())
      .then((json) => setCities(json.error ? [] : json.data.sort()))
      .catch(() => setCities([]))
      .finally(() => setLoadingCities(false));
  }, [form?.country, isEditing]);

  if (!original) {
    return (
      <div className="emp-detail-not-found">
        <p>Employee <strong>{id}</strong> not found.</p>
        <Link to="/employee" className="nav-link">← Back to Search</Link>
      </div>
    );
  }

  const handleChange = (e) => {
    const { name, value } = e.target;
    // Reset city when country changes
    if (name === 'country') {
      setForm((prev) => ({ ...prev, country: value, city: '' }));
    } else {
      setForm((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleEdit = () => {
    setIsEditing(true);
    setSaved(false);
  };

  const handleSave = () => {
    updateEmployee(form);  // write updated employee back to shared context
    setIsEditing(false);
    setSaved(true);
  };

  const handleCancel = () => {
    setForm(original);  // revert to last saved value from context
    setIsEditing(false);
    setSaved(false);
  };

  const fields = [
    { key: 'name',       label: 'Full Name',   type: 'text' },
    { key: 'email',      label: 'Email',        type: 'email' },
    { key: 'phone',      label: 'Phone',        type: 'text' },
    { key: 'department', label: 'Department',   type: 'text' },
  ];

  return (
    <div className="emp-detail">
      {/* Back link */}
      <Link to="/employee" className="emp-back-link">← Back to Search</Link>

      <div className="address-form">
        <div className="emp-detail-header">
          <div>
            <h2>{form.name}</h2>
            <span className="emp-id-badge">{form.id}</span>
          </div>
          <div className="emp-detail-actions">
            {!isEditing ? (
              <button className="btn-primary emp-btn" onClick={handleEdit}>
                ✏️ Edit
              </button>
            ) : (
              <>
                <button className="btn-primary emp-btn" onClick={handleSave}>
                  💾 Save
                </button>
                <button className="btn-secondary emp-btn" onClick={handleCancel}>
                  Cancel
                </button>
              </>
            )}
          </div>
        </div>

        {saved && (
          <p className="save-success">✅ Changes saved successfully.</p>
        )}

        {/* Text fields */}
        {fields.map(({ key, label, type }) => (
          <div className="field" key={key}>
            <label htmlFor={key}>{label}</label>
            {isEditing ? (
              <input
                id={key}
                name={key}
                type={type}
                value={form[key] ?? ''}
                onChange={handleChange}
              />
            ) : (
              <p className="emp-detail-value">{form[key]}</p>
            )}
          </div>
        ))}

        {/* Country */}
        <div className="field">
          <label htmlFor="country">Country</label>
          {isEditing ? (
            <select
              id="country"
              name="country"
              value={form.country}
              onChange={handleChange}
              disabled={loadingCountries}
            >
              <option value="">
                {loadingCountries ? 'Loading countries…' : '— Select a country —'}
              </option>
              {countries.map((c) => (
                <option key={c} value={c}>{c}</option>
              ))}
            </select>
          ) : (
            <p className="emp-detail-value">{form.country}</p>
          )}
        </div>

        {/* City */}
        <div className="field">
          <label htmlFor="city">City</label>
          {isEditing ? (
            <select
              id="city"
              name="city"
              value={form.city}
              onChange={handleChange}
              disabled={!form.country || loadingCities}
            >
              <option value="">
                {loadingCities
                  ? 'Loading cities…'
                  : form.country
                  ? '— Select a city —'
                  : '— Select a country first —'}
              </option>
              {cities.map((c) => (
                <option key={c} value={c}>{c}</option>
              ))}
            </select>
          ) : (
            <p className="emp-detail-value">{form.city}</p>
          )}
        </div>
      </div>
    </div>
  );
}
