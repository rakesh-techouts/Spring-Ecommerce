<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>My Profile | Techouts Mart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body class="auth-body">
<div class="auth-card">
    <h3>My Profile</h3>
    <p class="text-muted">Manage your account information.</p>
    <a class="btn btn-sm btn-filter mb-3" href="${pageContext.request.contextPath}/home">Back to Home</a>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <c:if test="${not empty warning}">
        <div class="alert alert-warning">${warning}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/profile">
        <div class="mb-3">
            <label class="form-label">Username</label>
            <input class="form-control" name="username" type="text" value="${user.username}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Email</label>
            <input class="form-control" name="email" type="email" value="${user.email}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Phone Number</label>
            <input class="form-control" name="phone" type="tel" value="${user.phone}" required 
                   pattern="[6-9][0-9]{9}" maxlength="10" 
                   placeholder="Enter 10-digit mobile number">
            <small class="text-muted">10-digit mobile number starting with 6-9</small>
        </div>
        <div class="mb-3">
            <label class="form-label">Address</label>
            <textarea class="form-control" name="address" rows="3" placeholder="Enter your address">${user.address}</textarea>
        </div>
        <button class="btn btn-accent w-100" type="submit">Update Profile</button>
    </form>

    <div class="mt-3">
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm w-100">Logout</a>
    </div>
</div>
</body>
</html>
