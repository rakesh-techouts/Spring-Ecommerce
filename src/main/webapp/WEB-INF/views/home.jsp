<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Home | Techouts Mart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20260302"/>
</head>
<body class="bg-ink">
<nav class="navbar navbar-expand-lg nav-glass">
    <div class="container">
        <a class="navbar-brand brand" href="${pageContext.request.contextPath}/home">Techouts Mart</a>
        <div class="d-flex gap-2">
            <a class="btn btn-sm btn-cart" href="${pageContext.request.contextPath}/cart">Cart (${cartCount})</a>
            <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/orders">My Orders</a>
            <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/profile">My Profile</a>
            <a class="btn btn-sm btn-logout" href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </div>
</nav>

<section class="container py-4">
    <div class="section-head mb-3">
        <h3 class="mb-0">Welcome, ${username}</h3>
        <div class="btn-group">
            <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/home?category=All">All</a>
            <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/home?category=Laptop">Laptop</a>
            <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/home?category=Mobile">Mobile</a>
            <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/home?category=Buds">Buds</a>
        </div>
    </div>

    <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
    <c:if test="${not empty warning}"><div class="alert alert-warning">${warning}</div></c:if>

    <c:if test="${not empty cartItems}">
        <div class="panel mb-4">
            <div class="d-flex justify-content-between align-items-center">
                <h5 class="m-0">Quick Cart</h5>
                <div class="d-flex gap-2">
                    <form method="post" action="${pageContext.request.contextPath}/cart/clear" class="d-inline">
                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to empty your cart?')">Make Empty Cart</button>
                    </form>
                    <a class="btn btn-sm btn-checkout" href="${pageContext.request.contextPath}/checkout">Checkout</a>
                </div>
            </div>
            <div class="mt-2">Items in cart: ${cartCount}</div>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${category == 'All'}">
            <h4 class="mt-2">Laptops</h4>
            <div class="row g-4 mt-1">
                <c:forEach var="p" items="${laptops}">
                    <div class="col-lg-4 col-md-6">
                        <div class="card product-card h-100">
                            <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="text-decoration-none">
                                <div class="product-image-wrap product-list-image-wrap"><img src="${p.imageUrl}" class="product-list-image" alt="${p.name}"/></div>
                            </a>
                            <div class="card-body d-flex flex-column">
                                <span class="badge rounded-pill badge-category mb-2">${p.category}</span>
                                <h5 class="card-title mb-2"><a class="text-decoration-none" href="${pageContext.request.contextPath}/product?id=${p.id}">${p.name}</a></h5>
                                <div class="mt-auto d-flex justify-content-between align-items-center mb-2"><strong class="price-tag">Rs.${p.price}</strong><small class="text-muted">Stock: ${p.stock}</small></div>
                                <a class="btn btn-sm btn-accent" href="${pageContext.request.contextPath}/product?id=${p.id}">View Details</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <h4 class="mt-4">Mobiles</h4>
            <div class="row g-4 mt-1">
                <c:forEach var="p" items="${mobiles}">
                    <div class="col-lg-4 col-md-6">
                        <div class="card product-card h-100">
                            <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="text-decoration-none">
                                <div class="product-image-wrap product-list-image-wrap"><img src="${p.imageUrl}" class="product-list-image" alt="${p.name}"/></div>
                            </a>
                            <div class="card-body d-flex flex-column">
                                <span class="badge rounded-pill badge-category mb-2">${p.category}</span>
                                <h5 class="card-title mb-2"><a class="text-decoration-none" href="${pageContext.request.contextPath}/product?id=${p.id}">${p.name}</a></h5>
                                <div class="mt-auto d-flex justify-content-between align-items-center mb-2"><strong class="price-tag">Rs.${p.price}</strong><small class="text-muted">Stock: ${p.stock}</small></div>
                                <a class="btn btn-sm btn-accent" href="${pageContext.request.contextPath}/product?id=${p.id}">View Details</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <h4 class="mt-4">Buds</h4>
            <div class="row g-4 mt-1">
                <c:forEach var="p" items="${buds}">
                    <div class="col-lg-4 col-md-6">
                        <div class="card product-card h-100">
                            <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="text-decoration-none">
                                <div class="product-image-wrap product-list-image-wrap"><img src="${p.imageUrl}" class="product-list-image" alt="${p.name}"/></div>
                            </a>
                            <div class="card-body d-flex flex-column">
                                <span class="badge rounded-pill badge-category mb-2">${p.category}</span>
                                <h5 class="card-title mb-2"><a class="text-decoration-none" href="${pageContext.request.contextPath}/product?id=${p.id}">${p.name}</a></h5>
                                <div class="mt-auto d-flex justify-content-between align-items-center mb-2"><strong class="price-tag">Rs.${p.price}</strong><small class="text-muted">Stock: ${p.stock}</small></div>
                                <a class="btn btn-sm btn-accent" href="${pageContext.request.contextPath}/product?id=${p.id}">View Details</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <c:if test="${empty buds}"><div class="col-12"><div class="empty-panel">No buds available currently.</div></div></c:if>
            </div>
        </c:when>

        <c:otherwise>
            <h4 class="mt-2">${category}</h4>
            <div class="row g-4 mt-1">
                <c:forEach var="p" items="${products}">
                    <div class="col-lg-4 col-md-6">
                        <div class="card product-card h-100">
                            <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="text-decoration-none">
                                <div class="product-image-wrap product-list-image-wrap"><img src="${p.imageUrl}" class="product-list-image" alt="${p.name}"/></div>
                            </a>
                            <div class="card-body d-flex flex-column">
                                <span class="badge rounded-pill badge-category mb-2">${p.category}</span>
                                <h5 class="card-title mb-2"><a class="text-decoration-none" href="${pageContext.request.contextPath}/product?id=${p.id}">${p.name}</a></h5>
                                <div class="mt-auto d-flex justify-content-between align-items-center mb-2"><strong class="price-tag">Rs.${p.price}</strong><small class="text-muted">Stock: ${p.stock}</small></div>
                                <a class="btn btn-sm btn-accent" href="${pageContext.request.contextPath}/product?id=${p.id}">View Details</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<footer class="site-footer mt-4">
    <div class="container py-4">
        <div class="footer-top">
            <div class="footer-brand-col">
                <p class="footer-chip mb-2">Member Dashboard</p>
                <h5 class="footer-brand mb-2">Techouts Mart</h5>
                <p class="footer-text mb-0">Fast, secure, and reliable shopping for your daily tech needs with smooth order tracking.</p>
            </div>
            <div class="footer-links-col">
                <h6 class="footer-title">Shop</h6>
                <div class="footer-links">
                    <a href="${pageContext.request.contextPath}/home">Home</a>
                    <a href="${pageContext.request.contextPath}/home?category=Laptop">Laptops</a>
                    <a href="${pageContext.request.contextPath}/home?category=Mobile">Mobiles</a>
                    <a href="${pageContext.request.contextPath}/home?category=Buds">Buds</a>
                </div>
            </div>
            <div class="footer-links-col">
                <h6 class="footer-title">Account</h6>
                <div class="footer-links">
                    <a href="${pageContext.request.contextPath}/cart">Cart</a>
                    <a href="${pageContext.request.contextPath}/checkout">Checkout</a>
                    <a href="${pageContext.request.contextPath}/orders">My Orders</a>
                    <a href="${pageContext.request.contextPath}/profile">My Profile</a>
                </div>
            </div>
        </div>
        <div class="footer-bottom mt-3">
            <small>&copy; 2026 Techouts Mart. All rights reserved.</small>
            <div class="footer-mini-links">
                <span>Verified Sellers</span>
                <span>Safe Checkout</span>
                <span>Quick Returns</span>
            </div>
        </div>
    </div>
</footer>
</body>
</html>
