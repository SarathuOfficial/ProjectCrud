import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int projectID = Integer.parseInt(request.getParameter("id"));
        byte[] image = getImageByProjectID(projectID);

        response.setContentType("image/jpeg");
        try (OutputStream out = response.getOutputStream()) {
            if (image != null) {
                out.write(image);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    private byte[] getImageByProjectID(int projectID) {
        byte[] image = null;
        String sql = "SELECT image FROM ProjectDetails WHERE projectID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    image = rs.getBytes("image");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return image;
    }
}
