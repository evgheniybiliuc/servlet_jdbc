<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<link rel="stylesheet" href="/styles/home.css">
<a id="logout" href="/logout">Logout</a>
<div id="myDIV" class="header">
    <h2>My To Do List</h2>
    <input type="text" id="myInput" placeholder="Title...">
    <span onclick="newElement()" class="addBtn">Add</span>
</div>

<ul id="myUL">
    <c:forEach items="${notes}" var="note">
        <li data-value="${note.id}" class="${note.status == "DONE" ? "checked" : ""}">${note.title}</li>
    </c:forEach>
    <%--<li class="checked">Pay bills</li>--%>
    <%--<li>Meet George</li>--%>
    <%--<li>Buy eggs</li>--%>
    <%--<li>Read a book</li>--%>
    <%--<li>Organize office</li>--%>
</ul>
<script src="/styles/js/home.js"></script>