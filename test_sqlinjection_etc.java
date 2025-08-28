import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class VulnerableApp {
    // ハードコードされた機密情報（脆弱性）
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "admin";
    private static final String PASS = "password";

    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            String userId = request.getParameter("userId"); // 任意の入力
            String productId = request.getParameter("productId");

            // SQLインジェクション（脆弱性1）
            String query1 = "SELECT * FROM users WHERE id = '" + userId + "'";
            ResultSet rs1 = stmt.executeQuery(query1);
            if (rs1.next()) {
                response.getWriter().println("User found: " + rs1.getString("username"));
            }

            // SQLインジェクション（脆弱性2）
            String query2 = "DELETE FROM orders WHERE product_id = '" + productId + "'";
            stmt.executeUpdate(query2);
            response.getWriter().println("Order deleted for product: " + productId);

            // SQLインジェクション（脆弱性3）
            String username = request.getParameter("username");
            String query3 = "SELECT * FROM orders WHERE username = '" + username + "'";
            ResultSet rs3 = stmt.executeQuery(query3);
            while (rs3.next()) {
                response.getWriter().println("Order ID: " + rs3.getInt("order_id"));
            }

            // クロスサイトスクリプティング（XSS）脆弱性
            String userComment = request.getParameter("comment");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h3>User Comment:</h3>");
            out.println("<p>" + userComment + "</p>"); // サニタイズなしで出力（XSSのリスク）
            out.println("</body></html>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ハードコードされた機密情報（脆弱性）を含む認証メソッド
    public boolean login(String username, String password) {
        // 脆弱な認証ロジック（例: ハードコードされたユーザー名とパスワード）
        return username.equals("admin") && password.equals("admin123");
    }
}
