<%@ tag body-content="empty" pageEncoding="utf-8" %>
<%@ tag import="com.util.TimeFormatter" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="value" required="true" type="java.lang.String" %>
<%= TimeFormatter.StringDateFormatDayOfWeek(value) %>