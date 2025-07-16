import React, { useState, useEffect, useCallback } from 'react';
import './App.css'; // <-- Importamos nuestro archivo de estilos

// --- Constantes de Configuración ---
const API_PRODUCTS_URL = 'http://localhost:8091/api/v1/products';
const API_INVENTORY_URL = 'http://localhost:8092/api/v1/inventory';
const API_KEY = 'mi-api-key-secreta';

// --- Componente Modal para Editar Productos ---
function EditProductModal({ product, onClose, onProductUpdated }) {
  const [nombre, setNombre] = useState(product.attributes.nombre);
  const [precio, setPrecio] = useState(product.attributes.precio);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);

    try {
      const response = await fetch(`${API_PRODUCTS_URL}/${product.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'X-API-KEY': API_KEY,
        },
        body: JSON.stringify({ nombre, precio: parseFloat(precio) }),
      });

      if (!response.ok) {
        throw new Error(`Error al actualizar el producto. Estatus: ${response.status}`);
      }
      
      onProductUpdated();
      onClose();
    } catch (err) {
      setError(err.message);
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <div className="modal-header">
          <h2>Editar Producto</h2>
          <button onClick={onClose} className="modal-close-btn">&times;</button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="edit-nombre">Nombre del Producto</label>
            <input id="edit-nombre" type="text" value={nombre} onChange={(e) => setNombre(e.target.value)} />
          </div>
          <div className="form-group">
            <label htmlFor="edit-precio">Precio</label>
            <input id="edit-precio" type="number" value={precio} onChange={(e) => setPrecio(e.target.value)} step="0.01" />
          </div>
          {error && <p className="text-red">{error}</p>}
          <div className="modal-footer">
            <button type="button" onClick={onClose} className="btn btn-secondary">Cancelar</button>
            <button type="submit" disabled={isLoading} className="btn btn-primary">
              {isLoading ? 'Guardando...' : 'Guardar Cambios'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}


// --- Componente para el Formulario de Creación de Productos ---
function CreateProductForm({ onProductCreated }) {
  const [nombre, setNombre] = useState('');
  const [precio, setPrecio] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!nombre || !precio) {
      setError('Nombre y precio son requeridos.');
      return;
    }
    setIsLoading(true);
    setError(null);

    try {
      const response = await fetch(API_PRODUCTS_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-API-KEY': API_KEY,
        },
        body: JSON.stringify({ nombre, precio: parseFloat(precio) }),
      });

      if (!response.ok) {
        throw new Error(`Error al crear el producto. Estatus: ${response.status}`);
      }
      
      onProductCreated();
      setNombre('');
      setPrecio('');
    } catch (err) {
      setError(err.message);
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>Crear Nuevo Producto</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="nombre">Nombre del Producto</label>
          <input type="text" id="nombre" value={nombre} onChange={(e) => setNombre(e.target.value)} placeholder="Ej: Monitor Curvo" disabled={isLoading}/>
        </div>
        <div className="form-group">
          <label htmlFor="precio">Precio</label>
          <input type="number" id="precio" value={precio} onChange={(e) => setPrecio(e.target.value)} placeholder="Ej: 499.99" step="0.01" disabled={isLoading}/>
        </div>
        <button type="submit" disabled={isLoading} className="btn btn-primary btn-full">
          {isLoading ? 'Creando...' : 'Crear Producto'}
        </button>
        {error && <p className="text-red">{error}</p>}
      </form>
    </div>
  );
}

// --- Componente para la Lista de Productos ---
function ProductList({ products, onSelectProduct, onEditProduct, onDeleteProduct }) {
  if (!products || products.length === 0) {
    return (
        <div className="card text-center">
            <p>No hay productos para mostrar.</p>
        </div>
    );
  }
  
  return (
    <div className="card">
      <div className="table-container">
        <table className="product-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Precio</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <tr key={product.id}>
                <td>{product.id}</td>
                <td>{product.attributes.nombre}</td>
                <td>${product.attributes.precio != null ? product.attributes.precio.toFixed(2) : 'N/A'}</td>
                <td className="actions-cell">
                  <button onClick={() => onSelectProduct(product.id)} className="btn btn-success">Inventario</button>
                  <button onClick={() => onEditProduct(product)} className="btn btn-warning">Editar</button>
                  <button onClick={() => onDeleteProduct(product.id)} className="btn btn-danger">Eliminar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

// --- Componente para el Detalle y Actualización de Inventario ---
function InventoryDetail({ productId, onStockUpdated }) {
    const [inventory, setInventory] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [quantityChange, setQuantityChange] = useState('');
  
    const fetchInventory = useCallback(async () => {
      if (!productId) {
        setInventory(null);
        return;
      }
      setIsLoading(true);
      setError(null);
      try {
        const response = await fetch(`${API_INVENTORY_URL}/${productId}`);
        if (!response.ok) {
          throw new Error(`Estatus: ${response.status}`);
        }
        const data = await response.json();
        setInventory(data);
      } catch (err) {
        setError(`Error al consultar el inventario. ${err.message}`);
        console.error('Detalle del error:', err);
      } finally {
        setIsLoading(false);
      }
    }, [productId]);
  
    useEffect(() => {
      fetchInventory();
    }, [fetchInventory]);
  
    const handleStockUpdate = async (e) => {
        e.preventDefault();
        const change = parseInt(quantityChange, 10);
        if (isNaN(change)) {
            alert('Por favor, introduce un número válido.');
            return;
        }
  
        try {
            const response = await fetch(`${API_INVENTORY_URL}/${productId}/stock`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ quantityChange: change })
            });
  
            if (!response.ok) {
                throw new Error(`Error al actualizar el stock. Estatus: ${response.status}`);
            }
            
            setQuantityChange('');
            onStockUpdated();
        } catch (err) {
            setError(err.message);
            console.error(err);
        }
    };
  
    if (!productId) {
      return (
        <div className="card text-center">
          <p>Selecciona un producto para ver su inventario.</p>
        </div>
      );
    }
  
    if (isLoading) return <div className="card text-center"><p>Cargando inventario...</p></div>;
  
    return (
      <div className="card">
        <h3>Detalle de Inventario</h3>
        {error && <p className="text-red text-center">{error}</p>}
        {inventory && !error && (
          <>
            <div className="inventory-details">
              <p><strong>ID Producto:</strong> {inventory.productId}</p>
              <p><strong>Nombre:</strong> {inventory.nombreProducto}</p>
              <p><strong>Precio:</strong> ${inventory.precioProducto != null ? inventory.precioProducto.toFixed(2) : 'N/A'}</p>
              <p><strong>Cantidad en Stock:</strong> {inventory.cantidad}</p>
            </div>
  
            <hr/>
  
            <h3>Actualizar Stock</h3>
            <form onSubmit={handleStockUpdate} className="update-stock-form">
              <input type="number" value={quantityChange} onChange={(e) => setQuantityChange(e.target.value)} placeholder="Ej: -5 o 10" required />
              <button type="submit" className="btn btn-purple">Actualizar</button>
            </form>
          </>
        )}
      </div>
    );
}

// --- Componente Principal de la Aplicación ---
export default function App() {
  const [products, setProducts] = useState([]);
  const [selectedProductId, setSelectedProductId] = useState(null);
  const [isLoadingProducts, setIsLoadingProducts] = useState(false);
  const [errorProducts, setErrorProducts] = useState(null);
  
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [productToEdit, setProductToEdit] = useState(null);

  const fetchProducts = useCallback(async () => {
    setIsLoadingProducts(true);
    setErrorProducts(null);
    try {
      const response = await fetch(API_PRODUCTS_URL, {
        headers: { 'X-API-KEY': API_KEY },
      });
      if (!response.ok) {
        throw new Error(`Estatus: ${response.status}`);
      }
      const responseData = await response.json();
      
      // --- LA LÍNEA DE LA SOLUCIÓN ---
      // Verificamos si la respuesta tiene la propiedad 'data' (que es un array)
      // y la usamos. Si no, asumimos que la respuesta es el array directamente.
      if (responseData && Array.isArray(responseData.data)) {
        setProducts(responseData.data);
      } else {
        // Fallback por si la API devuelve un array simple
        setProducts(responseData);
      }

    } catch (err) {
      setErrorProducts(`Error al cargar productos. ${err.message}`);
      console.error('Detalle del error:', err);
    } finally {
      setIsLoadingProducts(false);
    }
  }, []);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  const handleEditProduct = (product) => {
    setProductToEdit(product);
    setIsEditModalOpen(true);
  };

  const handleDeleteProduct = async (productId) => {
    if (window.confirm('¿Estás seguro de que quieres eliminar este producto?')) {
      try {
        const response = await fetch(`${API_PRODUCTS_URL}/${productId}`, {
          method: 'DELETE',
          headers: { 'X-API-KEY': API_KEY },
        });

        if (!response.ok) {
          throw new Error(`Error al eliminar el producto. Estatus: ${response.status}`);
        }
        
        if (selectedProductId === productId) {
            setSelectedProductId(null);
        }
        fetchProducts();
      } catch (err) {
        alert(err.message);
        console.error(err);
      }
    }
  };

  return (
    <div className="app-container">
      {isEditModalOpen && (
        <EditProductModal 
          product={productToEdit}
          onClose={() => setIsEditModalOpen(false)}
          onProductUpdated={fetchProducts}
        />
      )}

      <header className="app-header">
        <h1>Panel de Gestión</h1>
        <p>Una interfaz para interactuar con los microservicios de Productos e Inventario.</p>
      </header>

      <main className="main-grid">
        <div>
          <CreateProductForm onProductCreated={fetchProducts} />
          <div className="card">
             <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem'}}>
                <h2>Lista de Productos</h2>
                <button onClick={fetchProducts} disabled={isLoadingProducts} className="btn btn-secondary">
                    {isLoadingProducts ? 'Recargando...' : 'Recargar Productos'}
                </button>
            </div>
             {isLoadingProducts && <p className="text-center">Cargando...</p>}
             {errorProducts && <p className="text-red text-center">{errorProducts}</p>}
             {!isLoadingProducts && !errorProducts && (
                <ProductList 
                  products={products} 
                  onSelectProduct={setSelectedProductId}
                  onEditProduct={handleEditProduct}
                  onDeleteProduct={handleDeleteProduct}
                />
             )}
          </div>
        </div>

        <div>
           <InventoryDetail 
              productId={selectedProductId} 
              onStockUpdated={() => {
                  const currentId = selectedProductId;
                  setSelectedProductId(null);
                  setTimeout(() => setSelectedProductId(currentId), 0);
              }}
           />
        </div>
      </main>
    </div>
  );
}
