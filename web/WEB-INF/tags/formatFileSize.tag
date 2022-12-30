<%@ tag body-content="empty" pageEncoding="utf-8" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="value" required="true" type="java.lang.Long" %>
<%
    long size = value;
    System.out.println(size);
    long result;
    String unit = "B";
    long kb = size / 1024;
    if (kb > 1024) {
        result = kb / 1024;
        unit = "MB";
    } else {
        result = kb;
        unit = "KB";
    }
%>
<%= result + unit %>