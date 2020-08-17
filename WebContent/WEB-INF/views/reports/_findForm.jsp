<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <div class="Find_form">
        <form action="<c:url value='/report_find' />">
        <label>レポート検索</label><br />
            <input type="text" name="findItem" value="${findItem}"/>
            <select name="findItemNum">
                <c:if test="${findItemNum != null}">
                    <option value="${findItemNum}">
                        <c:choose>
                            <c:when test="${findItemNum == 0}">社員名</c:when>
                            <c:when test="${findItemNum == 1}">タイトル</c:when>
                            <c:when test="${findItemNum == 2}">全日報表示</c:when>
                            <c:when test="${findItemNum == 3}">フォロー中</c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                    </option>
                </c:if>
                    <option value="0"<c:if test="${findItemNum == 0}"> selected</c:if>>社員名</option>
                    <option value="1"<c:if test="${findItemNum == 1}"> selected</c:if>>タイトル</option>
                    <option value="2"<c:if test="${findItemNum == 2}"> selected</c:if>>全日報表示</option>
                    <option value="3"<c:if test="${findItemNum == 3}"> selected</c:if>>フォロー中</option>
            </select>
            <button type="submit">検索</button>
            <div class="clear_both"></div>
            ※全日報表示とフォロー中は文字検索は無効
        </form>
    </div>