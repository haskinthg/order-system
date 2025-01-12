const API_URL = "/api"

async function loadDeliveries() {
    const tableBody = document.getElementById("deliveries-table");
    tableBody.innerHTML = "";

    try {
        const response = await fetch(`${API_URL}/deliveries`);
        const deliveries = await response.json();

        deliveries.forEach((delivery) => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${delivery.id}</td>
                <td>${delivery.orderId}</td>
                <td>${delivery.paymentId}</td>
                <td id="status-${delivery.id}">${delivery.status}</td>
                <td>
                    <button class="btn btn-success btn-sm" onclick="updateStatus(${delivery.id}, 'DONE')" ${
                delivery.status !== "NEW" ? "disabled" : ""
            }>Mark as DONE</button>
                    <button class="btn btn-danger btn-sm" onclick="updateStatus(${delivery.id}, 'CANCEL')" ${
                delivery.status !== "NEW" ? "disabled" : ""
            }>Mark as CANCEL</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error("Ошибка загрузки доставок:", error);
    }
}

async function updateStatus(deliveryId, newStatus) {
    try {
        const response = await fetch(
            `${API_URL}/deliveries/${deliveryId}/status?delivery_status=${newStatus}`,
            { method: "POST" }
        );

        if (response.ok) {
            document.getElementById(`status-${deliveryId}`).textContent = newStatus;

            document
                .querySelector(`button[onclick="updateStatus(${deliveryId}, 'DONE')"]`)
                .setAttribute("disabled", "true");
            document
                .querySelector(`button[onclick="updateStatus(${deliveryId}, 'CANCEL')"]`)
                .setAttribute("disabled", "true");
        } else {
            console.error("Не удалось обновить статус доставки:", await response.text());
        }
    } catch (error) {
        console.error("Ошибка при обновлении статуса:", error);
    }
}

document.addEventListener("DOMContentLoaded", loadDeliveries);
