package controllers.reactions;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Reaction;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReactionDestroyServlet
 */
@WebServlet("/destroy")
public class ReactionDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReactionDestroyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Report a = em.find(Report.class, Integer.parseInt(request.getParameter("report_id")));
            Reaction r =em.find(Reaction.class, (Integer)(request.getSession().getAttribute("logid")));

            em.getTransaction().begin();
            em.remove(r);
            em.getTransaction().commit();
            em.close();

            request.getSession().setAttribute("flush_reaction", "投稿を削除しました。");

            request.getSession().removeAttribute("logid");

            request.setAttribute("report_id",a.getId());

            response.sendRedirect(request.getContextPath() + "/reports/show?id=" + a.getId());
        }
    }
}
