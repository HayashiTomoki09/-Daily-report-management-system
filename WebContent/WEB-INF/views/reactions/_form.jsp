<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h2>リアクションする</h2>
<c:if test="${flush_reaction != null}">
    <div id="flush_success">
        <c:out value="${flush_reaction}"></c:out>
    </div>
</c:if>
<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br />
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br />
        </c:forEach>
    </div>
</c:if>
<table id="reaction_form">
    <tbody>
        <tr>
            <th class="reactionType">リアクション種類</th>
            <th class="reactionComent">コメント</th>
        </tr>
        <tr>
            <td>
                <select name="reaction_type">
                    <option value="0"<c:if test="${Reaction.Type ==0}"> selected</c:if>>👍🏻</option>
                    <option value="1"<c:if test="${Reaction.Type ==1}"> selected</c:if>>👍🏼</option>
                    <option value="2"<c:if test="${Reaction.Type ==2}"> selected</c:if>>👍</option>
                    <option value="3"<c:if test="${Reaction.Type ==3}"> selected</c:if>>👍🏾</option>
                    <option value="4"<c:if test="${Reaction.Type ==4}"> selected</c:if>>👍🏿</option>
                </select>
            </td>
            <td>
                <input type="text" name="reaction_coment" size="60" maxlength="100" value="">
                <br /><br />
            </td>
        </tr>
    </tbody>
</table>
<input type="hidden" name="_token" value="${_token}" />


<c:choose>
    <c:when test="${LoginReactionCount == 1}">
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${flush_reaction != null}">
                <div class="reactionbutton">
                    <button type="submit" name="reactionbutton">投稿</button>
                </div>
            </c:when>
            <c:otherwise>
                <div class="reactionbutton_1">
                    <button type="submit" name="reactionbutton">投稿</button>
                </div>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>

<div class="clear_both"></div>


