package practice2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ResultServlet
 */
@WebServlet("/Result")
public class ResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ResultServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		//変数定義
		Connection conn = null;

		//もしテーブルの中がnullなら
		try {
			if(!OmikujiDAO.getInstance().checkTable()){

				//繋ぐ
				conn = DBManeger.getConnection();

				//insertUnseiとinsertOmikujiを呼ぶ
				OmikujiDAO.getInstance().insertUnsei();
				OmikujiDAO.getInstance().insertOmikuji();

			}



			//繋ぐ
			conn = DBManeger.getConnection();

			String birthday = request.getParameter("birthday");

			//Date型に変換
			Date formatDate = Date.valueOf(birthday);

			//入力された誕生日を持って同日チェックしに行く
			Omikuji resOmikuji = OmikujiDAO.getInstance().searchOmikuji(formatDate);

			//もしnullなら
			if(resOmikuji == null) {

				//おみくじの件数を調べて件数の中でランダム使う　
				Random rnd = new Random();
				int omikujiId = rnd.nextInt(OmikujiDAO.getInstance().checkOmikuji());

				//resOmikujiの中にchoiceOmikujiメソッドの結果を入れる
				resOmikuji = OmikujiDAO.getInstance().choiceOmikuji(omikujiId);

				//結果テーブルに反映
				OmikujiDAO.getInstance().insertResult(formatDate,resOmikuji.getOmikujiId());

			}

			request.setAttribute("result",resOmikuji.disp());

			RequestDispatcher dispatcher = request.getRequestDispatcher("entry/result.jsp");

			dispatcher.forward(request, response);

			// クローズ処理
			if (conn != null){
				conn.close();

		}

		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

}






/**
 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
 */
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// TODO Auto-generated method stub
	doGet(request, response);
}

}
