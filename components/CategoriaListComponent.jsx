import React, { useEffect, useState } from 'react';

function CategoriaListComponent({ categoria }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    fetch(`https://tu-api.com/items?categoria=${categoria}`)
      .then(res => res.json())
      .then(data => {
        setItems(data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, [categoria]);

  if (loading) return <div>Cargando...</div>;

  return (
    <ul>
      {items.map(item => (
        <li key={item.id}>{item.nombre}</li>
      ))}
    </ul>
  );
}

export default CategoriaListComponent;

