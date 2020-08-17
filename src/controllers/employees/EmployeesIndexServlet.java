package controllers.employees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;


@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public EmployeesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        int page = 1;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) { }


        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        List<Employee> employees ;
        long employees_count ;

        //管理者ID　削除済み社員取得
        if(login_employee.getAdmin_flag() == 1){
            employees = em.createNamedQuery("getAllEmployees", Employee.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            employees_count = em.createNamedQuery("getAllEmployeesCount", Long.class)
                                 .getSingleResult();

        }else{
        //ユーザーID　削除済み社員取得しない
            employees = em.createNamedQuery("getEmployeesDelete0", Employee.class)
                    .setFirstResult(15 * (page - 1))
                    .setMaxResults(15)
                    .getResultList();

            employees_count = em.createNamedQuery("getAllEmployeesCount", Long.class)
                                .getSingleResult();
        }


        Long[] followChecks = new Long [employees.size()];
        Long[] reportCount = new Long [employees.size()];
        Long[] reactionCount = new Long [employees.size()];
        Long[] followerCount = new Long [employees.size()];


      //followCheck
        int i = 0;
        for(Employee e : employees){

            long Follow = (long)em.createNamedQuery("followedCheck", Long.class)
                    .setParameter("employee" ,e)
                    .setParameter("logid" ,login_employee)
                    .getSingleResult();

            followChecks[i] = Follow;
            i++;
        }

        //reportsCount
        int j = 0;
        for(Employee e : employees){

            long myReportCount = (long)em.createNamedQuery("getMyReportsCount", Long.class)
                    .setParameter("employee" ,e)
                    .getSingleResult();

            reportCount[j] = myReportCount;
            j++;
        }


      //ReactionCount
        int k = 0;
        for(Employee e : employees){

            long getReactionCount = (long)em.createNamedQuery("getEmployeeReactionsCount", Long.class)
                    .setParameter("employee" ,e)
                    .getSingleResult();

            reactionCount[k] = getReactionCount;
            k++;
        }


      //FollowerCount
        int l = 0;
        for(Employee e : employees){

            long getFollowerCount = (long)em.createNamedQuery("getFollowerCount", Long.class)
                    .setParameter("employee" ,e)
                    .getSingleResult();

            followerCount[l] = getFollowerCount;
            l++;
        }




        em.close();
        request.setAttribute("_token", request.getSession().getId());
        request.setAttribute("login_employee", login_employee);
        request.setAttribute("employees", employees);
        request.setAttribute("employees_count", employees_count);
        request.setAttribute("page", page);
        request.setAttribute("followChecks", followChecks);
        request.setAttribute("reportCount", reportCount);
        request.setAttribute("reactionCount", reactionCount);
        request.setAttribute("followerCount", followerCount);


        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        rd.forward(request, response);
    }
}
