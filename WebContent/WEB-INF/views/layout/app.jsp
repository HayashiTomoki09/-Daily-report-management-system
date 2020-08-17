<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <title>日報管理システム</title>
        <link rel="stylesheet" href="<c:url value='/css/reset.css' />">
        <link rel="stylesheet" href="<c:url value='/css/style.css' />">
        <script>
        function js_alert() {
            alert("UserLogin ID;0000 Pass;user\nManagerLogin ID;9999 Pass;manager\n\n機能拡張\n・リアクション機能追加\n・レポート検索機能追加\n・社員間フォロー機能追加\n\nご自由に変更してみて下さい！！");
        }
    </script>
    </head>
    <body>
        <div id="wrapper">
            <div id="header">
                <div id="header_menu">
                    <h1><a href="<c:url value='/' />">日報管理システム</a></h1>&nbsp;&nbsp;&nbsp;
                    <c:if test="${sessionScope.login_employee != null}">
                        <c:choose>
                            <c:when test="${sessionScope.login_employee.admin_flag == 1}">
                                <a href="<c:url value='/employees/index' />">従業員管理</a>&nbsp;&nbsp;
                            </c:when>
                            <c:otherwise>
                                <a href="<c:url value='/employees/index' />">社員一覧</a>&nbsp;&nbsp;
                            </c:otherwise>
                        </c:choose>
                        <a href="<c:url value='/reports/index' />">日報管理</a>&nbsp;&nbsp;
                        <a href="<c:url value='/' />">自分の日報</a>&nbsp;&nbsp;
                    </c:if>
                </div>
                <c:if test="${sessionScope.login_employee != null}">
                    <div id="employee_name">
                        <c:out value="${sessionScope.login_employee.name}" />&nbsp;さん&nbsp;&nbsp;&nbsp;
                        <a href="<c:url value='/logout' />">ログアウト</a>
                    </div>
                </c:if>
            </div>
            <div id="content">
                ${param.content}
            </div>
            <div id="footer">
                by Tomoki Hayashi.
            </div>
        </div>
    </body>
</html>