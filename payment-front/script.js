const API_URL = "/api"

async function loadPayments() {
    const tableBody = document.getElementById("payments-table");
    tableBody.innerHTML = "";

    try {
        const response = await fetch(`${API_URL}/payments`);
        const payments = await response.json();

        payments.forEach((payment) => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${payment.id}</td>
                <td>${payment.orderId}</td>
                <td>${payment.amount}</td>
                <td id="status-${payment.id}">${payment.status}</td>
                <td>
                    <button class="btn btn-success btn-sm" onclick="updateStatus(${payment.id}, 'DONE')" ${
                payment.status !== "NEW" ? "disabled" : ""
            }>Mark as DONE</button>
                    <button class="btn btn-danger btn-sm" onclick="updateStatus(${payment.id}, 'CANCEL')" ${
                payment.status !== "NEW" ? "disabled" : ""
            }>Mark as CANCEL</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error("Ошибка загрузки платежей:", error);
    }
}

async function updateStatus(paymentId, newStatus) {
    try {
        const response = await fetch(
            `${API_URL}/payments/${paymentId}/status?payment_status=${newStatus}`,
            { method: "POST" }
        );

        if (response.ok) {
            document.getElementById(`status-${paymentId}`).textContent = newStatus;

            document
                .querySelector(`button[onclick="updateStatus(${paymentId}, 'DONE')"]`)
                .setAttribute("disabled", "true");
            document
                .querySelector(`button[onclick="updateStatus(${paymentId}, 'CANCEL')"]`)
                .setAttribute("disabled", "true");
        } else {
            console.error("Не удалось обновить статус платежа:", await response.text());
        }
    } catch (error) {
        console.error("Ошибка при обновлении статуса:", error);
    }
}

document.addEventListener("DOMContentLoaded", loadPayments);
