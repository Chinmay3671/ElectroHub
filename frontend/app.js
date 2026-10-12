// ElectroHub - Frontend to Backend REST API Integration with Ordering
const API_BASE = 'http://localhost:8081';

// Seeded sample products matching the Spring Boot database
const DEFAULT_PRODUCTS = [
    { id: 1, name: "Arduino Uno R3", category: "Microcontrollers", price: 25.99, description: "ATmega328P microcontroller development board with 14 digital I/O pins and 6 analog inputs." },
    { id: 2, name: "Raspberry Pi 4 Model B (4GB)", category: "Single Board Computers", price: 75.00, description: "Quad-core 64-bit ARM Cortex-A72 processor, 4GB LPDDR4 RAM, dual 4K micro-HDMI display outputs." },
    { id: 3, name: "ESP32 NodeMCU Module", category: "IoT & Wireless", price: 8.50, description: "Dual-core 240MHz Wi-Fi and Bluetooth BLE IoT microcontroller development board." },
    { id: 4, name: "MB102 Solderless Breadboard", category: "Prototyping", price: 4.99, description: "Standard 830 tie-points solderless breadboard with dual power distribution rails." },
    { id: 5, name: "HC-SR04 Ultrasonic Sensor", category: "Sensors", price: 3.25, description: "High accuracy non-contact ultrasonic distance measuring sensor (2cm to 400cm range)." },
    { id: 6, name: "SG90 Micro Servo Motor (9g)", category: "Motors & Actuators", price: 2.99, description: "Compact 180-degree rotation micro servo motor suitable for robotics and hobby projects." }
];

// In-memory fallback cart
let localCart = [];
let allProducts = [...DEFAULT_PRODUCTS];
let currentCartItems = [];
let currentGrandTotal = 0;
let isBackendOnline = false;

// Display a floating notification message
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

// 1. Load Products from Spring Boot Backend (/api/products)
async function loadProducts() {
    try {
        const response = await fetch(`${API_BASE}/api/products`, { cache: 'no-cache' });
        if (response.ok) {
            const data = await response.json();
            if (Array.isArray(data) && data.length > 0) {
                allProducts = data;
                isBackendOnline = true;
            }
        }
    } catch (err) {
        isBackendOnline = false;
        allProducts = [...DEFAULT_PRODUCTS];
    }

    renderProducts(allProducts);
}

// Render Products Grid
function renderProducts(products) {
    const container = document.getElementById('products-container');
    if (!container) return;

    if (!products || products.length === 0) {
        container.innerHTML = '<p style="color: #666; padding: 10px;">No products found in this category.</p>';
        return;
    }

    container.innerHTML = products.map(product => `
        <div class="product-card" id="product-${product.id}">
            <h3>${product.id}. ${escapeHtml(product.name)}</h3>
            <p><strong>Category:</strong> ${escapeHtml(product.category)}</p>
            <p><strong>Description:</strong> ${escapeHtml(product.description || '')}</p>
            <p class="price"><strong>Price:</strong> $${Number(product.price).toFixed(2)}</p>
            <div class="action-row">
                <label for="qty-${product.id}">Quantity:</label>
                <input type="number" id="qty-${product.id}" value="1" min="1" max="99">
                <button type="button" class="btn" onclick="handleAddToCart(${product.id})">Add to Cart</button>
            </div>
        </div>
    `).join('');
}

// Category Filter Event
function filterCategory() {
    const category = document.getElementById('category-select').value;
    if (category === 'ALL') {
        renderProducts(allProducts);
    } else {
        const filtered = allProducts.filter(p => p.category === category);
        renderProducts(filtered);
    }
}

// 2. Add to Cart (POST /api/cart/items)
async function handleAddToCart(productId) {
    const qtyInput = document.getElementById(`qty-${productId}`);
    const quantity = qtyInput ? parseInt(qtyInput.value, 10) : 1;

    if (isNaN(quantity) || quantity <= 0) {
        showAlert('Please enter a valid quantity of 1 or more.', true);
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
                showAlert(`Added ${item.productName || 'product'} (Qty: ${quantity}) to your cart!`);
                await loadCart();
                return;
            }
        } catch (err) {
            // fallback to local
        }
    }

    addToLocalCart(productId, quantity);
}

// In-memory cart helper
function addToLocalCart(productId, quantity) {
    const product = allProducts.find(p => p.id === productId);
    if (!product) return;

    const existing = localCart.find(i => i.productId === productId);
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

    showAlert(`Added ${product.name} (Qty: ${quantity}) to your cart!`);
    renderCartTable(localCart);
}

// 3. Load Cart (GET /api/cart/total or GET /api/cart/items)
async function loadCart() {
    if (isBackendOnline) {
        try {
            const totalRes = await fetch(`${API_BASE}/api/cart/total`);
            if (totalRes.ok) {
                const data = await totalRes.json();
                currentCartItems = data.items || [];
                currentGrandTotal = data.grandTotal || 0;
                renderCartTable(currentCartItems, currentGrandTotal, data.totalItems, data.totalQuantity);
                return;
            }

            const itemsRes = await fetch(`${API_BASE}/api/cart/items`);
            if (itemsRes.ok) {
                currentCartItems = await itemsRes.json();
                renderCartTable(currentCartItems);
                return;
            }
        } catch (err) {
            // fallback
        }
    }

    currentCartItems = localCart;
    renderCartTable(localCart);
}

// Render Cart Table & Badges
function renderCartTable(items, customGrandTotal, customTotalItems, customTotalQty) {
    const tbody = document.getElementById('cart-table-body');
    const summary = document.getElementById('cart-summary');
    const badge = document.getElementById('cart-badge');
    const checkoutTotal = document.getElementById('checkout-total');
    if (!tbody || !summary) return;

    if (!items || items.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center; padding: 15px; color: #777;">Your cart is currently empty.</td></tr>`;
        summary.innerHTML = `
            <p><strong>Total Items:</strong> 0</p>
            <p><strong>Total Units:</strong> 0</p>
            <p class="grand-total"><strong>Grand Total:</strong> $0.00</p>
        `;
        if (badge) badge.textContent = '0';
        if (checkoutTotal) checkoutTotal.textContent = '$0.00';
        currentGrandTotal = 0;
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

    currentGrandTotal = finalTotal;

    summary.innerHTML = `
        <p><strong>Total Items:</strong> ${finalItems}</p>
        <p><strong>Total Units:</strong> ${finalQty}</p>
        <p class="grand-total"><strong>Grand Total:</strong> $${Number(finalTotal).toFixed(2)}</p>
    `;

    if (badge) badge.textContent = String(finalQty);
    if (checkoutTotal) checkoutTotal.textContent = `$${Number(finalTotal).toFixed(2)}`;
}

// 4. Remove Item from Cart (DELETE /api/cart/items/{id})
async function handleRemoveCartItem(id) {
    if (isBackendOnline) {
        try {
            const res = await fetch(`${API_BASE}/api/cart/items/${id}`, { method: 'DELETE' });
            if (res.ok) {
                showAlert(`Item #${id} removed from cart.`);
                await loadCart();
                return;
            }
        } catch (err) {
            // fallback
        }
    }

    localCart = localCart.filter(item => item.id !== id);
    showAlert(`Item #${id} removed from cart.`);
    renderCartTable(localCart);
}

// 5. Clear Entire Cart (DELETE /api/cart/items)
async function handleClearCart() {
    if (currentCartItems.length === 0 && localCart.length === 0) {
        showAlert('Cart is already empty.', true);
        return;
    }

    if (isBackendOnline) {
        try {
            const res = await fetch(`${API_BASE}/api/cart/items`, { method: 'DELETE' });
            if (res.ok) {
                showAlert('Shopping cart cleared.');
                await loadCart();
                return;
            }
        } catch (err) {
            // fallback
        }
    }

    localCart = [];
    currentCartItems = [];
    showAlert('Shopping cart cleared.');
    renderCartTable([]);
}

// 6. Open Checkout / Order Form
function handleOpenCheckout() {
    if (!currentCartItems || currentCartItems.length === 0) {
        showAlert('Your shopping cart is empty! Please add components before ordering.', true);
        return;
    }

    const checkoutSection = document.getElementById('checkout-section');
    if (checkoutSection) {
        checkoutSection.style.display = 'block';
        checkoutSection.scrollIntoView({ behavior: 'smooth' });
    }
}

// Cancel Checkout
function handleCancelCheckout() {
    const checkoutSection = document.getElementById('checkout-section');
    if (checkoutSection) {
        checkoutSection.style.display = 'none';
    }
}

// 7. Place Order (Submits and Confirms Order)
async function handleOrderSubmit(event) {
    event.preventDefault();

    if (!currentCartItems || currentCartItems.length === 0) {
        showAlert('Your cart is empty. Cannot place an empty order.', true);
        return;
    }

    const name = document.getElementById('cust-name').value.trim();
    const phone = document.getElementById('cust-phone').value.trim();
    const email = document.getElementById('cust-email').value.trim();
    const address = document.getElementById('cust-address').value.trim();
    const payment = document.getElementById('cust-payment').value;

    if (!name || !phone || !address) {
        showAlert('Please fill in all required fields (Name, Phone, Address).', true);
        return;
    }

    const orderId = 'ORD-' + Math.floor(100000 + Math.random() * 900000);
    const orderDate = new Date().toLocaleString();

    // Build Receipt HTML
    const itemsListHtml = currentCartItems.map(item => `
        <div>- <strong>${escapeHtml(item.productName)}</strong> x ${item.quantity} = $${(item.unitPrice * item.quantity).toFixed(2)}</div>
    `).join('');

    const receiptBox = document.getElementById('order-receipt');
    if (receiptBox) {
        receiptBox.innerHTML = `
            <p><strong>Order ID:</strong> <span style="color:#0056b3; font-weight:bold;">#${orderId}</span></p>
            <p><strong>Order Date & Time:</strong> ${orderDate}</p>
            <p><strong>Customer Name:</strong> ${escapeHtml(name)}</p>
            <p><strong>Contact:</strong> ${escapeHtml(phone)} ${email ? '(' + escapeHtml(email) + ')' : ''}</p>
            <p><strong>Delivery Address:</strong> ${escapeHtml(address)}</p>
            <p><strong>Payment Mode:</strong> ${escapeHtml(payment)}</p>
            <div class="receipt-items">
                <p><strong>Ordered Items:</strong></p>
                ${itemsListHtml}
            </div>
            <p style="font-size: 16px; color: #0056b3; margin-top: 10px;">
                <strong>Total Amount Paid:</strong> $${Number(currentGrandTotal).toFixed(2)}
            </p>
            <p style="color: #28a745; margin-top: 8px;">
                &#10004; Estimated delivery within 2-4 business days. Thank you for shopping with ElectroHub!
            </p>
        `;
    }

    // Clear cart via backend API
    if (isBackendOnline) {
        try {
            await fetch(`${API_BASE}/api/cart/items`, { method: 'DELETE' });
        } catch (err) {
            // ignore
        }
    }

    localCart = [];
    currentCartItems = [];
    await loadCart();

    // Reset Form & Hide Checkout Box
    document.getElementById('order-form').reset();
    document.getElementById('checkout-section').style.display = 'none';

    // Show Confirmation Modal
    document.getElementById('order-success-modal').style.display = 'flex';
}

// Close Modal
function handleCloseModal() {
    document.getElementById('order-success-modal').style.display = 'none';
    window.location.hash = '#home';
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

// Event Listeners on DOM Load
document.addEventListener('DOMContentLoaded', async () => {
    // Buttons
    const clearBtn = document.getElementById('clear-cart-btn');
    if (clearBtn) clearBtn.addEventListener('click', handleClearCart);

    const openCheckoutBtn = document.getElementById('open-checkout-btn');
    if (openCheckoutBtn) openCheckoutBtn.addEventListener('click', handleOpenCheckout);

    const cancelCheckoutBtn = document.getElementById('cancel-checkout-btn');
    if (cancelCheckoutBtn) cancelCheckoutBtn.addEventListener('click', handleCancelCheckout);

    const orderForm = document.getElementById('order-form');
    if (orderForm) orderForm.addEventListener('submit', handleOrderSubmit);

    const closeModalBtn = document.getElementById('close-modal-btn');
    if (closeModalBtn) closeModalBtn.addEventListener('click', handleCloseModal);

    const categorySelect = document.getElementById('category-select');
    if (categorySelect) categorySelect.addEventListener('change', filterCategory);

    // Initial Data Fetch from Spring Boot Backend
    await loadProducts();
    await loadCart();
});
