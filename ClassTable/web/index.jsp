<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/3/20
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>学号查询</title>
  </head>
  <body>
  <form action="${pageContext.request.contextPath}/SignUpServlet" method="post">
    <table width="60%" border="1">
      <tr>
        <td>我大哥叫啥</td>
        <td>
          <%--使用EL表达式${}提取存储在request对象中的formbean对象中封装的表单数据(formbean.userName)以及错误提示消息(formbean.errors.userName)--%>
          <input type="text" name="key">
        </td>
      </tr>
      <tr>
        <td>你学号多少</td>
        <td>
          <input type="password" name="stu_num" >
        </td>
      </tr>
      <tr>
        <td>
          <input type="reset" value="清空">
        </td>
        <td>
          <input type="submit" value="提交">
        </td>
      </tr>
    </table>
  </form>
  </body>
</html>
