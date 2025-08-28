import java.sql.*;
import javax.servlet.http.*;
import java.io.IOException;

public class VulnerableApplication extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/test";
    private static final String USER = "root";
    private static final String PASS = "password";

    // 修正済み：プレースホルダを使用して脆弱性を排除
    public void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) { // プレースホルダ使用
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                response.getWriter().println("Logged in as " + rs.getString("username"));
            } else {
                response.getWriter().println("Invalid credentials.");
            }
        } catch (SQLException e) {
            response.getWriter().println("Database error: " + e.getMessage());
        }
    }

    // 修正なし：SQLインジェクションを残す
    public void saveComment(HttpServletRequest request, HttpServletResponse response) {
        String comment = request.getParameter("comment");

        String query = "INSERT INTO comments (text) VALUES ('" + comment + "')";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) { // SQLインジェクションの脆弱性
            stmt.executeUpdate(query);
            response.getWriter().println("Comment saved: " + comment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修正済み：プレースホルダを使用して脆弱性を排除
    public void searchOrders(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productId = request.getParameter("productId");
        String userId = request.getParameter("userId");

        String query = "SELECT * FROM orders WHERE product_id = ? AND user_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) { // プレースホルダ使用
            pstmt.setString(1, productId);
            pstmt.setString(2, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                response.getWriter().println("Order found: " + rs.getString("order_id"));
            } else {
                response.getWriter().println("No order found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修正なし：SQLインジェクションを残す
    public void deleteOrder(HttpServletRequest request, HttpServletResponse response) {
        String orderId = request.getParameter("orderId");

        String query = "DELETE FROM orders WHERE order_id = '" + orderId + "'";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) { // SQLインジェクションの脆弱性
            stmt.executeUpdate(query);
            response.getWriter().println("Order deleted: " + orderId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修正済み：プレースホルダを使用して脆弱性を排除
    public void fetchProductDetails(HttpServletRequest request, HttpServletResponse response) {
        String productId = request.getParameter("productId");

        String query = "SELECT * FROM products WHERE product_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) { // プレースホルダ使用
            pstmt.setString(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                response.getWriter().println("Product details: " + rs.getString("name"));
            } else {
                response.getWriter().println("Product not found.");
            }
        } catch (SQLException e) {
            response.getWriter().println("Error retrieving product: " + e.getMessage());
        }
    }
}
