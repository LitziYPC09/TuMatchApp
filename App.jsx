// ...existing code...
import CategoriaListComponent from './components/CategoriaListComponent';
// ...existing code...

function App() {
  // ...existing code...
  return (
    <div>
      <h2>Electrónica</h2>
      <CategoriaListComponent categoria="electronica" />
      <h2>Ropa</h2>
      <CategoriaListComponent categoria="ropa" />
      {/* Puedes agregar más categorías aquí */}
    </div>
  );
}
// ...existing code...

