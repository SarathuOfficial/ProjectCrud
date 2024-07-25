import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/DeleteProject")
public class DeleteProject extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String projectId = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        // Add your logic to delete the project from the database
        boolean deleteSuccess = deleteProjectFromDatabase(projectId);

        if (deleteSuccess) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Project deleted successfully");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Change to 400 Bad Request
            response.getWriter().write("Error: Unable to delete project. Please check if the project ID is correct and try again.");
        }
    }

    // Method to delete a project from the database
    private boolean deleteProjectFromDatabase(String projectId) {
        String sql = "DELETE FROM ProjectDetails WHERE projectID = ?"; // Use the correct column name
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, projectId);

            // Execute the delete statement
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if the project was deleted successfully

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}