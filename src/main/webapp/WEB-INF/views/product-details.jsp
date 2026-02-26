<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Product Details | Techouts Mart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body class="bg-ink">
<nav class="navbar navbar-expand-lg nav-glass">
    <div class="container">
        <a class="navbar-brand brand" href="${pageContext.request.contextPath}/">Techouts Mart</a>
        <div class="d-flex gap-2">
            <c:choose>
                <c:when test="${isLoggedIn}">
                    <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/home">Home</a>
                    <a class="btn btn-sm btn-cart" href="${pageContext.request.contextPath}/cart">Cart (${cartCount})</a>
                    <a class="btn btn-sm btn-logout" href="${pageContext.request.contextPath}/logout">Logout</a>
                </c:when>
                <c:otherwise>
                    <a class="btn btn-sm btn-accent" href="${pageContext.request.contextPath}/login">Login</a>
                    <a class="btn btn-sm btn-filter" href="${pageContext.request.contextPath}/register">Register</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>

<section class="container py-4">
    <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>
    <c:if test="${not empty warning}"><div class="alert alert-warning">${warning}</div></c:if>

    <div class="panel">
        <div class="row g-4 align-items-start">
            <div class="col-lg-5">
                <div class="product-image-wrap product-detail-image-wrap">
                    <img src="${product.imageUrl}" alt="${product.name}" class="product-detail-image"/>
                </div>
            </div>
            <div class="col-lg-7">
                <span class="badge rounded-pill badge-category mb-2">${product.category}</span>
                <h2 class="mb-2">${product.name}</h2>
                <p class="mb-3">${product.description}</p>
                <div class="mb-2"><strong>Price:</strong> Rs.${product.price}</div>
                <div class="mb-3"><strong>Available Stock:</strong> ${product.stock}</div>

                <c:choose>
                    <c:when test="${isLoggedIn}">
                        <div class="d-flex flex-wrap gap-2">
                            <form method="post" action="${pageContext.request.contextPath}/cart/add" class="d-flex gap-2">
                                <input type="hidden" name="productId" value="${product.id}"/>
                                <input type="hidden" name="redirectTo" value="product"/>
                                <input type="hidden" name="redirectProductId" value="${product.id}"/>
                                <input type="number" name="quantity" min="1" value="1" class="form-control" required/>
                                <button class="btn btn-accent" type="submit">Add to Cart</button>
                            </form>

                            <form method="post" action="${pageContext.request.contextPath}/buy-now" class="d-flex gap-2">
                                <input type="hidden" name="productId" value="${product.id}"/>
                                <input type="number" name="quantity" min="1" value="1" class="form-control" required/>
                                <button class="btn btn-checkout" type="submit">Buy Now</button>
                            </form>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-info">Please login if you want to buy this product.</div>
                        <a class="btn btn-accent" href="${pageContext.request.contextPath}/login">Login to Buy</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</section>
</body>
</html>
