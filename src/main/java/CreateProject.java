import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CreateProject")
public class CreateProject extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String projectName = request.getParameter("projectName");
        String projectDescription = request.getParameter("projectDescription");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String status = request.getParameter("status");
        String budget = request.getParameter("budget");

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO ProjectDetails (projectName, projectDescription, startDate, endDate, status, budget) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, projectName);
            statement.setString(2, projectDescription);
            statement.setDate(3, java.sql.Date.valueOf(startDate));
            statement.setDate(4, java.sql.Date.valueOf(endDate));
            statement.setString(5, status);
            statement.setBigDecimal(6, new java.math.BigDecimal(budget));
            statement.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("index.jsp");
    }
}
