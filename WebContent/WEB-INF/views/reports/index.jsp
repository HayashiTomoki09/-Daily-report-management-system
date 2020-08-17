<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>

        <h2 id="Find">日報　一覧</h2>
        <c:import url="_findForm.jsp" />
        <div class="clear_both"></div>
            <br /><br />
        <table id="report_list">
            <tbody>
                <tr>
                    <th class="report_name">氏名</th>
                    <th class="report_date">日付</th>
                    <th class="report_title">タイトル</th>
                    <th class="reaction_count">リアクション総数</th>
                    <th class="report_action">操作</th>
                    <th class="reaction_status">リアクション</th>
                    <th class="follow_status">フォロー</th>
                </tr>
                <c:forEach var="report" items="${reports}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="report_name"><c:out value="${report.employee.name}" /></td>
                        <td class="report_date"><fmt:formatDate value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
                        <td class="report_title">${report.title}</td>
                        <td class="reaction_count">${report_reactions[status.index]}</td>
                        <td class="report_action"><a href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>
                        <td class="reaction_status">
                            <c:choose>
                                <c:when test="${sessionScope.login_employee.id == report.employee.id}">自身の日報</c:when>
                                <c:when test="${reaction_status[status.index] == 0}">未投稿</c:when>
                                <c:when test="${reaction_status[status.index] == 1}"></c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                        </td>
                        <td class="follow_status">
                        <%-- <c:import url="../follows/_reportForm.jsp" /> ←で行うと全て解除が表示される --%>
                        <%@ include file="../follows/_reportForm.jsp"%>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${reports_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((reports_count - 1) / 15) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${findItemNum == null}">
                                <a href="<c:url value='/reports/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                            </c:when>
                            <c:otherwise>
                                <a href="<c:url value='/report_find?page=${i}&findItem=${findItem}&findItemNum=${findItemNum}' />">
                                <c:out value="${i}" />
                                </a>&nbsp;
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='/reports/new' />">新規日報の登録</a></p>

    </c:param>
</c:import>