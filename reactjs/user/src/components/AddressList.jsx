export default function AddressList({ addresses, onDelete }) {
  if (addresses.length === 0) {
    return (
      <div className="address-list empty">
        <p>No addresses saved yet. Add one above!</p>
      </div>
    );
  }

  return (
    <div className="address-list">
      <h2>Saved Addresses ({addresses.length})</h2>
      <ul>
        {addresses.map((addr) => (
          <li key={addr.id} className="address-card">
            <div className="address-info">
              <strong>{addr.name}</strong>
              <span>{addr.street}</span>
              <span>
                {addr.city}, {addr.country}
                {addr.zip ? ` — ${addr.zip}` : ''}
              </span>
            </div>
            <button
              className="btn-delete"
              onClick={() => onDelete(addr.id)}
              aria-label={`Delete address for ${addr.name}`}
            >
              ✕
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
