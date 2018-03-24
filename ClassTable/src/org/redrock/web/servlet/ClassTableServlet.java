package org.redrock.web.servlet;

import net.sf.json.JSONObject;
import org.redrock.web.netSpider.Tools;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 懒癌又犯了，哎，不想动就去看一看spring MVC去吧
 * postman填两个参数，key 填我大哥 stu_num填学号
 */
@WebServlet("/web")
public class ClassTableServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        System.out.println(req.getParameter("key"));
        System.out.println(req.getParameter("stu_num"));
        JSONObject jsonObject=new JSONObject();
        try {
            String key = (String) req.getParameter("key");
            String stu_num = (String) req.getParameter("stu_num");
            if (key.equals("胡仓")||key.equals("李晨铭")) {
                Tools tools = new Tools();
                jsonObject = tools.getAllDate(stu_num);
            }else {
                jsonObject.put("error","你连我大哥都不知道，还想用我写的方法？");
//              resp.sendRedirect(req.getContextPath() + "/index.jsp");
            }
        }catch (Exception e) {
            jsonObject.put("error", "没参数！！");
        }
        resp.getWriter().write(jsonObject.toString());
//        resp.sendRedirect(req.getContextPath() + "/table.jsp");
    }
}
