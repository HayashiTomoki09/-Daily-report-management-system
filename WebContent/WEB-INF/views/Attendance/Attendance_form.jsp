<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<br /><br />
<h3>【出退社登録】</h3>
<br />
<table id="attendance_table">
            <tbody>
                <tr>
                    <th class="attendance_registrationOperation">登録操作</th>
                    <th class="attendance_cometime">出社時間</th>
                    <th class="attendance_leavetime">退社時間</th>
                    <th class="attendance_staytime">在社時間</th>
                </tr>
                <tr>
                    <td class="attendance_registrationOperation">
                        <c:choose>
                            <c:when test="${AttendancesCheck == 1 and Attendance.leavetime != null}">
                                <form method="POST" action="<c:url value='/attendance/destroy' />">
                                    <button type="submit">退社取消</button>
                                    <input type="hidden" name="_token" value="${_token}" />
                                </form>
                            </c:when>
                            <c:when test="${AttendancesCheck == 1}">
                                <form method="POST" action="<c:url value='/attendance/update' />">
                                    <button type="submit">退社</button>
                                    <input type="hidden" name="_token" value="${_token}" />
                                </form>
                            </c:when>
                            <c:when test="${AttendancesCheck == 0}">
                                <form method="POST" action="<c:url value='/attendance/create' />">
                                    <button type="submit">出社</button>
                                    <input type="hidden" name="_token" value="${_token}" />
                                </form>
                            </c:when>
                            <c:otherwise></c:otherwise>
                        </c:choose>
                    </td>
                    <td class="attendance_cometime">
                        <fmt:formatDate value='${Attendance.cometime}' pattern='HH:mm' />
                    </td>
                    <td class="attendance_leavetime">
                        <fmt:formatDate value='${Attendance.leavetime}' pattern='HH:mm' />
                    </td>
                    <td class="attendance_staytime">
                        <c:if test="${stayHour != null or stayMinute != null}">
                        ${stayHour}　時間　　${stayMinute}　分
                        </c:if>
                    </td>
                </tr>
            </tbody>
        </table>
<br /><br />
