package controller.attendance;

import java.io.IOException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Attendance;
import models.Employee;
import utils.DBUtil;


@WebServlet("/attendance/destroy")
public class AttendanceDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;




    public AttendanceDestroyServlet() {
        super();


    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Date date = new Date();
            Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");



            Attendance a = em.createNamedQuery("Attendance", Attendance.class)
                    .setParameter("report_date" ,date)
                    .setParameter("employee" ,login_employee)
                    .getSingleResult();

            a.setLeavetime(null);

            em.getTransaction().begin();
            em.persist(a);
            em.getTransaction().commit();
            em.close();


            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
