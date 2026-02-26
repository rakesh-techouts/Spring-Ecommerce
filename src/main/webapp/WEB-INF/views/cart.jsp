<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Cart | Techouts Mart</title>
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
    <h3 class="text-white mb-3">${username}'s Cart</h3>

    <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
    <c:if test="${not empty warning}"><div class="alert alert-warning">${warning}</div></c:if>

    <c:choose>
        <c:when test="${empty items}">
            <div class="empty-panel text-white">Your cart is empty.</div>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <c:forEach var="item" items="${items}">
                    <div class="col-lg-4 col-md-6">
                        <div class="card product-card h-100">
                            <div class="product-image-wrap"><img src="${item.product.imageUrl}" class="card-img-top" alt="${item.product.name}"/></div>
                            <div class="card-body d-flex flex-column">
                                <div class="d-flex justify-content-between align-items-start mb-2">
                                    <span class="badge rounded-pill badge-category">${item.product.category}</span>
                                    <small class="text-muted">Stock: ${item.product.stock}</small>
                                </div>
                                <h5 class="card-title mb-2">${item.product.name}</h5>
                                <p class="card-text mb-3">${item.product.description}</p>

                                <div class="panel cart-info-panel mb-3">
                                    <div class="d-flex justify-content-between mb-1"><span>Price</span><strong>Rs.${item.product.price}</strong></div>
                                    <div class="d-flex justify-content-between mb-1"><span>Quantity</span><strong>${item.quantity}</strong></div>
                                    <div class="d-flex justify-content-between"><span>Sub Total</span><strong>Rs.${item.product.price * item.quantity}</strong></div>
                                </div>

                                <div class="d-flex align-items-center justify-content-between mt-auto">
                                    <div class="d-flex gap-1 align-items-center cart-qty-controls">
                                        <form method="post" action="${pageContext.request.contextPath}/cart/decrease">
                                            <input type="hidden" name="itemId" value="${item.id}"/>
                                            <input type="hidden" name="redirectTo" value="cart"/>
                                            <button class="btn btn-sm btn-filter" type="submit">-</button>
                                        </form>
                                        <span class="qty-pill">${item.quantity}</span>
                                        <form method="post" action="${pageContext.request.contextPath}/cart/increase">
                                            <input type="hidden" name="itemId" value="${item.id}"/>
                                            <input type="hidden" name="redirectTo" value="cart"/>
                                            <button class="btn btn-sm btn-accent" type="submit">+</button>
                                        </form>
                                    </div>
                                    <form method="post" action="${pageContext.request.contextPath}/cart/remove">
                                        <input type="hidden" name="itemId" value="${item.id}"/>
                                        <button class="btn btn-sm btn-danger" type="submit">Remove</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="panel mt-4 d-flex justify-content-between align-items-center">
                <div>
                    <h5 class="m-0">Grand Total: Rs.${total}</h5>
                    <small class="text-muted">${items.size()} item(s) in cart</small>
                </div>
                <a class="btn btn-checkout" href="${pageContext.request.contextPath}/checkout">Proceed to Checkout</a>
            </div>
        </c:otherwise>
    </c:choose>
</section>
</body>
</html>
