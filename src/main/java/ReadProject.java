import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/ReadProject")
public class ReadProject extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        List<String[]> projects = getAllProjects();
        JSONArray jsonArray = new JSONArray();

        for (String[] project : projects) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", project[0]);
            jsonObject.put("name", project[1]);
            jsonObject.put("description", project[2]);
            jsonObject.put("startDate", project[3]);
            jsonObject.put("endDate", project[4]);
            jsonObject.put("budget", project[5]);
            jsonObject.put("image", project[6]);
            jsonArray.put(jsonObject);
        }

        PrintWriter out = response.getWriter();
        out.print(jsonArray.toString());
        out.flush();
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
