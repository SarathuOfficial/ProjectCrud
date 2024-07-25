import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/UpdateProject")
@MultipartConfig
public class UpdateProject extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPut(request, response);
    }

    @Override
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
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing parameters");
            return;
        }

        Part imagePart = request.getPart("image");
        InputStream imageStream = imagePart != null && imagePart.getSize() > 0 ? imagePart.getInputStream() : null;

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "UPDATE ProjectDetails SET projectName = ?, projectDescription = ?, startDate = ?, endDate = ?, status = ?, budget = ?, image = ? WHERE projectId = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, projectName);
                statement.setString(2, projectDescription);
                statement.setDate(3, java.sql.Date.valueOf(startDate));
                statement.setDate(4, java.sql.Date.valueOf(endDate));
                statement.setString(5, status);
                statement.setBigDecimal(6, new BigDecimal(budget));
                if (imageStream != null) {
                    statement.setBlob(7, imageStream);
                } else {
                    statement.setNull(7, java.sql.Types.BLOB);
                }
                statement.setInt(8, Integer.parseInt(projectId));

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    response.setContentType("text/plain");
                    response.getWriter().write("Project updated successfully");
                } else {
                	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Project ID does not exist");
      
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}
