import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/CreateProject")
@MultipartConfig
public class CreateProject extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String projectName = request.getParameter("projectName");
        String projectDescription = request.getParameter("projectDescription");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String status = request.getParameter("status");
        String budget = request.getParameter("budget");

        Part filePart = request.getPart("image");
        byte[] imageBytes = null;
        if (filePart != null) {
            InputStream inputStream = filePart.getInputStream();
            imageBytes = inputStream.readAllBytes();
        }

        java.sql.Date startSqlDate = null;
        java.sql.Date endSqlDate = null;

        try {
            if (startDate != null && !startDate.trim().isEmpty()) {
                startSqlDate = java.sql.Date.valueOf(startDate);
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                endSqlDate = java.sql.Date.valueOf(endDate);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO ProjectDetails (projectName, projectDescription, startDate, endDate, status, budget, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, projectName);
            statement.setString(2, projectDescription);
            statement.setDate(3, startSqlDate);
            statement.setDate(4, endSqlDate);
            statement.setString(5, status);
            statement.setBigDecimal(6, new java.math.BigDecimal(budget));
            if (imageBytes != null) {
                statement.setBytes(7, imageBytes);
            } else {
                statement.setNull(7, java.sql.Types.BLOB);
            }
            statement.executeUpdate();
            
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
            return;
        }
        
        response.sendRedirect("index.jsp");
    }

}
