<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:choose>
            <c:when test="${report != null}">
                <h2>日報　詳細ページ</h2>

                <table>
                    <tbody>
                        <tr>
                            <th>氏名</th>
                            <td><c:out value="${report.employee.name}" /></td>
                        </tr>
                        <tr>
                            <th>日付</th>
                            <td><fmt:formatDate value="${report.report_date}" pattern="yyyy-MM-dd" /></td>
                        </tr>
                        <tr>
                            <th>内容</th>
                            <td>
                                <pre><c:out value="${report.content}" /></pre>
                            </td>
                        </tr>
                        <tr>
                            <th>登録日時</th>
                            <td>
                                <fmt:formatDate value="${report.created_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>更新日時</th>
                            <td>
                                <fmt:formatDate value="${report.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                        </tr>
                        <tr>
                            <th>リアクション総数</th>
                                <td>
                                        👍🏻&nbsp;<c:out value="${TypeCount0}" />&nbsp;&nbsp;&nbsp;
                                        👍🏼&nbsp;<c:out value="${TypeCount1}" />&nbsp;&nbsp;&nbsp;
                                        👍&nbsp;<c:out value="${TypeCount2}" />&nbsp;&nbsp;&nbsp;
                                        👍🏾&nbsp;<c:out value="${TypeCount3}" />&nbsp;&nbsp;&nbsp;
                                        👍🏿&nbsp;<c:out value="${TypeCount4}" />&nbsp;&nbsp;&nbsp;
                                </td>
                        </tr>
                        <tr>
                            <th>出退社時間</th>
                            <td>
                                <c:if test="${stayHour != null or stayMinute != null}">
                                    <fmt:formatDate value='${Attendance.cometime}' pattern='HH:mm' />　〜　<fmt:formatDate value='${Attendance.leavetime}' pattern='HH:mm' />
                                </c:if>

                            </td>
                        </tr>
                        <tr>
                            <th>在社時間</th>
                            <td>
                                <c:if test="${stayHour != null or stayMinute != null}">
                                    ${stayHour}　時間　　${stayMinute}　分
                                </c:if>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <c:choose>
                    <c:when test="${sessionScope.login_employee.id == report.employee.id}">
                        <p><a href="<c:url value="/reports/edit?id=${report.id}" />">この日報を編集する</a></p>
                    </c:when>
                    <c:otherwise>
                        <form method="POST" action="<c:url value='/create' />">
                            <c:import url="../reactions/_form.jsp" />
                            <input type="hidden" name="report_id" value="${report.id}" />
                        </form>
                    </c:otherwise>
                </c:choose>



                <h2>投稿に対するリアクション</h2>
                    <table id="reactions_list">
                        <tbody>
                            <tr>
                                <th class="reactionlist_type">種類</th>
                                <th class="reactionlist_message">メッセージ</th>
                                <th class="reactionlist_date">投稿日</th>
                                <th class="reactionlist_name">投稿者</th>
                            </tr>
                            <c:if test="${LoginReactionCount == 1}">
                                <tr class="row${status.count % 2}">
                                    <td class="reactionlist_type">
                                        <c:choose>
                                            <c:when test="${LoginReaction.type == 0}">👍🏻</c:when>
                                            <c:when test="${LoginReaction.type == 1}">👍🏼</c:when>
                                            <c:when test="${LoginReaction.type == 2}">👍</c:when>
                                            <c:when test="${LoginReaction.type == 3}">👍🏾</c:when>
                                            <c:when test="${LoginReaction.type == 4}">👍🏿</c:when>
                                        </c:choose>
                                    </td>
                                    <td class="reactionlist_message"><c:out value="${LoginReaction.message}" /></td>
                                    <td class="reactionlist_date"><fmt:formatDate value='${LoginReaction.created_at}' pattern='yyyy-MM-dd' /></td>
                                    <td class="reactionlist_name"><c:out value="${LoginReaction.employee.name}" /></td>
                                </tr>
                            </c:if>
                            <c:forEach var="reaction" items="${reactions}" varStatus="status">
                                <tr class="row${status.count % 2}">

                                    <td class="reactionlist_type">
                                        <c:choose>
                                            <c:when test="${reaction.type == 0}">👍🏻</c:when>
                                            <c:when test="${reaction.type == 1}">👍🏼</c:when>
                                            <c:when test="${reaction.type == 2}">👍</c:when>
                                            <c:when test="${reaction.type == 3}">👍🏾</c:when>
                                            <c:when test="${reaction.type == 4}">👍🏿</c:when>
                                        </c:choose>
                                    </td>
                                    <td class="reactionlist_message"><c:out value="${reaction.message}" /></td>
                                    <td class="reactionlist_date"><fmt:formatDate value='${reaction.created_at}' pattern='yyyy-MM-dd' /></td>
                                    <td class="reactionlist_name"><c:out value="${reaction.employee.name}" /></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div class="reactionbutton_delete">
                    <c:choose>
                        <c:when test="${LoginReactionCount == 0}">
                        </c:when>
                        <c:otherwise>
                            <form method="POST" action="<c:url value='/destroy' />">
                                <input type="hidden" name="report_id" value="${report.id}" />
                                <input type="hidden" name="_token" value="${_token}" />
                                <button type="submit" name="reactionbutton_delete">取消</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                    </div>
                    <div class="clear_both"></div>
                    <div id="pagination">
                        （全 ${reactions_count} 件）<br />
                        <c:forEach var="i" begin="1" end="${((reactions_count - 1) / 15) + 1}" step="1">
                            <c:choose>
                                <c:when test="${i == page}">
                                    <c:out value="${i}" />&nbsp;
                                </c:when>
                                <c:otherwise>
                                    <a href="<c:url value='/reports/show?page=${i}&id=${report.id}' />">
                                        <c:out value="${i}" />
                                    </a>&nbsp;
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
            </c:when>
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>
        </c:choose>

        <p><a href="<c:url value="/reports/index" />">一覧に戻る</a></p>
    </c:param>
</c:import>
