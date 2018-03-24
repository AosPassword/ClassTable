<%@ page import="net.sf.json.JSONObject" %>
<%@ page import="net.sf.json.JSONArray" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/3/23
  Time: 14:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--未完成的jsp，懒癌来了真的神仙都救不了，我去看看springmvc算了--%>

<html>
<head>
    <title>class_table</title>
</head>
<body>
<%
    JSONObject jsonObject= (JSONObject) request.getSession().getAttribute("class_table");
    JSONArray jsonArray=jsonObject.getJSONArray("data");
%>
<table>
    <thead>
    <tr>
        <td>课程号</td>
        <td>课程名</td>
        <td>类别</td>
        <td>教师</td>
        <td>上课时间</td>
        <td>地点</td>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>A1090020-大学体育2-羽毛球</td>
        <td>必修</td>
        <td>理论</td>
        <td rowspan='1' align='center'>正常 </td>
        <td>黄婧</td>
        <td>星期3第3-4节 1-16周</td>
        <td>运动场1</td>
    </tr>
</body>
</html>
