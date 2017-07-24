package practice2;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OmikujiDAO {

	//変数定義
	Connection conn = null;
	PreparedStatement ps = null;


	/**
	 * インスタンスを返却するためのメソッド
	 */
	public static OmikujiDAO getInstance(){

		return new OmikujiDAO();

	}


	/**
	 * 運勢マスタテーブル作成用メソッド
	 * @throws SQLException
	 */
	public void insertUnsei() throws SQLException{

		//繋ぐ
		conn = DBManeger.getConnection();

		//運勢マスタテーブルに対してのインサートSQLとプレースホルダの用意
		String sql = "INSERT INTO unsei values(?, ?, ?, ?, ?, ?)";

		//運勢の配列用意
		String[] str = {"大吉","小吉","中吉","吉","凶","末吉"};

		//運勢の数だけ回す
		//実行するSQL文とパラメータを指定する
		for(int i = 0; i < str.length; i++){
			ps = conn.prepareStatement(sql);
			ps.setInt(1, i);
			ps.setString(2, str[i]);
			ps.setString(3, NameConstants.YANA);
			ps.setDate(4, date());
			ps.setString(5, NameConstants.YANA);
			ps.setDate(6, date());
			ps.executeUpdate();

		}

		//コミット
		conn.commit();

	}


	/**
	 * CSVファイルからおみくじ内容を読み込み、
	 * おみくじテーブルに登録するためのメソッド
	 * @throws SQLException
	 * @throws IOException
	 */
	public void insertOmikuji() throws SQLException, IOException{

		//繋ぐ
		conn = DBManeger.getConnection();

		//おみくじテーブルに対してのインサートSQLとプレースホルダの用意
		String sql2 = "INSERT INTO omikuji values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

		//ファイルを読み込む
		FileReader fr = new FileReader("/Users/r_yanagawa/Documents/fortune.csv");
		BufferedReader br = new BufferedReader(fr);

		//for文用
		int i = 1;

		//読み込んだファイルを１行ずつ処理する
		String line = br.readLine();
		while (line != null) {

			//区切り文字","で分割する
			String[] kuji = line.split(",",0);

			//実行するSQL文とパラメータを指定する
			ps = conn.prepareStatement(sql2);
			ps.setInt(1, i);
			ps.setInt(2, unsei(kuji));
			ps.setString(3, kuji[1]);
			ps.setString(4, kuji[2]);
			ps.setString(5, kuji[3]);
			ps.setString(6, NameConstants.YANA);
			ps.setDate(7, date());
			ps.setString(8, NameConstants.YANA);
			ps.setDate(9, date());
			ps.executeUpdate();

			i++;

			line = br.readLine();

		}

		//終わったら閉じる
		br.close();			

		//コミット
		conn.commit();

	}



	/**
	 * 入力された誕生日と今日の日付が同じなら同じ結果を返すための準備メソッド
	 * 
	 * @param formatDate
	 * @return omikuji
	 * @throws SQLException
	 */
	public Omikuji searchOmikuji(Date formatDate) throws SQLException{

		//繋ぐ
		conn = DBManeger.getConnection();

		//３テーブル結合　入力された誕生日と今日の日付が一致したものだけ取り出す
		String sql3 = "SELECT unsei.unsei_name,omikuji.omikuji_id,omikuji.akinai,omikuji.negaigoto,omikuji.gakumon FROM (omikuji inner join unsei on omikuji.unsei_id = unsei.unsei_id) inner join result on omikuji.omikuji_id = result.omikuji_id where result.fortune_day = ? and result.birthday = ?";
		PreparedStatement ps = conn.prepareStatement(sql3);

		//今日の日付と入力された誕生日をセット
		ps.setDate(1,date());
		ps.setDate(2,formatDate);

		//実行
		ResultSet rs2 = ps.executeQuery();

		//おみくじオブジェクト生成
		Omikuji omikuji = null;

		//レコードがある限り繰り返す
		while(rs2.next()){

			//コンソールに出す用と結果テーブルに反映用にセット
			omikuji = new Omikuji();
			omikuji.setOmikujiId(rs2.getInt("omikuji_id"));
			omikuji.setUnseiName(rs2.getString("unsei_name"));
			omikuji.setNegaigoto(rs2.getString("negaigoto"));
			omikuji.setAkinai(rs2.getString("akinai"));
			omikuji.setGakumon(rs2.getString("gakumon"));

		}

		return omikuji;


	}


	/**
	 * 結果テーブルに登録するメソッド
	 * @param formatDate
	 * @param omikujiId
	 * @throws SQLException
	 */
	public void insertResult(Date formatDate,int omikujiId) throws SQLException{

		//繋ぐ
		conn = DBManeger.getConnection();

		//運勢マスタテーブルに対してのインサートSQLとプレースホルダの用意
		String sql4 = "INSERT INTO result values(?, ?, ?, ?, ?)";

		//実行するSQL文とパラメータを指定する
		ps = conn.prepareStatement(sql4);
		ps.setDate(1, date());
		ps.setDate(2, formatDate);
		ps.setInt(3, omikujiId);
		ps.setString(4, NameConstants.YANA);
		ps.setDate(5, date());
		ps.executeUpdate();

		//コミット
		conn.commit();

	}


	/**
	 * テーブルに値が入っているか確認するためのメソッド
	 * @return true false
	 * @throws SQLException
	 */
	public boolean checkTable() throws SQLException{

		//繋ぐ
		conn = DBManeger.getConnection();

		Statement stmt = conn.createStatement();
		String sql6 = "SELECT COUNT(*) FROM omikuji";
		ResultSet rs3 = stmt.executeQuery(sql6);

		int cnt = 0;
		while(rs3.next()){
			rs3.getInt(1);

		}
		if (cnt == 0) {
			return true;
		}

		return false;

	}


	/**
	 * おみくじテーブルの件数を返すためのメソッド
	 * @return cnt
	 * @throws SQLException
	 */
	public int checkOmikuji() throws SQLException{

		//繋ぐ
		conn = DBManeger.getConnection();

		Statement stmt = conn.createStatement();
		String sql6 = "SELECT COUNT(*) FROM omikuji";
		ResultSet rs3 = stmt.executeQuery(sql6);

		int cnt = 0;

		while(rs3.next()){

			cnt = rs3.getInt(1);

		}

		return cnt;

	}


	/**
	 * 同じ結果を返すためのメソッド
	 * @param omikujiId
	 * @return omikuji
	 * @throws SQLException
	 */
	public Omikuji choiceOmikuji(int omikujiId) throws SQLException{

		//繋ぐ
		conn = DBManeger.getConnection();

		String sql6 = "SELECT unsei.unsei_name,omikuji.omikuji_id,omikuji.akinai,omikuji.negaigoto,omikuji.gakumon FROM omikuji inner join unsei on omikuji.unsei_id = unsei.unsei_id where omikuji_id = ? ";
		PreparedStatement ps = conn.prepareStatement(sql6);

		ps.setInt(1, omikujiId);

		ResultSet rs2 = ps.executeQuery();

		Omikuji omikuji = null;

		//レコードがある限り繰り返す
		while(rs2.next()){

			//コンソールに出す用と結果テーブルに反映用にセット
			omikuji = new Omikuji();
			omikuji.setOmikujiId(rs2.getInt("omikuji_id"));
			omikuji.setUnseiName(rs2.getString("unsei_name"));
			omikuji.setNegaigoto(rs2.getString("negaigoto"));
			omikuji.setAkinai(rs2.getString("akinai"));
			omikuji.setGakumon(rs2.getString("gakumon"));

		}

		return omikuji;

	}


	/**
	 * 今日の日付生成のためのメソッド
	 * @return　date2
	 */
	public static Date date(){

		//今日の日付を生成
		java.util.Date date1 = new java.util.Date();
		java.sql.Date date2 = new java.sql.Date(date1.getTime());

		return date2;

	}



	/**
	 * 
	 * 運勢名を運勢コードに変換するためのメソッド
	 * 
	 * @param kuji
	 * @return unseiId
	 */
	public static int unsei(String[] kuji){


		int unseiId = 0;

		//もし運勢が大吉だったら
		if(kuji[0].equals("大吉")){

			unseiId = 0;

		}

		//もし運勢が中吉だったら
		if(kuji[0].equals("小吉")){

			unseiId = 1;

		}

		//もし運勢が小吉だったら
		if(kuji[0].equals("中吉")){

			unseiId = 2;

		}

		//もし運勢が吉だったら
		if(kuji[0].equals("吉")){

			unseiId = 3;

		}

		//もし運勢が末吉だったら
		if(kuji[0].equals("凶")){

			unseiId = 4;

		}

		//もし運勢が凶だったら
		if(kuji[0].equals("末吉")){

			unseiId = 5;

		}



		return unseiId;



	}
}








