package controllers.follows;

import java.io.IOException;
import java.net.URLEncoder;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;


@WebServlet("/follows/create")
public class followsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public followsCreateServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /*
        getWriterメソッドを使って確認可能
        response.setContentType("text/html; charset=UTF-8");
        String URL = null;
        URL = "/reports/index?page=" +  page + "&findItem=" + findItem + "&findItemNum=" + findItemNum;
        PrintWriter out = response.getWriter();
        out.println(request.getContextPath() + URL);
        */


        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Follow f = new Follow();
            Employee a = em.find(Employee.class, Integer.parseInt(request.getParameter("emp_id")));


            f.setFollowed_id(a);
            f.setFollower_id((Employee)request.getSession().getAttribute("login_employee"));

            em.getTransaction().begin();
            em.persist(f);
            em.getTransaction().commit();
            em.close();

            Integer page = Integer.parseInt(request.getParameter("page"));
            String pageName = request.getParameter("pageName");
            /*Integer findItemNum =  Integer.parseInt(request.getParameter("findItemNum"));
            String findItem = (String) request.getSession().getAttribute("SessionfindItem");
            String URL = null;*/


            if(pageName.equals("employee")){//社員一覧に戻る
                    response.sendRedirect(request.getContextPath() + "/employees/index?page=" + page);

            }else if(!(request.getParameter("findItemNum").equals(""))){//日報一覧（検索機能保持）に戻る
                Integer findItemNum =  Integer.parseInt(request.getParameter("findItemNum"));
                String findItem = (String) request.getSession().getAttribute("SessionfindItem");
                String URL = null;

                String encodedString = URLEncoder.encode(findItem, "UTF-8");//2バイト文字はURLエンコードする必要がある　URLEncoder.encode("表示する文字列", "UTF-8");
                URL = "/report_find?page=" +  page + "&findItem=" + encodedString + "&findItemNum=" + findItemNum;
                response.sendRedirect(request.getContextPath() + URL);

            }else{//日報一覧に戻る
                response.sendRedirect(request.getContextPath() + "/reports/index?page=" + page);
            }
        }
    }
}
