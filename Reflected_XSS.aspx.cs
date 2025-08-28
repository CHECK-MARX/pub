using System;
using System.Web;

namespace ReflectedXssExample
{
    public partial class Reflected_XSS : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            string userInput = Request.QueryString["input"];
            string keyword = Request.QueryString["kw"];

            // 典型的な Reflected XSS（無害化せず出力）
            Response.Write("<h2>Echoed Input:</h2>");
            Response.Write("<p>" + userInput + "</p>");
            Response.Write("<p>Keyword: " + keyword + "</p>");

            // 関数分離例
            OutputWithoutSanitize(userInput);
        }

        private void OutputWithoutSanitize(string val)
        {
            Response.Write("<div>Output: " + val + "</div>");
        }
    }
}
