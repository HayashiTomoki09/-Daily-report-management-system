package controllers.reports;

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
import models.Report;
import utils.DBUtil;

@WebServlet("/report_find")
public class ReportFindServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ReportFindServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");
        Integer findItemNum =  Integer.parseInt(request.getParameter("findItemNum"));
        String findItem =request.getParameter("findItem");


        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }



        List<Report> reports = null;
        long reports_count = 0;

        switch(findItemNum){
        case 0://社員名選択時
            reports = em.createNamedQuery("getFindReportsName", Report.class)
                        .setParameter("name", "%" + findItem + "%")
                        .setFirstResult(15 * (page - 1))
                        .setMaxResults(15)
                        .getResultList();

            reports_count = em.createNamedQuery("getFindReportsNameCount", Long.class)
                              .setParameter("name", "%" + findItem + "%")
                              .getSingleResult();

            break;


        case 1://タイトル選択時
            reports = em.createNamedQuery("getFindReportsTitle", Report.class)
                        .setParameter("name", "%" + findItem + "%")
                        .setFirstResult(15 * (page - 1))
                        .setMaxResults(15)
                        .getResultList();

            reports_count = em.createNamedQuery("getFindReportsTitleCount", Long.class)
                              .setParameter("name", "%" + findItem + "%")
                              .getSingleResult();

            break;


        case 2://全日報選択時
            reports = em.createNamedQuery("getAllReports", Report.class)
                        .setFirstResult(15 * (page - 1))
                        .setMaxResults(15)
                        .getResultList();

            reports_count = em.createNamedQuery("getReportsCount", Long.class)
                              .getSingleResult();

            findItem = "";
            break;


        case 3://フォロー中選択時
            reports = em.createNamedQuery("getFindReportsFollow", Report.class)
                        .setParameter("logid", login_employee)
                        .setFirstResult(15 * (page - 1))
                        .setMaxResults(15)
                        .getResultList();

            reports_count = em.createNamedQuery("getFindReportsFollowCount", Long.class)
                              .setParameter("logid", login_employee)
                              .getSingleResult();

            findItem = "";
            break;
        }



        //リアクション数、リアクションステータス、フォローチェック確認処理
        Long[] report_reactions = new Long [reports.size()];
        Long[] reaction_status = new Long [reports.size()];
        Long[] followChecks = new Long [reports.size()];

        int i = 0;
        for(Report r : reports){

            long reactions = em.createNamedQuery("getReactionsCount", Long.class)
                    .setParameter("report_id" ,r)
                    .getSingleResult();

            report_reactions[i] = reactions;
            i++;
        }

        int j = 0;
        for(Report r : reports){

            long reaction_getstatus = em.createNamedQuery("getLoginReactionCount", Long.class)
                    .setParameter("report_id" ,r)
                    .setParameter("logid" ,login_employee)
                    .getSingleResult();

            reaction_status[j] = reaction_getstatus;
            j++;
        }

        int k = 0;
        for(Report r : reports){

            long Follow = (long)em.createNamedQuery("followedCheck", Long.class)
                    .setParameter("employee" ,r.getEmployee())
                    .setParameter("logid" ,login_employee)
                    .getSingleResult();

            followChecks[k] = Follow;
            k++;
        }

        em.close();



        request.setAttribute("_token", request.getSession().getId());
        request.setAttribute("login_employee", login_employee);
        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("report_reactions", report_reactions);
        request.setAttribute("reaction_status", reaction_status);
        request.setAttribute("reactions", reports);
        request.setAttribute("page", page);
        request.setAttribute("findItem", findItem);
        request.setAttribute("findItemNum", findItemNum);
        request.setAttribute("followChecks", followChecks);



        //検索機能　文字入力SessionScope
        if(request.getSession().getAttribute("SessionfindItem") != null) {
            request.getSession().removeAttribute("SessionfindItem");
        }
        request.getSession().setAttribute("SessionfindItem", findItem);

        //flush処理
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
        rd.forward(request, response);

    }
}