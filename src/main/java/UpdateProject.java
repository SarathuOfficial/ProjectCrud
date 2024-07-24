import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UpdateProject")
public class UpdateProject extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPut(request, response);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve parameters
        String projectId = request.getParameter("projectId");
        String projectName = request.getParameter("projectName");
        String projectDescription = request.getParameter("projectDescription");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String status = request.getParameter("status");
        String budget = request.getParameter("budget");

        if (projectId == null || projectName == null || projectDescription == null || startDate == null || endDate == null || status == null || budget == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing parameters");
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "UPDATE ProjectDetails SET projectName = ?, projectDescription = ?, startDate = ?, endDate = ?, status = ?, budget = ? WHERE projectId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, projectName);
            statement.setString(2, projectDescription);
            statement.setDate(3, java.sql.Date.valueOf(startDate));
            statement.setDate(4, java.sql.Date.valueOf(endDate));
            statement.setString(5, status);
            statement.setBigDecimal(6, new BigDecimal(budget));
            statement.setInt(7, Integer.parseInt(projectId));

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Project updated successfully");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Project not found");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}
