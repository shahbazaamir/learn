import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useEmployees } from '../context/EmployeesContext';

const INITIAL_FILTERS = {
  name: '',
  employeeId: '',
  country: '',
  city: '',
};

export default function EmployeeSearch() {
  const { employees } = useEmployees();
  const [countries, setCountries] = useState([]);
  const [cities, setCities] = useState([]);
  const [filters, setFilters] = useState(INITIAL_FILTERS);
  const [results, setResults] = useState(null); // null = show all
  const [loadingCountries, setLoadingCountries] = useState(true);
  const [loadingCities, setLoadingCities] = useState(false);

  // Fetch countries on mount
  useEffect(() => {
    fetch('https://countriesnow.space/api/v0.1/countries')
      .then((res) => res.json())
      .then((json) => {
        if (!json.error) setCountries(json.data.map((c) => c.country).sort());
      })
      .catch(() => {})
      .finally(() => setLoadingCountries(false));
  }, []);

  // Fetch cities when country changes
  useEffect(() => {
    if (!filters.country) {
      setCities([]);
      setFilters((prev) => ({ ...prev, city: '' }));
      return;
    }
    setLoadingCities(true);
    setFilters((prev) => ({ ...prev, city: '' }));
    fetch('https://countriesnow.space/api/v0.1/countries/cities', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ country: filters.country }),
      redirect: 'follow',
    })
      .then((res) => res.json())
      .then((json) => setCities(json.error ? [] : json.data.sort()))
      .catch(() => setCities([]))
      .finally(() => setLoadingCities(false));
  }, [filters.country]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  const handleSearch = (e) => {
    e.preventDefault();
    const { name, employeeId, city } = filters;
    const filtered = employees.filter((emp) => {
      const matchName = !name       || emp.name.toLowerCase().includes(name.toLowerCase());
      const matchId   = !employeeId || emp.id.toLowerCase().includes(employeeId.toLowerCase());
      const matchCity = !city       || emp.city.toLowerCase() === city.toLowerCase();
      return matchName && matchId && matchCity;
    });
    setResults(filtered);
  };

  const handleReset = () => {
    setFilters(INITIAL_FILTERS);
    setResults(null);
    setCities([]);
  };

  // Display filtered results if a search was run, otherwise show all
  const displayList = results ?? employees;

  return (
    <div className="employee-search">
      <form className="address-form" onSubmit={handleSearch} noValidate>
        <h2>Employee Search</h2>

        <div className="field">
          <label htmlFor="name">Name</label>
          <input
            id="name"
            name="name"
            type="text"
            value={filters.name}
            onChange={handleChange}
            placeholder="e.g. Alice"
          />
        </div>

        <div className="field">
          <label htmlFor="employeeId">Employee ID</label>
          <input
            id="employeeId"
            name="employeeId"
            type="text"
            value={filters.employeeId}
            onChange={handleChange}
            placeholder="e.g. E001"
          />
        </div>

        <div className="field">
          <label htmlFor="country">Country</label>
          <select
            id="country"
            name="country"
            value={filters.country}
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
        </div>

        <div className="field">
          <label htmlFor="city">City</label>
          <select
            id="city"
            name="city"
            value={filters.city}
            onChange={handleChange}
            disabled={!filters.country || loadingCities}
          >
            <option value="">
              {loadingCities
                ? 'Loading cities…'
                : filters.country
                ? '— Select a city —'
                : '— Select a country first —'}
            </option>
            {cities.map((c) => (
              <option key={c} value={c}>{c}</option>
            ))}
          </select>
        </div>

        <div className="form-actions">
          <button type="submit" className="btn-primary">Search</button>
          <button type="button" className="btn-secondary" onClick={handleReset}>Reset</button>
        </div>
      </form>

      {/* Results */}
      <div className="employee-results">
        <h3>Results ({displayList.length})</h3>
        {displayList.length === 0 ? (
          <p className="no-results">No employees match your search.</p>
        ) : (
          <table className="employee-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>City</th>
                <th>Country</th>
              </tr>
            </thead>
            <tbody>
              {displayList.map((emp) => (
                <tr key={emp.id}>
                  <td>
                    <Link to={`/employee/${emp.id}`} className="emp-id-link">
                      {emp.id}
                    </Link>
                  </td>
                  <td>{emp.name}</td>
                  <td>{emp.city}</td>
                  <td>{emp.country}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
