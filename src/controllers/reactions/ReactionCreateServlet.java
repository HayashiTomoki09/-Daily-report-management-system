package controllers.reactions;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Reaction;
import models.Report;
import models.validators.ReactionValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ReactionCreateServlet
 */
@WebServlet("/create")
public class ReactionCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ReactionCreateServlet() {
        super();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Reaction r = new Reaction();
            Report a = em.find(Report.class, Integer.parseInt(request.getParameter("report_id")));
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());


            r.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
            r.setReport_id(a);
            r.setType(Integer.parseInt(request.getParameter("reaction_type")));
            r.setMessage(request.getParameter("reaction_coment"));
            r.setCreated_at(currentTime);


            List<String> errors = ReactionValidator.validate(r);
            if(errors.size() > 0) {
                em.close();


                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("reaction", r);
                request.setAttribute("errors", errors);


                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
                rd.forward(request, response);
            } else {
                em.getTransaction().begin();
                em.persist(r);
                em.getTransaction().commit();
                em.close();


                request.getSession().setAttribute("flush_reaction", "リアクションを投稿しました。");
                request.setAttribute("report_id", Integer.parseInt(request.getParameter("report_id")));
                response.sendRedirect(request.getContextPath() + "/reports/show?id=" + a.getId());
            }
        }
    }
}
