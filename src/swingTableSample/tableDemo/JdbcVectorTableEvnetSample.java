package swingTableSample.tableDemo;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class JdbcVectorTableEvnetSample extends JFrame {
	/**
	 * @변수명 : serialVersionUID
	 * @설명 : 직렬화시 필요한 변수 선언
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @변수명 : data
	 * @설명 : 테이블에 표시될 데이터를 저장하는 벡터 변수 선언
	 */
	@SuppressWarnings("rawtypes")
	private Vector data 			= null;
	/**
	 * @변수명 : title
	 * @설명 : 테이블에 표시될 타이틀을 저장하는 벡터 변수 선언
	 */
	@SuppressWarnings("rawtypes")
	private Vector title 			= null;
	private JTable table 			= null;
	/**
	 * @변수명 : model
	 * @설명 : 테이블에 표시될 타이틀과 데이터를 저장하는 모델 객체 변수 선언
	 */
	private DefaultTableModel model = null;
	/**
	 * @변수명 : btnAdd
	 * @설명 : 추가 버튼 객체 변수 선언
	 */
	private JButton btnAdd 			= null;
	/**
	 * @변수명 : btnDel
	 * @설명 : 삭제 버튼 객체 변수 선언
	 */
	private JButton btnDel 			= null;
	/**
	 * @변수명 : btnUpdate
	 * @설명 : 수정 버튼 객체 변수 선언
	 */
	private JButton btnUpdate		= null;
	/**
	 * @변수명 : btnClear
	 * @설명 : 초기화 버튼 객체 변수 선언
	 */
	private JButton btnSearch		= null;
	/**
	 * @변수명 : btnSearch
	 * @설명 : 초기화 버튼 객체 변수 선언
	 */
	private JButton btnClear		= null;
	/**
	 * @변수명 : tfNum
	 * @설명 : 번호를 표시하는 TextField  변수 선언
	 */
	private JTextField tfNum		= null;
	/**
	 * @변수명 : tfName
	 * @설명 : 이름을 표시하는 TextField 변수 선언
	 */
	private JTextField tfName		= null;
	/**
	 * @변수명 : tfAddress
	 * @설명 : 주소를 표시하는 TextField 변수 선언
	 */
	private JTextField tfAddress	= null;
//	레이블 변수 선언
	private JLabel	lblNum			= null;
	private JLabel	lblName			= null;
	private JLabel	lblAddress		= null;
	/**
	 * @변수명 : Url
	 * @설명 : 데이터베이스 연동시 필요한 URL 저장하는 변수와 유저 정보
	 */
//	private String Url 				= "jdbc:log4jdbc:mysql://localhost:3306/shop_member?useSSL=false&amp;serverTimezone=UTC"; // URL 정보 저장 변수
//	private String user 			= "root"; // user 정보 저장 변수 -> hr
//	private String password 		= "rootroot"; // password 정보 저장 변수 -> hr
	/**
	 * @변수명 : conn
	 * @설명 : 데이터베이스 연동 Connection 객체 변수 선언
	 */
	private Connection conn 		= null;
	/**
	 * @변수명 : stmt
	 * @설명 : 데이터베이스 연동 후 쿼리 결과를 구하는 Statement 객체 변수 선언
	 */
	private Statement stmt			= null;
	/**
	 * @변수명 : pstmtAdd
	 * @설명 : 추가(insert) 쿼리 실행하는 PreparedStatement 객체 변수 선언
	 */
	private PreparedStatement pstmtAdd    = null;
	/**
	 * @변수명 : pstmtDel
	 * @설명 : 삭제(delete) 쿼리 실행하는 PreparedStatement 객체 변수 선언
	 */
	private PreparedStatement pstmtDel    = null;
	/**
	 * @변수명 : pstmtUpdate
	 * @설명 : 수정(update) 쿼리 실행하는 PreparedStatement 객체 변수 선언
	 */
	private PreparedStatement pstmtUpdate = null;
	/**
	 * @생성자명 : JdbcVectorTableEvnetSample
	 * @설명 : Frame 초기화, 패널 생성, 테이블 생성, 모델 생성, 화면에 필요한 컴포넌트 생성 및 
	 *        초기화 
	 */
	public JdbcVectorTableEvnetSample() {
		super("JdbcTable 연동 Sample");
//		데이터베이스 연동 Connection, Statement, 
//		각각의 PreparedStatement 생성, 초기화 메소드 , 중요 확인 필요
		preDbTreatment();
//		테이블에 표시될 데이터 벡터 생성
		data = new Vector<>();
//		테이블에 표시될 타이틀 벡터 생성, 초기화 
		title = new Vector<>();
		title.add("번호");
		title.add("이름");
		title.add("주소");
//		테이블에 표시될 모델 객체 생성
		model = new DefaultTableModel();
//		selectAll() : 데이터베이스  newmember 테이블에 있는 모든 데이터를 가지고 오는 메소드
//		벡터 result에 저장
		Vector result = selectAll();
//		모델에 변경된 데이터(result)를 새로 적용
		model.setDataVector(result, title);
//		모델을 통해 테이블 생성
		table = new JTable(model);
//		테이블에 스크롤팬 생성 
		JScrollPane sp = new JScrollPane(table);
//		테이블에 마우스 클릭(mouseClicked)시 처리될 이벤트 등록
		table.addMouseListener(new MouseAdapter() {
//			마우스 클릭시 처리를 담당하는 메소드 재정의
			@Override
			public void mouseClicked(MouseEvent e) {
//				getSelectedRow() : 테이블에서 선택된 줄의 값을 가지고 오는 메소드(0부터 시작됨) 
				int index = table.getSelectedRow();
//				현재 테이블에 표시되고 있는 data(모델)에서 index(현재 선택된 줄값)로 
//				1개의 레코드(줄) 전체를 벡터로 저장해서 in 벡터 변수에 대입
				Vector in = (Vector) data.get(index);
//				in 벡터에 들어있는 값을 각각의 String 변수에 대입 
				String num = (String)in.get(0);
				String name = (String)in.get(1);
				String address = (String)in.get(2);
//				화면에 표시된 각각의 TextField(번호, 이름, 주소)에 
//				값 setting
				tfNum.setText(num);
				tfName.setText(name);
				tfAddress.setText(address);
//				번호는 setEditable(false)로 수정 방지 처리
				tfNum.setEditable(false);
//				tfName.setEditable(false);
//				tfAddress.setEditable(false);
			}
		});
//		화면에 표시될 패널 생성
		JPanel panel = new JPanel();
//		값을 입력받거나 표시할 텍스트필드(번호, 이름, 주소) 생성
		tfNum = new JTextField(8);
		tfName = new JTextField(10);
		tfAddress = new JTextField(20);
//		레이블 생성
		lblNum = new JLabel("번호");
		lblName = new JLabel("이름");
		lblAddress = new JLabel("주소");
//		버튼(추가, 삭제, 수정, 초기화) 생성
		btnAdd = new JButton("추가");
		btnDel = new JButton("삭제");
		btnUpdate = new JButton("수정");
		btnSearch = new JButton("검색");
		btnClear = new JButton("초기화");
//		추가버튼에 이벤트(클릭시) 처리 -> 텍스트필드에 입력된 정보를 데이터베이스에 
//		저장(Insert)하는 영역
		btnAdd.addActionListener(new ActionListener() {
//			actionPerformed(ActionEvent e) : 추가 버튼 클릭시 호출될 메소드
//			추가버튼을 클릭하면 처리할 내용 작성
			@Override
			public void actionPerformed(ActionEvent e) {
//				현재 텍스트 필드에 있는 값을 각각의 변수에 대입 
				String num = tfNum.getText(); // 번호
				String name = tfName.getText(); // 이름
				String address = tfAddress.getText(); // 주소
//				각각의 변수에 저장된 값을 데이터베이스에 Insert하는 메소드
				insert(num, name, address);
//				신규 저장된 데이터를 데이터베이스에서 다시 읽어와서 result 벡터에 저장 
				Vector result = selectAll();
//				변경된 데이터(벡터)로 모델 갱신 -> 테이블 표시 갱신됨 
				model.setDataVector(result, title);
			}
		});
//		삭제 버튼에 이벤트(클릭시) 처리 -> 텍스트 필드에 입력된 번호값으로 데이터베이스에 
//		해당 번호의 레코드를 삭제(Delete) 하는 영역
		btnDel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				텍스트필드에 있는 번호값 변수에 대입
				String num = tfNum.getText();
//				번호값으로 데이터베이스에서 해당 레크드를 삭제하는 메소드
				delete(num);
//				삭제처리 데이터를 데이터베이스에서 다시 읽어와서 result 벡터에 저장 
				Vector result = selectAll();
//				변경된 데이터(벡터)로 모델 갱신 -> 테이블 표시 갱신됨
				model.setDataVector(result, title);
			}
		});
//		수정버튼에 이벤트(클릭시) 처리 -> 텍스트 필드에 입력된 번호값으로 데이터베이스에
//		해당 번호의 레크드를 수정(Update) 하는 영역 
		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				텍스트필드에 있는 값들을 변수에 저장
				String num = tfNum.getText();
				String name = tfName.getText();
				String address = tfAddress.getText();
//				번호를 기준으로 수정된 이름과 주소를 수정하는 메소드 
				update(name, address, num);
//				삭제처리 데이터를 데이터베이스에서 다시 읽어와서 result 벡터에 저장 
				Vector result = selectAll();
//				변경된 데이터(벡터)로 모델 갱신 -> 테이블 표시 갱신됨
				model.setDataVector(result, title);
			}
		});
//		검색버튼에 이벤트(클릭시) 처리 -> 텍스트 필드에 입력된 이름으로 데이터베이스에
//		해당 번호의 레크드를 검색(Search) 하는 영역 
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				텍스트필드에 있는 값들을 변수에 저장
				String num = tfNum.getText();
				String name = tfName.getText();
				String address = tfAddress.getText();
//				번호를 기준으로 수정된 이름과 주소를 수정하는 메소드 
				selectname(num,name,address);
				model.setDataVector(selectname(num,name,address), title);
			}
		});
//		초기화 버튼 이벤트(클릭시) 처리 -> 텍스트필드 초기화, 번호텍스트필드에 커서 위치
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				초기화
				tfNum.setText("");
				tfName.setText("");
				tfAddress.setText("");
//				수정가능하게 변경
				tfNum.setEditable(true);
				tfName.setEditable(true);
				tfAddress.setEditable(true);
//				번호텍스트필드에 커서 위치
				tfNum.requestFocus();
//				삭제처리 데이터를 데이터베이스에서 다시 읽어와서 result 벡터에 저장 
				Vector result = selectAll();
//				변경된 데이터(벡터)로 모델 갱신 -> 테이블 표시 갱신됨
				model.setDataVector(result, title);
			}
		});
//		패널에 각각의 레이블과 텍스트필드 추가
		panel.add(lblNum);
		panel.add(tfNum);
		panel.add(lblName);
		panel.add(tfName);
		panel.add(lblAddress);
		panel.add(tfAddress);
//		패널에 버튼 추가
		panel.add(btnAdd);
		panel.add(btnDel);
		panel.add(btnUpdate);
		panel.add(btnSearch);
		panel.add(btnClear);
//		Frame의 ContentPane 컨테이너 가지오기
		Container c = getContentPane();
//		컨테이너에 테이블, 패널(텍스트필드, 번트이 포함된 패널) 추가
		c.add(new JLabel("JDBC Table Demo", JLabel.CENTER),"North");
		c.add(sp,BorderLayout.CENTER);
		c.add(panel, BorderLayout.SOUTH);
//		프레임 종료시 처리될 이벤트 처리 
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent w) {
				try{
					stmt.close(); // Statement 객체 닫기
					conn.close(); // Connection 객체 닫기
					setVisible(false); // 화면 닫기
					dispose(); // 자원 반납
					System.exit(0); // 종료 처리
				}catch(Exception e){
				}
			}
		});
	}
	/**
	 * @Method Name : selectname
	 * @return : data  
	 * @설명 : 데이터베이스에서 검색된 데이터를 data Vector에 담아 리턴
	 */
	private Vector selectname(String a, String b, String c) {
		data.clear();
		try{
//			PreparedStatement 생성-> conn은 preDbTreatment() 메소드를 통해 생성되어 있음
			ResultSet rs = stmt.executeQuery("select * from newmember where address "+" like '%" + a + "%' and name "+" like '%" + b + "%' and address "+" like '%" + c + "%'");
			
			while(rs.next()){
				Vector in = new Vector<String>(); // 1개의 레코드 저장하는 벡터 생성
				String num = rs.getString(1); // 데이터베이스에서 번호값 가지고 와서 num 변수에 저장
//				String num = rs.getString("num");
				String name = rs.getString(2); // 데이터베이스에서 이름값 가지고 와서 name 변수에 저장
//				String name = rs.getString("name");
				String address = rs.getString(3); // 데이터베이스에서 주소값 가지고 와서 address 변수에 저장
//				String address = rs.getString("address");
//				벡터에 각각의 값 추가
				in.add(num);
				in.add(name);
				in.add(address);
//				전체 데이터를 저장하는 벡터에 in(1명의 데이터 저장) 벡터 추가
				data.add(in);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return data; // 전체 데이터 저장하는 data 벡터 리턴
	}
	/**
	 * @Method Name : selectAll
	 * @return : data  
	 * @설명 : 데이터베이스에서 검색된 데이터를 data Vector에 담아 리턴
	 */
	private Vector selectAll() {
		data.clear();
		try{
			ResultSet rs = stmt.executeQuery("select * from newmember order by num");
			while(rs.next()){
				Vector in = new Vector<String>(); // 1개의 레코드 저장하는 벡터 생성
				String num = rs.getString(1); // 데이터베이스에서 번호값 가지고 와서 num 변수에 저장
//				String num = rs.getString("num");
				String name = rs.getString(2); // 데이터베이스에서 이름값 가지고 와서 name 변수에 저장
//				String name = rs.getString("name");
				String address = rs.getString(3); // 데이터베이스에서 주소값 가지고 와서 address 변수에 저장
//				String address = rs.getString("address");
//				벡터에 각각의 값 추가
				in.add(num);
				in.add(name);
				in.add(address);
//				전체 데이터를 저장하는 벡터에 in(1명의 데이터 저장) 벡터 추가
				data.add(in);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return data; // 전체 데이터 저장하는 data 벡터 리턴
	}
	/**
	 * @Method Name : insert
	 * @param num :번호 텍스트필드에 입력받은 값 
	 * @param name : 이름 텍스트필드에 입력받은 값
	 * @param address : 주소 텍스트필드에 입력받은 값
	 * @설명 : 각각의 텍스트필드에 입력된 값을 파라미터로 받아서 데이터베이스에 
	 *        insert 처리하는 메소드
	 */
	private void insert(String num, String name, String address){
		try{
//			PreparedStatement 생성-> conn은 preDbTreatment() 메소드를 통해 생성되어 있음
			pstmtAdd = conn.prepareStatement("insert into newmember values(?,?,?)");
//			insert into newmember values(? -> 1 ,? -> 2, ? -> 3)" 각각의 ? 에 값 대입
			pstmtAdd.setString(1, num);
			pstmtAdd.setString(2, name);
			pstmtAdd.setString(3, address);
//			대입받은 쿼리를 실행 -> 입력 (insert)
			pstmtAdd.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * @Method Name : delete
	 * @param num : 번호 텍스트필드에 입력받은 값
	 * @설명 : 번호값을 파라미터를 받아 해당 번호의 데이터를 디비에서 삭제(delete) 하는 메소드 
	 */
	private void delete(String num){
		try{
//			PreparedStatement 생성-> conn은 preDbTreatment() 메소드를 통해 생성되어 있음
			pstmtDel = conn.prepareStatement("delete from newmember where num = ?");
//			num 값을 비교해서 삭제함
 			pstmtDel.setString(1, num);
// 			대입받은 쿼리를 실행-> 삭제 (delete)
			pstmtDel.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * @Method Name : update
	 * @param name : 이름 텍스트필드에 입력받은 값
	 * @param address : 주소 텍스트필드에 입력받은 값
	 * @param num : 번호 텍스트필드에 입력받은 값
	 * @설명 : 각각의 텍스트필드에 입력된 값을 파라미터로 받아서 데이터베이스에 
	 *        해당 번호값으로 이름과 주소를 수정하는 메소드 
	 */
	private void update(String name, String address, String num){
		try{
//			PreparedStatement 생성-> conn은 preDbTreatment() 메소드를 통해 생성되어 있음
			pstmtUpdate = conn.prepareStatement("update newmember set name = ?, address = ? where num = ?");
//			값 대입
			pstmtUpdate.setString(1, name);
			pstmtUpdate.setString(2, address);
//			? 순서 중요 확인 필요함
			pstmtUpdate.setString(3, num);
//			쿼리 실행
			pstmtUpdate.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * @Method Name : preDbTreatment
	 * @설명 : 데이터베이스 연동 및 Connection, Statement 생성
	 */
	private void preDbTreatment() {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop_member", "root","rootroot");
			stmt = conn.createStatement();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		JdbcVectorTableEvnetSample frame = new JdbcVectorTableEvnetSample();
//		내부 컴포넌트의 크기와 정렬 상태에 따라 프레임의 크기를 정함
		frame.pack();
		frame.setVisible(true);
	}
}