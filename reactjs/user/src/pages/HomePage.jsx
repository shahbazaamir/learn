import { Link } from 'react-router-dom';

export default function HomePage() {
  return (
    <div className="home-page">
      <h2>Welcome 👋</h2>
      <p>Choose a section to get started:</p>
      <div className="home-cards">
        <Link to="/address" className="home-card">
          <span className="home-card-icon">📍</span>
          <strong>Address Book</strong>
          <span>Store and manage addresses</span>
        </Link>
        <Link to="/employee" className="home-card">
          <span className="home-card-icon">👤</span>
          <strong>Employee</strong>
          <span>Search employee details</span>
        </Link>
      </div>
    </div>
  );
}
