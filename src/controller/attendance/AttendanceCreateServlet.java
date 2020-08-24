package controller.attendance;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Attendance;
import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class AttendanceCreateServlet
 */
@WebServlet("/attendance/create")
public class AttendanceCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AttendanceCreateServlet() {
        super();
    }




    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();



            Attendance a = new Attendance();

            a.setEmployee_id((Employee)request.getSession().getAttribute("login_employee"));

            Date attendance_date = new Date(System.currentTimeMillis());
            String attendance_dt = request.getParameter("attendance_date");
            if(attendance_dt != null && !attendance_dt.equals("")) {
                attendance_date = Date.valueOf(request.getParameter("attendance_date"));
            }
            a.setAttendance_date(attendance_date);


            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            a.setCometime(currentTime);

            em.getTransaction().begin();
            em.persist(a);
            em.getTransaction().commit();
            em.close();


            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
