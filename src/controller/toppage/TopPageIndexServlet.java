package controller.toppage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Attendance;
import models.Employee;
import models.Report;
import utils.DBUtil;

@WebServlet("/index.html")
public class TopPageIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public TopPageIndexServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }
        List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)
                                  .setParameter("employee", login_employee)
                                  .setFirstResult(15 * (page - 1))
                                  .setMaxResults(15)
                                  .getResultList();

        long reports_count = (long)em.createNamedQuery("getMyReportsCount", Long.class)
                                     .setParameter("employee", login_employee)
                                     .getSingleResult();


        Long[] reactions = new Long [(int)reports_count];

        int i = 0;
        for(Report r : reports){

            long reactions_Count = em.createNamedQuery("getReactionsCount", Long.class)
                    .setParameter("report_id" ,r)
                    .getSingleResult();

            reactions[i] = reactions_Count;
            i++;
        }


        Date date = new Date();
        //出退勤登録の有無確認
        long AttendancesCheck = em.createNamedQuery("today'sAttendancesCheck", Long.class)
                                  .setParameter("report_date" ,date)
                                  .setParameter("employee" ,login_employee)
                                  .getSingleResult();



        Attendance attendance = null;
        //出退勤情報取得
        if (AttendancesCheck != 0){
            attendance = em.createNamedQuery("Attendance", Attendance.class)
                           .setParameter("report_date" ,date)
                           .setParameter("employee" ,login_employee)
                           .getSingleResult();
        }


        em.close();



        //在社時間計算

        Integer stayHour = null;
        Integer stayMinute = null;


        if (AttendancesCheck != 0 && attendance.getLeavetime() != null){
            SimpleDateFormat cometimeHourString = new SimpleDateFormat("HH");
            SimpleDateFormat leavetimeHourString = new SimpleDateFormat("HH");
            SimpleDateFormat cometimeMinuteString = new SimpleDateFormat("mm");
            SimpleDateFormat leavetimeMinuteString = new SimpleDateFormat("mm");
            String cometimeHourSt = null;
            String leavetimeHourSt = null;
            String cometimeMinuteSt = null;
            String leavetimeMinuteSt = null;
            Integer cometimeHourInt = null;
            Integer leavetimeHourInt = null;
            Integer cometimeMinuteInt = null;
            Integer leavetimeMinuteInt = null;

            //Timestamp型→String型→Integer型に変換
            cometimeHourSt = cometimeHourString.format(attendance.getCometime());
            cometimeHourInt = Integer.parseInt(cometimeHourSt);

            leavetimeHourSt = leavetimeHourString.format(attendance.getLeavetime());
            leavetimeHourInt = Integer.parseInt(leavetimeHourSt);

            cometimeMinuteSt = cometimeMinuteString.format(attendance.getCometime());
            cometimeMinuteInt = Integer.parseInt(cometimeMinuteSt);

            leavetimeMinuteSt = leavetimeMinuteString.format(attendance.getLeavetime());
            leavetimeMinuteInt = Integer.parseInt(leavetimeMinuteSt);

            //在社時間計算
                //時間を分にする
            cometimeHourInt *= 60;
            leavetimeHourInt *= 60;

            cometimeMinuteInt += cometimeHourInt;
            leavetimeMinuteInt += leavetimeHourInt;
                //在社時間トータル分
            Integer stayHM = leavetimeMinuteInt - cometimeMinuteInt;
                //在社時間
            stayHour = stayHM / 60;
            stayMinute = stayHM % 60;
        }



        request.setAttribute("_token", request.getSession().getId());
        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("reactions", reactions);
        request.setAttribute("page", page);
        request.setAttribute("AttendancesCheck", AttendancesCheck);
        request.setAttribute("Attendance", attendance);
        request.setAttribute("stayHour", stayHour);
        request.setAttribute("stayMinute", stayMinute);

        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
    }
}
