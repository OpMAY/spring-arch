<%@ tag body-content="empty" pageEncoding="utf-8" %>
<%@ tag import="java.text.NumberFormat" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="value" required="true" type="java.lang.Integer" %>
<%
    String formatted = "";
    if (value != null) {
        formatted = NumberFormat.getInstance().format(value);
    }
%>
<%= formatted %>