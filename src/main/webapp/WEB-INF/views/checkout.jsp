<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Checkout | Techouts Mart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body class="bg-ink">
<nav class="navbar navbar-expand-lg nav-glass">
    <div class="container">
        <a class="navbar-brand brand" href="${pageContext.request.contextPath}/home">Techouts Mart</a>
        <div class="d-flex gap-2">
            <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/cart">Back to Cart</a>
            <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/orders">My Orders</a>
        </div>
    </div>
</nav>

<section class="container py-4">
    <h3 class="text-white mb-3">Checkout Summary - ${username}</h3>

    <c:if test="${not empty warning}"><div class="alert alert-warning">${warning}</div></c:if>

    <c:choose>
        <c:when test="${empty items}">
            <div class="empty-panel text-white">No items to checkout. Please add products to cart.</div>
        </c:when>
        <c:otherwise>
            <div class="panel">
                <table class="table table-dark table-hover align-middle mb-0">
                    <thead>
                    <tr>
                        <th>Product</th>
                        <th>Price</th>
                        <th>Qty</th>
                        <th>Subtotal</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${items}">
                        <tr>
                            <td>
                                <div class="d-flex align-items-center">
                                    <img src="${item.product.imageUrl}" class="me-2" style="width: 40px; height: 40px; object-fit: cover;" alt="${item.product.name}"/>
                                    <span>${item.product.name}</span>
                                </div>
                            </td>
                            <td>Rs.${item.product.price}</td>
                            <td>${item.quantity}</td>
                            <td>Rs.${item.product.price * item.quantity}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="panel mt-3">
                <h5 class="mb-3">Delivery & Payment</h5>
                <form method="post" action="${pageContext.request.contextPath}/checkout/place">
                    <input type="hidden" name="checkoutMode" value="${checkoutMode}"/>
                    <c:if test="${checkoutMode == 'DIRECT'}">
                        <input type="hidden" name="productId" value="${directProductId}"/>
                        <input type="hidden" name="quantity" value="${directQuantity}"/>
                    </c:if>

                    <div class="mb-3">
                        <label class="form-label">Shipping Address</label>
                        <textarea class="form-control" name="shippingAddress" rows="3" required>${userAddress}</textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Payment Method</label>
                        <select class="form-select" name="paymentMethod" required>
                            <option value="">Select payment method</option>
                            <option value="Cash On Delivery">Cash On Delivery</option>
                            <option value="UPI">UPI</option>
                            <option value="Card">Card</option>
                            <option value="Net Banking">Net Banking</option>
                        </select>
                    </div>

                    <div class="d-flex justify-content-between align-items-center">
                        <h5 class="m-0">Grand Total: Rs.${total}</h5>
                        <button class="btn btn-accent" type="submit">Place Order</button>
                    </div>
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</section>
</body>
</html>
