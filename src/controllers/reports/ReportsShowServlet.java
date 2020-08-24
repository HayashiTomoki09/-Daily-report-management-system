package controllers.reports;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
import models.Reaction;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsShowServlet
 */
@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();


        Report r = em.find(Report.class,Integer.parseInt(request.getParameter("id")));
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");


        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }

        List<Reaction> reactions = em.createNamedQuery("getReactionsAll", Reaction.class)
                .setParameter("report_id", r)
                .setParameter("logid", login_employee)
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();

        long reactions_count = (long)em.createNamedQuery("getReactionsCount", Long.class)
                                          .setParameter("report_id", r)
                                          .getSingleResult();



        //リアクションタイプカウント

            long TypeCount0 = (long)em.createNamedQuery("getReactionsTypeCount", Long.class)
                    .setParameter("typeNum" ,0)
                    .setParameter("report_id", r)
                    .getSingleResult();
            long TypeCount1 = (long)em.createNamedQuery("getReactionsTypeCount", Long.class)
                    .setParameter("typeNum" ,1)
                    .setParameter("report_id", r)
                    .getSingleResult();
            long TypeCount2 = (long)em.createNamedQuery("getReactionsTypeCount", Long.class)
                    .setParameter("typeNum" ,2)
                    .setParameter("report_id", r)
                    .getSingleResult();
            long TypeCount3 = (long)em.createNamedQuery("getReactionsTypeCount", Long.class)
                    .setParameter("typeNum" ,3)
                    .setParameter("report_id", r)
                    .getSingleResult();
            long TypeCount4 = (long)em.createNamedQuery("getReactionsTypeCount", Long.class)
                    .setParameter("typeNum" ,4)
                    .setParameter("report_id", r)
                    .getSingleResult();

            long LoginReactionCount = (long)em.createNamedQuery("getLoginReactionCount", Long.class)
                    .setParameter("logid",login_employee)
                    .setParameter("report_id", r)
                    .getSingleResult();


            if(LoginReactionCount == 1){
                Reaction LoginReaction = em.createNamedQuery("getLoginReaction", Reaction.class)
                        .setParameter("logid",login_employee)
                        .setParameter("report_id", r)
                        .getSingleResult();

                request.getSession().setAttribute("logid", LoginReaction.getId());
                request.setAttribute("LoginReaction", LoginReaction);
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



        request.setAttribute("report", r);
        request.setAttribute("reactions", reactions);
        request.setAttribute("reactions_count", reactions_count);
        request.setAttribute("TypeCount0", TypeCount0);
        request.setAttribute("TypeCount1", TypeCount1);
        request.setAttribute("TypeCount2", TypeCount2);
        request.setAttribute("TypeCount3", TypeCount3);
        request.setAttribute("TypeCount4", TypeCount4);
        request.setAttribute("page", page);
        request.setAttribute("LoginReactionCount", LoginReactionCount);
        request.setAttribute("Attendance", attendance);
        request.setAttribute("stayHour", stayHour);
        request.setAttribute("stayMinute", stayMinute);
        request.setAttribute("_token", request.getSession().getId());


        if(request.getSession().getAttribute("flush_reaction") != null) {
            request.setAttribute("flush_reaction", request.getSession().getAttribute("flush_reaction"));
            request.getSession().removeAttribute("flush_reaction");
        }


        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);
    }

    public List<String> stringToList(String str) {
        return Arrays.asList(str.split(","));
      }
}

