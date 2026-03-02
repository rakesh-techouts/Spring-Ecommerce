<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Order Confirmation | Techouts Mart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body class="bg-ink">
<nav class="navbar navbar-expand-lg nav-glass">
    <div class="container">
        <a class="navbar-brand brand" href="${pageContext.request.contextPath}/home">Techouts Mart</a>
        <div class="d-flex gap-2">
            <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/home">Continue Shopping</a>
            <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/orders">My Orders</a>
            <a class="btn btn-sm btn-logout" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </div>
</nav>

<section class="container py-4">
    <!-- Success Message -->
    <div class="text-center mb-4">
        <div class="alert alert-success alert-lg">
            <h3 class="mb-2">🎉 Your Order Placed Successfully!</h3>
            <p class="mb-0 fs-5">Thank you for choosing us</p>
        </div>
    </div>

    <!-- Order Details -->
    <div class="row">
        <!-- Customer Information -->
        <div class="col-lg-4 mb-4">
            <div class="panel">
                <h5 class="mb-3">Customer Information</h5>
                <div class="mb-2"><strong>Full Name:</strong> ${user.name}</div>
                <div class="mb-2"><strong>Email:</strong> ${user.email}</div>
                <div class="mb-2"><strong>Phone:</strong> ${user.phone}</div>
                <div class="mb-2"><strong>Shipping Address:</strong> ${shippingAddress}</div>
            </div>
        </div>

        <!-- Order Items -->
        <div class="col-lg-8 mb-4">
            <div class="panel">
                <h5 class="mb-3">Order Items</h5>
                <div class="table-responsive">
                    <table class="table table-dark table-hover align-middle">
                        <thead>
                        <tr>
                            <th>Product</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Subtotal</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${orderItems}">
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <img src="${item.product.imageUrl}" class="me-3" style="width: 60px; height: 60px; object-fit: cover;" alt="${item.product.name}"/>
                                        <div>
                                            <div class="fw-bold">${item.product.name}</div>
                                            <small class="text-muted">${item.product.category}</small>
                                        </div>
                                    </div>
                                </td>
                                <td>Rs.${item.price}</td>
                                <td>${item.quantity}</td>
                                <td><strong>Rs.${item.price * item.quantity}</strong></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <!-- Total -->
                <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top">
                    <h5 class="mb-0">Total Amount:</h5>
                    <h4 class="mb-0 text-success">Rs.${totalAmount}</h4>
                </div>
            </div>
        </div>
    </div>

    <!-- Action Buttons -->
    <div class="text-center">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-lg btn-accent me-3">
            🛍️ Continue Shopping
        </a>
        <a href="${pageContext.request.contextPath}/orders" class="btn btn-lg btn-filter">
            📋 Check My Orders
        </a>
    </div>
</section>

<style>
.alert-lg {
    padding: 2rem;
    font-size: 1.2rem;
}
</style>
</body>
</html>
