<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>My Orders | Techouts Mart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body class="bg-ink">
<nav class="navbar navbar-expand-lg nav-glass">
    <div class="container">
        <a class="navbar-brand brand" href="${pageContext.request.contextPath}/home">Techouts Mart</a>
        <div class="d-flex gap-2">
            <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/cart">Cart</a>
            <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/home">Home</a>
            <a class="btn btn-sm btn-logout" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </div>
</nav>

<section class="container py-4">
    <h3 class="text-white mb-3">My Orders - ${username}</h3>

    <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="empty-panel text-white">No orders yet.</div>
        </c:when>
        <c:otherwise>
            <c:forEach var="order" items="${orders}">
                <div class="panel mb-3">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <h5 class="m-0">Order #${order.id}</h5>
                        <span class="badge bg-success">${order.status}</span>
                    </div>
                    <div class="mb-1">Date: ${order.orderDate}</div>
                    <div class="mb-1">Address: ${order.shippingAddress}</div>
                    <div class="mb-2">Payment: ${order.paymentMethod}</div>
                    <table class="table table-dark table-sm align-middle mb-2">
                        <thead>
                        <tr><th>Product</th><th>Price</th><th>Qty</th><th>Subtotal</th></tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${order.orderItems}">
                            <tr>
                                <td>${item.product.name}</td>
                                <td>Rs.${item.price}</td>
                                <td>${item.quantity}</td>
                                <td>Rs.${item.price * item.quantity}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <div><strong>Total: Rs.${order.totalAmount}</strong></div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</section>
</body>
</html>
