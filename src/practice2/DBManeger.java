package practice2;


import java.sql.Connection;
import java.sql.DriverManager;

public class DBManeger {

	/**
	 * コネクションメソッド
	 * @return conn
	 */
	public static Connection getConnection(){

		//変数定義
		Connection conn = null;

		try {
			//JDBCドライバをロードする
			Class.forName("org.postgresql.Driver");

			//DB接続情報を設定する
			String path = "jdbc:postgresql://localhost:5656/test";  //接続パス
			String id = "postgres";    //ログインID
			String pw = "Rui032512";  //ログインパスワード

			//DBへのコネクションを作成する
			conn = DriverManager.getConnection(path, id, pw);
			conn.setAutoCommit(false);  //オートコミットはオフ


		} catch (Exception ex) {
			// 例外処理
			ex.printStackTrace();

		}
		return conn;

	}






}
