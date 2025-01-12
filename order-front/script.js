let apiUrl = "/api";

async function loadOrders() {
    const response = await fetch(apiUrl + '/orders');
    const orders = await response.json();

    const tableBody = document.getElementById('ordersTable').querySelector('tbody');
    tableBody.innerHTML = '';

    orders.forEach(order => {
        const row = document.createElement('tr');
        row.innerHTML = `
      <td>${order.id}</td>
      <td>${order.customer}</td>
      <td>${order.product}</td>
      <td>${order.quantity}</td>
      <td>${order.status}</td>
    `;
        tableBody.appendChild(row);
    });
}

document.getElementById('orderForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const customer = document.getElementById('customer').value;
    const product = document.getElementById('product').value;
    const quantity = document.getElementById('quantity').value;

    const response = await fetch(apiUrl + '/orders', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ customer, product, quantity }),
    });

    if (response.ok) {
        loadOrders();
        document.getElementById('orderForm').reset();
    } else {
        alert('Error adding order');
    }
});

async function init() {
    await loadOrders();
}

init();
