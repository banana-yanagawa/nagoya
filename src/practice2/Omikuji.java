package practice2;


public class Omikuji extends OmikujiKind {

	public static Omikuji getInstance(){
		return new Omikuji();
	}

	public int getOmikujiId() {
		return omikujiId;
	}

	public void setOmikujiId(int omikujiId) {
		this.omikujiId = omikujiId;
	}

	public String getUnseiName() {
		return unseiName;
	}

	public void setUnseiName(String unseiName) {
		this.unseiName = unseiName;
	}

	public void setUnsei(String unsei) {
		this.unsei = unsei;
	}

	public String getUnsei() {
		return unsei;
	}

	public String getNegaigoto() {
		return negaigoto;
	}

	public void setNegaigoto(String negaigoto) {
		this.negaigoto = negaigoto;
	}

	public String getAkinai() {
		return akinai;
	}

	public void setAkinai(String akinai) {
		this.akinai = akinai;
	}

	public String getGakumon() {
		return gakumon;
	}

	public void setGakumon(String gakumon) {
		this.gakumon = gakumon;
	}


	@Override
	public String disp() {

		//dispメソッドで出したい文字を連結
		StringBuilder sb = new StringBuilder();

		//DISP_STRの%s部分に運勢をセット
		String str = String.format(DISP_STR, unseiName);

		//改行用
		String sep = System.getProperty("line.separator");

		sb.append(str);
		sb.append(sep);
		sb.append("願い事：");
		sb.append(negaigoto);
		sb.append(sep);
		sb.append("商い：");
		sb.append(akinai);
		sb.append(sep);
		sb.append("学問：");
		sb.append(gakumon);

		return sb.toString();

	}


}