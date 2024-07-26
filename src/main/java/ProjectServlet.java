import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/project")
@MultipartConfig
public class ProjectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String projectName = request.getParameter("projectName");
        String projectDescription = request.getParameter("projectDescription");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String status = request.getParameter("status");
        String budget = request.getParameter("budget");

        Part filePart = request.getPart("image");
        byte[] imageBytes = null;
        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream inputStream = filePart.getInputStream()) {
                imageBytes = inputStream.readAllBytes();
            }
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
            response.setContentType("text/plain");
            response.getWriter().write("Invalid date format");
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            String sql = "INSERT INTO ProjectDetails (projectName, projectDescription, startDate, endDate, status, budget, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, projectName);
                statement.setString(2, projectDescription);
                statement.setDate(3, startSqlDate);
                statement.setDate(4, endSqlDate);
                statement.setString(5, status);
                statement.setBigDecimal(6, new BigDecimal(budget));
                if (imageBytes != null) {
                    statement.setBytes(7, imageBytes);
                } else {
                    statement.setNull(7, java.sql.Types.BLOB);
                }
                statement.executeUpdate();
                response.setContentType("text/plain");
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("Project created successfully");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
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
                    response.setStatus(HttpServletResponse.SC_CREATED);
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
    

        @Override
        protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String projectId = request.getParameter("projectId");

            if (projectId == null || projectId.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Error: Project ID is required.");
                return;
            }

            boolean deleteSuccess = deleteProjectFromDatabase(projectId);

            if (deleteSuccess) {
            	response.setContentType("text/plain");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Project deleted successfully.");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Error: Unable to delete project. Please check if the project ID is correct and try again.");
            }
        }

        private boolean deleteProjectFromDatabase(String projectId) {
            String sql = "DELETE FROM ProjectDetails WHERE projectID = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, projectId);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        
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
        String sql = "SELECT projectID, projectName, projectDescription, startDate, endDate, budget FROM ProjectDetails";
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
                project[6] = "image?id=" + project[0];
                projects.add(project);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return projects;
    }
    
}
