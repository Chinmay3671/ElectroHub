// ElectroHub - Frontend to Backend REST API Integration
const API_BASE = 'http://localhost:8081';

// Default static fallback products if backend is offline
const DEFAULT_PRODUCTS = [
    { id: 1, name: "Arduino Uno R3", category: "Microcontrollers", price: 25.99, description: "ATmega328P microcontroller development board with 14 digital I/O pins and 6 analog inputs." },
    { id: 2, name: "Raspberry Pi 4 Model B (4GB)", category: "Single Board Computers", price: 75.00, description: "Quad-core 64-bit ARM Cortex-A72 processor, 4GB LPDDR4 RAM, dual 4K micro-HDMI display outputs." },
    { id: 3, name: "ESP32 NodeMCU Module", category: "IoT & Wireless", price: 8.50, description: "Dual-core 240MHz Wi-Fi and Bluetooth BLE IoT microcontroller development board." },
    { id: 4, name: "MB102 Solderless Breadboard", category: "Prototyping", price: 4.99, description: "Standard 830 tie-points solderless breadboard with dual power distribution rails." },
    { id: 5, name: "HC-SR04 Ultrasonic Sensor", category: "Sensors", price: 3.25, description: "High accuracy non-contact ultrasonic distance measuring sensor (2cm to 400cm range)." },
    { id: 6, name: "SG90 Micro Servo Motor (9g)", category: "Motors & Actuators", price: 2.99, description: "Compact 180-degree rotation micro servo motor suitable for robotics and hobby projects." }
];

// In-memory fallback cart when backend is offline
let localCart = [
    { id: 1, productId: 1, productName: "Arduino Uno R3", category: "Microcontrollers", unitPrice: 25.99, quantity: 2, subtotal: 51.98 },
    { id: 2, productId: 3, productName: "ESP32 NodeMCU Module", category: "IoT & Wireless", unitPrice: 8.50, quantity: 3, subtotal: 25.50 }
];

let isBackendOnline = false;

// Display a simple alert/notification message
function showAlert(message, isError = false) {
    const alertBox = document.getElementById('alert-msg');
    if (!alertBox) return;
    alertBox.textContent = message;
    alertBox.className = 'alert-msg ' + (isError ? 'alert-error' : 'alert-success');
    alertBox.style.display = 'block';

    setTimeout(() => {
        alertBox.style.display = 'none';
    }, 3500);
}

// Update the backend status banner
function updateStatusBadge(online) {
    isBackendOnline = online;
    const badge = document.getElementById('backend-status');
    if (!badge) return;

    if (online) {
        badge.className = 'status-bar status-online';
        badge.innerHTML = '<strong>Backend Connected:</strong> Spring Boot REST API is ONLINE at <code>http://localhost:8081</code> (Live MySQL Sync)';
    } else {
        badge.className = 'status-bar status-offline';
        badge.innerHTML = '<strong>Backend Offline:</strong> Spring Boot is not running on <code>http://localhost:8081</code>. Operating in Demo/Local mode. (Run <code>mvn spring-boot:run</code> to connect live database)';
    }
}

// 1. Fetch and Display Products
async function loadProducts() {
    const container = document.getElementById('products-container');
    if (!container) return;

    let products = DEFAULT_PRODUCTS;

    try {
        const response = await fetch(`${API_BASE}/api/products`, { cache: 'no-cache' });
        if (response.ok) {
            const data = await response.json();
            if (Array.isArray(data) && data.length > 0) {
                products = data;
                updateStatusBadge(true);
            }
        } else {
            updateStatusBadge(false);
        }
    } catch (err) {
        updateStatusBadge(false);
    }

    // Render product cards
    container.innerHTML = products.map(product => `
        <div class="product-card" id="product-${product.id}">
            <h3>${product.id}. ${escapeHtml(product.name)}</h3>
            <p><strong>Category:</strong> ${escapeHtml(product.category)}</p>
            <p><strong>Description:</strong> ${escapeHtml(product.description || '')}</p>
            <p><strong>Price:</strong> $${Number(product.price).toFixed(2)}</p>
            <div class="action-row">
                <label for="qty-${product.id}">Quantity:</label>
                <input type="number" id="qty-${product.id}" value="1" min="1" max="99">
                <button type="button" class="btn" onclick="handleAddToCart(${product.id})">Add to Cart</button>
            </div>
        </div>
    `).join('');
}

// 2. Handle Add to Cart
async function handleAddToCart(productId) {
    const qtyInput = document.getElementById(`qty-${productId}`);
    const quantity = qtyInput ? parseInt(qtyInput.value, 10) : 1;

    if (isNaN(quantity) || quantity <= 0) {
        showAlert('Please enter a valid quantity greater than 0', true);
        return;
    }

    if (isBackendOnline) {
        try {
            const response = await fetch(`${API_BASE}/api/cart/items`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ productId: productId, quantity: quantity })
            });

            if (response.ok) {
                const item = await response.json();
                showAlert(`Added ${item.productName || 'product'} (Qty: ${item.quantity}) to cart!`);
                await loadCart();
            } else {
                showAlert('Failed to add product to backend cart', true);
            }
        } catch (err) {
            showAlert('Connection error. Operating in local mode.', true);
            addToLocalCart(productId, quantity);
        }
    } else {
        addToLocalCart(productId, quantity);
    }
}

// Local Cart helper when offline
function addToLocalCart(productId, quantity) {
    const product = DEFAULT_PRODUCTS.find(p => p.id === productId);
    if (!product) return;

    const existing = localCart.find(item => item.productId === productId);
    if (existing) {
        existing.quantity += quantity;
        existing.subtotal = +(existing.quantity * existing.unitPrice).toFixed(2);
    } else {
        const nextId = localCart.length > 0 ? Math.max(...localCart.map(i => i.id)) + 1 : 1;
        localCart.push({
            id: nextId,
            productId: product.id,
            productName: product.name,
            category: product.category,
            unitPrice: product.price,
            quantity: quantity,
            subtotal: +(quantity * product.price).toFixed(2)
        });
    }

    showAlert(`Added ${product.name} (Qty: ${quantity}) to cart (local mode)!`);
    renderCartTable(localCart);
}

// 3. Fetch and Display Cart
async function loadCart() {
    if (isBackendOnline) {
        try {
            // First check if cart total endpoint works
            const totalRes = await fetch(`${API_BASE}/api/cart/total`);
            if (totalRes.ok) {
                const totalData = await totalRes.json();
                renderCartTable(totalData.items || [], totalData.grandTotal, totalData.totalItems, totalData.totalQuantity);
                return;
            }

            // Fallback to /api/cart/items
            const itemsRes = await fetch(`${API_BASE}/api/cart/items`);
            if (itemsRes.ok) {
                const items = await itemsRes.json();
                renderCartTable(items);
                return;
            }
        } catch (err) {
            // Fall back to localCart
        }
    }

    renderCartTable(localCart);
}

// Render the cart table and summary
function renderCartTable(items, customGrandTotal, customTotalItems, customTotalQty) {
    const tbody = document.getElementById('cart-table-body');
    const summary = document.getElementById('cart-summary');
    if (!tbody || !summary) return;

    if (!items || items.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; padding: 15px; color: #777;">Your cart is currently empty.</td></tr>`;
        summary.innerHTML = `
            <p><strong>Total Items in Cart:</strong> 0</p>
            <p><strong>Total Quantity:</strong> 0 units</p>
            <p class="grand-total"><strong>Grand Total:</strong> $0.00</p>
        `;
        return;
    }

    let grandTotal = 0;
    let totalQty = 0;

    tbody.innerHTML = items.map(item => {
        const subtotal = item.subtotal !== undefined ? item.subtotal : (item.unitPrice * item.quantity);
        grandTotal += subtotal;
        totalQty += item.quantity;

        return `
            <tr>
                <td>${item.id}</td>
                <td><strong>${escapeHtml(item.productName)}</strong></td>
                <td>${escapeHtml(item.category || '')}</td>
                <td>$${Number(item.unitPrice).toFixed(2)}</td>
                <td>${item.quantity}</td>
                <td>$${Number(subtotal).toFixed(2)}</td>
                <td>
                    <button type="button" class="btn btn-remove" onclick="handleRemoveCartItem(${item.id})">Remove</button>
                </td>
            </tr>
        `;
    }).join('');

    const finalTotal = customGrandTotal !== undefined ? customGrandTotal : grandTotal;
    const finalItems = customTotalItems !== undefined ? customTotalItems : items.length;
    const finalQty = customTotalQty !== undefined ? customTotalQty : totalQty;

    summary.innerHTML = `
        <p><strong>Total Items in Cart:</strong> ${finalItems}</p>
        <p><strong>Total Quantity:</strong> ${finalQty} units</p>
        <p class="grand-total"><strong>Grand Total:</strong> $${Number(finalTotal).toFixed(2)}</p>
    `;
}

// 4. Remove Item from Cart
async function handleRemoveCartItem(id) {
    if (isBackendOnline) {
        try {
            const res = await fetch(`${API_BASE}/api/cart/items/${id}`, { method: 'DELETE' });
            if (res.ok) {
                showAlert(`Item #${id} removed from cart!`);
                await loadCart();
                return;
            }
        } catch (err) {
            // Fall back to local
        }
    }

    localCart = localCart.filter(item => item.id !== id);
    showAlert(`Item #${id} removed from cart!`);
    renderCartTable(localCart);
}

// 5. Clear Entire Cart
async function handleClearCart() {
    if (isBackendOnline) {
        try {
            const res = await fetch(`${API_BASE}/api/cart/items`, { method: 'DELETE' });
            if (res.ok) {
                showAlert('All items cleared from cart!');
                await loadCart();
                return;
            }
        } catch (err) {
            // Fall back to local
        }
    }

    localCart = [];
    showAlert('All items cleared from cart!');
    renderCartTable(localCart);
}

// 6. Load External Resources
async function loadResources() {
    const list = document.getElementById('resource-list');
    if (!list) return;

    if (isBackendOnline) {
        try {
            const res = await fetch(`${API_BASE}/api/resources`);
            if (res.ok) {
                const resources = await res.json();
                if (Array.isArray(resources) && resources.length > 0) {
                    list.innerHTML = resources.map(r => `
                        <li>
                            <strong>${escapeHtml(r.title)}:</strong>
                            <a href="${escapeHtml(r.url)}" target="_blank" rel="noopener noreferrer">${escapeHtml(r.url)}</a>
                            - ${escapeHtml(r.description || '')}
                        </li>
                    `).join('');
                    return;
                }
            }
        } catch (err) {
            // Fall back to default static list
        }
    }
}

// Simple HTML Escaping for Security
function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', async () => {
    // Bind Clear Cart button
    const clearBtn = document.getElementById('clear-cart-btn');
    if (clearBtn) {
        clearBtn.addEventListener('click', handleClearCart);
    }

    // Load initial data
    await loadProducts();
    await loadCart();
    await loadResources();
});
