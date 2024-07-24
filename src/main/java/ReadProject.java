import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ReadProject")
public class ReadProject extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String[]> projects = getAllProjects();
        request.setAttribute("projects", projects);
        request.getRequestDispatcher("ReadProjects.jsp").forward(request, response);
    }

    private List<String[]> getAllProjects() {
        List<String[]> projects = new ArrayList<>();
        String sql = "SELECT projectID, projectName, projectDescription, startDate, endDate, budget, image FROM ProjectDetails";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String[] project = new String[7];
                project[0] = rs.getString("projectID");
                project[1] = rs.getString("projectName");
                project[2] = rs.getString("projectDescription");
                project[3] = rs.getString("startDate");
                project[4] = rs.getString("endDate");
                project[5] = rs.getString("budget");
                project[6] = rs.getBytes("image") != null ? "image?id=" + project[0] : null;
                projects.add(project);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return projects;
    }
}