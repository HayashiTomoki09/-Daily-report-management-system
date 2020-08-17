<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:choose>
    <c:when test="${employee.delete_flag == 1}"></c:when>
    <c:when test="${employee.id == login_employee.id}"></c:when>
    <c:when test="${followChecks[status.index] == 0}">
        <form method="POST" action="<c:url value='/follows/create' />">
            <input type="hidden" name="emp_id" value="${employee.id}">
            <input type="hidden" name="page" value="${page}">
            <input type="hidden" name="pageName" value="employee">
            <input type="hidden" name="_token" value="${_token}" />
            <button type="submit">フォロー</button>
        </form>
    </c:when>
    <c:otherwise>
            <form method="POST" action="<c:url value='/follows/destroy' />">
            <input type="hidden" name="emp_id" value="${employee.id}">
            <input type="hidden" name="page" value="${page}">
            <input type="hidden" name="pageName" value="employee">
            <input type="hidden" name="_token" value="${_token}" />
            <button type="submit">解除</button>
        </form>
    </c:otherwise>
</c:choose>