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

@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ReportsIndexServlet() {
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


        List<Report> reports = em.createNamedQuery("getAllReports", Report.class)
                                  .setFirstResult(15 * (page - 1))
                                  .setMaxResults(15)
                                  .getResultList();

        long reports_count = em.createNamedQuery("getReportsCount", Long.class)
                .getSingleResult();


        Long[] report_reactions = new Long [(int)reports_count];
        Long[] reaction_status = new Long [(int)reports_count];
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

            long reaction_st = em.createNamedQuery("getLoginReactionCount", Long.class)
                    .setParameter("report_id" ,r)
                    .setParameter("logid" ,login_employee)
                    .getSingleResult();

            reaction_status[j] = reaction_st;
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
        request.setAttribute("followChecks", followChecks);

        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        if(request.getSession().getAttribute("SessionfindItem") != null) {
            request.getSession().removeAttribute("SessionfindItem");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");
        rd.forward(request, response);
    }
}
