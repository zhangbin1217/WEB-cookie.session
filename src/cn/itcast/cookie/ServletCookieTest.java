package cn.itcast.cookie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 在服务器的Servlet判断是否有一个为lasttime的cookie
 *                 1.有：不是第一次访问
 *                     1.相应数据：欢饮回来，您上次访问的时间为2018年6月10日
 *                     2.写回cookie：lasttime=2018年6月10日
 *
 *                 2.没有：是第一次访问
 *                     1.相应数据：您好，欢迎您首次访问
 *                     2.写回cookie：lasttime=2018年6月10日
 *
 *
 */
@WebServlet("/servletCookieTest")
public class ServletCookieTest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置相应消息体的数据格式以及编码
        response.setContentType("text/html;charset=utf-8");
        boolean flag=false;//没有cookie为lastTime
        //获取所有cookie
        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for (Cookie cookie:cookies){
                String name = cookie.getName();
                if ("lastTime".equals(name)){

                    String value = cookie.getValue();
                    flag=true;
                    //设置cookie的value
                    //获取当前时间的字符串，重新设置Cookie的值，重新发送cookie
                    Date date=new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    String str_date = sdf.format(date);
                    //编码
                    str_date = URLEncoder.encode(str_date, "utf-8");
                    cookie.setValue(str_date);
                    response.addCookie(cookie);
                    //设置cookie的存活时间
                    cookie.setMaxAge(60*60*24*30);//一个月

                    value= URLDecoder.decode(value,"utf-8");
                    //不是第一次访问
                    response.getWriter().write("<h1>欢迎回来，您上次访问的时间为："+value+"</h1>");
                    break;
                }
            }
        }
        if (cookies==null||cookies.length==0||flag==false){
            //没有，第一次访问
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            String str_date = sdf.format(date);
            str_date=URLEncoder.encode(str_date,"utf-8");
            Cookie cookie = new Cookie("lastTime",str_date);
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            str_date=URLDecoder.decode(str_date,"utf-8");
            response.getWriter().write("<h1>您好，欢迎您首次访问,访问时间为："+str_date+"</h1>");

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
