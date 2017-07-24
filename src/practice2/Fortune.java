package practice2;


import java.util.ResourceBundle;

public interface Fortune {

	//fortune.prorertiesを読み込み
	ResourceBundle rb = ResourceBundle.getBundle("fortune");

	//fortune.prorertiesで定義してあるdisp_strをDISP_STRに入れる
	String DISP_STR = rb.getString("disp_str");

	String disp();

}
