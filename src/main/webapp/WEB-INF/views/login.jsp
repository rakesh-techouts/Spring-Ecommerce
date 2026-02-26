<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Login | Techouts Mart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body class="auth-body">
<div class="auth-card">
    <h3>Sign In</h3>
    <p class="text-muted">Use your account to continue shopping.</p>
    <a class="btn btn-sm btn-filter mb-3" href="${pageContext.request.contextPath}/">Home</a>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <c:if test="${not empty warning}">
        <div class="alert alert-warning">${warning}</div>
    </c:if>
    <c:if test="${param.registered == 'true'}">
        <div class="alert alert-success">Registration successful. Please login.</div>
    </c:if>
    <c:if test="${param.logout == 'true'}">
        <div class="alert alert-info">You have been logged out.</div>
    </c:if>
    <c:if test="${param.authRequired == 'true'}">
        <div class="alert alert-warning">Please login first to continue.</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="mb-3">
            <label class="form-label">Username or Email</label>
            <input class="form-control" type="text" name="identity" required/>
        </div>
        <div class="mb-3">
            <label class="form-label">Password</label>
            <input class="form-control" type="password" name="password" required/>
        </div>
        <button class="btn btn-accent w-100" type="submit">Login</button>
    </form>

    <p class="mt-3 mb-0">No account? <a href="${pageContext.request.contextPath}/register">Create one</a></p>
</div>
</body>
</html>
