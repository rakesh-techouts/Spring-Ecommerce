<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Register | Techouts Mart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body class="auth-body">
<div class="auth-card">
    <h3>Create Account</h3>
    <p class="text-muted">Start your shopping journey.</p>
    <a class="btn btn-sm btn-filter mb-3" href="${pageContext.request.contextPath}/">Home</a>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <c:if test="${not empty warning}">
        <div class="alert alert-warning">${warning}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/register">
        <div class="mb-3">
            <label class="form-label">Name</label>
            <input class="form-control" name="username" type="text" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Email</label>
            <input class="form-control" name="email" type="email" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Password</label>
            <input class="form-control" name="password" type="password" required>
        </div>
        <button class="btn btn-accent w-100" type="submit">Register</button>
    </form>

    <p class="mt-3 mb-0">Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a></p>
</div>
</body>
</html>
