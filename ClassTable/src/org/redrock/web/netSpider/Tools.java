package org.redrock.web.netSpider;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.list.SynchronizedList;
import org.redrock.web.java.ClassTable;
import org.redrock.web.java.ComparatorClassTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    public static String getUrl(String stu_num){
        String s="http://jwzx.cqupt.edu.cn/jwzxtmp/kebiao/kb_stu.php?xh="+stu_num;
        return s;
    }
    public static String getData(String URL) throws IOException {
        URL url = new URL(URL);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        InputStreamReader inputStream= new InputStreamReader(connection.getInputStream(),"UTF-8");
        BufferedReader bufferedReader=new BufferedReader(inputStream);
        StringBuffer stringBuffer =new StringBuffer();//反正没人用,这个线程安全感觉..没啥用
        String s=null;
        while ((s=bufferedReader.readLine())!=null){
            stringBuffer.append(s);
        }
        bufferedReader.close();
        connection.disconnect();
        return stringBuffer.toString();
    }

    /**这个方法先调用geturl得到url，然后会返回一个list容器，第0个返回的值是学期，第二个是学号，先把最基础的爬下来
     * 这玩意就是练手的，本来没必要单独列出的，完全可以优化没了的，但是。。懒
     * @param string
     * @return list
     * @throws IOException
     */
    public static List<String> getHead(String string) throws IOException {
        String data=getData(string);
        Pattern pattern=Pattern.compile("<li>〉〉(.+?)\\s+学生课表>>([0-9]{10})\\s+</li>");
        Matcher match=pattern.matcher(data);
        List<String>  head=new ArrayList<>();
        if (match.find()) {
            head.add(match.group(1));
            head.add(match.group(2));
        }else {
            head=null;
        }
        return head;
    }

    /**加工一下html页面。。原来那个太乱了。这样之后好看多了
     * @param url
     * @return String
     * @throws IOException
     */
    public static String getClassData(String url) throws IOException {
        String data=getData(url);
        Pattern pattern=Pattern.compile("<div id=\"kbStuTabs-list\">(.+?)<div\\s*id=\"kbStuTabs-ttk\">");
        Matcher matcher=pattern.matcher(data);
        if (matcher.find()){
            String s1=matcher.group(1);
            String s2=s1.replaceAll("\\s+","");
            Pattern pattern1=Pattern.compile("<tbody>(.*?)</tbody>");
            Matcher matcher1=pattern1.matcher(s2);
            if (matcher1.find()){
                s2=matcher1.group(1);
            }else {
                s2="第二步爬错了";
            }
            return s2;
        }else {
            return "第一步就爬错了";
        }
    }

    /**小爬一下，先把统一格式的课都爬下来，正则根本不敢多动
     * 其实吧这个方法也并没有什么用处，但是反正也不追求代码的效率，就懒得改了
     * 毕竟是用来练手的嘛
     * @param s
     * @return List<String>
     * @throws IOException
     */
    public static List<String> getOneKindClass(String s) throws IOException {
        String data=getClassData(s);
        List<String> strings=new ArrayList<>();
        Pattern pattern=Pattern.compile("<tr>(<tdrowspan.*?)</tr>");
        Matcher matcher=pattern.matcher(data);
        while (matcher.find()){
            strings.add(matcher.group(1));
        }
        return strings;
    }

//    这东西原来是准备优化速度的，但是有bug，懒得改了
// public static List<ClassTable> getOneKindClassInJSON(String s) throws IOException {
//        String data=getClassData(s);
//        List<ClassTable> list=new ArrayList<>();
//        //<tdrowspan='1'>A00172A1090020007</td><tdrowspan='1'>A1090020-大学体育2-羽毛球</td><tdrowspan='1'>必修</td><tdrowspan='1'align='center'>理论</td><tdrowspan='1'align='center'>正常</td><td>黄婧</td><td>星期3第3-4节1-16周</td><td>运动场1</td><tdrowspan='1'align='center'><ahref='kb_stuList.php?jxb=A00172A1090020007'target=_blank>名单</a></td><tdrowspan='1'></td>
//        Pattern pattern=Pattern.compile("<tdrowspan='[0-9]'>[A-Z0-9]+</td><tdrowspan='[0-9]'>[A-Z][0-9]+-(.*?)</td><tdrowspan='[0-9]'>(.*?)</td><tdrowspan='[0-9]'align='center'>.*?</td><tdrowspan='[0-9]'align='center'>.*?</td><td>(.*?)</td><td>(星期[1-5])(第.*?节)(.*?周)</td><td>(.*?)</td>.*?</td>");
//        Pattern pattern=Pattern.compile("<tdrowspan='[0-9]'>[A-Z0-9]+</td><tdrowspan='[0-9]'>[A-Z][0-9]+-(.*?)</td><tdrowspan='[0-9]'>(.*?)</td><tdrowspan='[0-9]'align='center'>.*?</td><tdrowspan='[0-9]'align='center'>.*?</td><td>(.*?)</td><td>(星期[1-5])(第.*?节)(.*?周)</td><td>(.*?)</td>.*</td>");
//        Pattern pattern=Pattern.compile("<tdrowspan='[0-9]'>[A-Z0-9]+</td><tdrowspan='[0-9]'>[A-Z][0-9]+-(.*?)</td><tdrowspan='[0-9]'>(.*?)</td><tdrowspan='[0-9]'align='center'>.*?</td><tdrowspan='[0-9]'align='center'>.*?</td><td>(.*?)</td><td>(星期[1-5])(第[1-10]-[1-10]节)(.*?周)</td><td>(.*?)</td>.*?</td>.*?</td>");
//        Matcher matcher=pattern.matcher(s);
//        while (matcher.find()){
//            int index=1;
//            ClassTable classTable=new ClassTable();
//            classTable.setCourse(matcher.group(index++));
//            classTable.setType(matcher.group(index++));
//            classTable.setTeacher(matcher.group(index++));
//            classTable.setDay(matcher.group(index++));
//            classTable.setLesson(matcher.group(index++));
//            classTable.setWeek(matcher.group(index++));
//            classTable.setClassroom(matcher.group(index++));
//            list.add(classTable);
//            }
//        return list;
//    }
public static List<ClassTable> getOneKindClassInClassTable(List<String> strings){
    List<ClassTable> list=new ArrayList<>();
    for (int i = 0; i <strings.size() ; i++) {
        //<tdrowspan='1'>A00172A1090020007</td><tdrowspan='1'>A1090020-大学体育2-羽毛球</td><tdrowspan='1'>必修</td><tdrowspan='1'align='center'>理论</td><tdrowspan='1'align='center'>正常</td><td>黄婧</td><td>星期3第3-4节1-16周</td><td>运动场1</td><tdrowspan='1'align='center'><ahref='kb_stuList.php?jxb=A00172A1090020007'target=_blank>名单</a></td><tdrowspan='1'></td>
        Pattern pattern=Pattern.compile("<tdrowspan='[0-9]'>[A-Z0-9]+</td><tdrowspan='[0-9]'>([A-Z][0-9]+)-(.*?)</td><tdrowspan='[0-9]'>(.*?)</td><tdrowspan='[0-9]'align='center'>.*?</td><tdrowspan='[0-9]'align='center'>.*?</td><td>(.*?)</td><td>(星期[1-5])(第.*?节)(.*?周)</td><td>(.*?)</td>.*</td>");
        //Pattern pattern=Pattern.compile("<tdrowspan='[0-9]'>[A-Z0-9]+</td><tdrowspan='[0-9]'>[A-Z][0-9]+-(.*?)</td><tdrowspan='[0-9]'>(.*?)</td><tdrowspan='[0-9]'align='center'>.*?</td><tdrowspan='[0-9]'align='center'>.*?</td><td>(.*?)</td><td>(星期[1-5])(第[1-10]-[1-10]节)(.*?周)</td><td>(.*?)</td>.*?</td>.*?</td>");
        Matcher matcher=pattern.matcher(strings.get(i));
        if (matcher.find()){
            int index=1;
            ClassTable classTable=new ClassTable();
            classTable.setClass_num(matcher.group(index++));
            classTable.setCourse(matcher.group(index++));
            classTable.setType(matcher.group(index++));
            classTable.setTeacher(matcher.group(index++));
            classTable.setDay(matcher.group(index++));
            classTable.setLesson(matcher.group(index++));
            classTable.setWeek(matcher.group(index++));
            classTable.setClassroom(matcher.group(index++));
            list.add(classTable);
        }
    }
    return list;
}
    public static List<ClassTable> getOtherKindClass(String s) throws IOException {
        String data=getClassData(s);
        List<ClassTable> list=new ArrayList<>();
        Pattern pattern=Pattern.compile("<tr><td>(.*?)</td><td>(星期[1-5])(第.*?节)(.*?周)</td><td>(.*?)</td></tr>");
        Matcher matcher=pattern.matcher(data);
        if (matcher.find()) {
            while (matcher.find()) {
                ClassTable classTable = new ClassTable();
                classTable.setTeacher(matcher.group(1));
                classTable.setDay(matcher.group(2));
                classTable.setLesson(matcher.group(3));
                classTable.setWeek(matcher.group(4));
                list.add(classTable);
            }
        }else {
            System.out.println("未找到");
        }
        return list;
    }

    /**
     * 还是想吐槽一下我这个代码写的贼鸡儿臃肿。。但是反正也。。救我一个人用嘛，就这样把
     * @param stu_num
     * @return
     * @throws IOException
     */
    public static JSONObject getAllDate(String stu_num) throws IOException {
        JSONObject json=new JSONObject();
        String url=getUrl(stu_num);
        List<String> strings = getOneKindClass(url);
        List<ClassTable> list_one = getOneKindClassInClassTable(strings);
        List<ClassTable> list_two = getOtherKindClass(url);
        for (int a = 0; a <list_two.size() ; a++) {
            for (int i = 0; i <list_one.size() ; i++) {
                if (list_two.get(a).getTeacher().equals(list_one.get(i).getTeacher())) {
                    list_two.get(a).setCourse(list_one.get(i).getCourse());
                    list_two.get(a).setClassroom(list_one.get(i).getClassroom());
                    list_two.get(a).setType(list_one.get(i).getType());
                    list_two.get(a).setClass_num(list_one.get(i).getClass_num());
                }
            }
        }
        list_one.addAll(list_two);
        sortList(list_one);
        JSONArray jsonArray=new JSONArray();
        for (ClassTable c:list_one){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("class_num",c.getClass_num());
            jsonObject.put("course",c.getCourse());
            jsonObject.put("type",c.getType());
            jsonObject.put("teacher",c.getTeacher());
            jsonObject.put("day",c.getDay());
            jsonObject.put("lesson",c.getLesson());
            jsonObject.put("week",c.getWeek());
            jsonObject.put("classroom",c.getClassroom());
            jsonArray.add(jsonObject);
        }
        json.put("status", 200);
        json.put("success", true);
        List<String> list=getHead(url);
        json.put("term",list.get(0));
        json.put("stu_num",list.get(1));
        json.put("data",jsonArray);
        return json;
    }
    public static void sortList(List<ClassTable> classTables){
        for (ClassTable classTable:classTables){
            String day=classTable.getDay().substring(2);
            classTable.setSort_day(Integer.parseInt(day));
            String lesson=classTable.getLesson().substring(1,2);
            classTable.setSort_lesson(Integer.parseInt(lesson));
        }
        ComparatorClassTable comparatorClassTable=new ComparatorClassTable();
        Collections.sort(classTables,comparatorClassTable);
    }
}
