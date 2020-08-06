package application;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ConnectionDatabase;
import model.Degree;
import model.Student;
import model.StudyCenter;

public class StudentFormController implements Initializable{
	//Columns
	@FXML private TableColumn<Student, String> colName;
	@FXML private TableColumn<Student, String> colLastname;
	@FXML private TableColumn<Student, Number> colAge;
	@FXML private TableColumn<Student, String> colGender;
	@FXML private TableColumn<Student, Date> colInDate;
	@FXML private TableColumn<Student, Degree> colDegree;
	@FXML private TableColumn<Student, StudyCenter> colStudyCenter;
	
	//Components GUI
	@FXML private TextField txtCode;
	@FXML private TextField txtName;
	@FXML private TextField txtLastname;
	@FXML private TextField txtAge;
	@FXML private RadioButton rbtFemale;
	@FXML private RadioButton rbtMale;
	@FXML private DatePicker dtpDate;
	
	@FXML private Button btnSave;
	@FXML private Button btnUpdate;
	@FXML private Button btnDelete;
	@FXML private Button btnNew;
	
	@FXML private ComboBox<Degree> cbDegree;
	@FXML private ComboBox<StudyCenter> cbStudyCenter;
	@FXML private TableView<Student> tblStudent;
	
	//Collections
	private ConnectionDatabase connection;
	private ObservableList<Degree> degreeList;
	private ObservableList<StudyCenter> studyCenterList;
	private ObservableList<Student> studentList;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Instanciar y crear conexión de la Base de Datos
		connection = new ConnectionDatabase();
		connection.createConnection();
		
		//Inicializar listas
		degreeList = FXCollections.observableArrayList();
		studyCenterList = FXCollections.observableArrayList();
		studentList = FXCollections.observableArrayList();
		
		//Llenar listas
		Degree.getData(connection.getConnection(), degreeList);
		StudyCenter.getData(connection.getConnection(), studyCenterList);
		Student.getData(connection.getConnection(), studentList);
		
		//Enlazar listas con ComboBox y TableView
		cbDegree.setItems(degreeList);
		cbStudyCenter.setItems(studyCenterList);
		tblStudent.setItems(studentList);
		
		//Enlazar columnas con atributos
		colName.setCellValueFactory(new PropertyValueFactory<Student, String>("nombre"));
		colLastname.setCellValueFactory(new PropertyValueFactory<Student, String>("apellido"));
		colAge.setCellValueFactory(new PropertyValueFactory<Student, Number>("edad"));
		colGender.setCellValueFactory(new PropertyValueFactory<Student, String>("genero"));
		colInDate.setCellValueFactory(new PropertyValueFactory<Student, Date>("fechaIngreso"));
		colDegree.setCellValueFactory(new PropertyValueFactory<Student, Degree>("idDegree"));
		colStudyCenter.setCellValueFactory(new PropertyValueFactory<Student, StudyCenter>("idStudyCenter"));
		
		eventManager();
		
		//Cerrar conexión de la Base de Datos
		connection.closeConnection();
	}
	
	public void eventManager() {
		tblStudent.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<Student>() {

					@Override
					public void changed(ObservableValue<? extends Student> arg0, Student beforeValue, Student selectedValue) {
						
						if(selectedValue != null) {
							txtCode.setText(String.valueOf(selectedValue.getId()));
							txtName.setText(selectedValue.getNombre());
							txtLastname.setText(selectedValue.getApellido());
							txtAge.setText(String.valueOf(selectedValue.getEdad()));
							if (selectedValue.getGenero().equals("F")) {
								rbtFemale.setSelected(true);
								rbtMale.setSelected(false);
							} else if(selectedValue.getGenero().equals("M")){
								rbtFemale.setSelected(false);
								rbtMale.setSelected(true);
							}
							dtpDate.setValue(selectedValue.getFechaIngreso().toLocalDate());
							cbDegree.setValue(selectedValue.getIdDegree());
							cbStudyCenter.setValue(selectedValue.getIdStudyCenter());
							
							btnSave.setDisable(true);
							btnDelete.setDisable(false);
							btnUpdate.setDisable(false);
						}
					}
					
				}
		);
	}
	
	@FXML
	public void targetEvent_btnSave() {
		connection.createConnection(); //Instanciar y crear conexión de la Base de Datos
		Student student = new Student( //Crear una nueva instancia del tipo Student
				0, 
				txtName.getText(), 
				txtLastname.getText(),
				Integer.valueOf(txtAge.getText()),
				rbtFemale.isSelected() ? "F" : "M",
				Date.valueOf(dtpDate.getValue()),
				cbDegree.getSelectionModel().getSelectedItem(),
				cbStudyCenter.getSelectionModel().getSelectedItem()
		);
		int result = student.saveRecord(connection.getConnection()); //Llamar al metodo saveRecord de la clase Student
		connection.closeConnection(); //Cerrar conexión de la Base de Datos
		
		if(result == 1) { //Comprobar resultado y mandar una alerta
			studentList.add(student);
			//JDK 8u>40
			Alert msg = new Alert(AlertType.INFORMATION);
			msg.setTitle("Registro agregado");
			msg.setContentText("El registro ha sido agregado exitosamente");
			msg.setHeaderText("Resultado: ");
			msg.show();
		}
	}
	
	@FXML
	public void targetEvent_btnUpdate() {
		connection.createConnection(); //Instanciar y crear conexión de la Base de Datos
		Student student = new Student( //Crear una nueva instancia del tipo Student
				Integer.valueOf(txtCode.getText()), 
				txtName.getText(), 
				txtLastname.getText(),
				Integer.valueOf(txtAge.getText()),
				rbtFemale.isSelected() ? "F" : "M",
				Date.valueOf(dtpDate.getValue()),
				cbDegree.getSelectionModel().getSelectedItem(),
				cbStudyCenter.getSelectionModel().getSelectedItem()
		);
		int result = student.updateRecord(connection.getConnection()); //Llamar al metodo updateRecord de la clase Student
		connection.closeConnection(); //Cerrar conexión de la Base de Datos
		
		if(result == 1) { //Comprobar resultado y mandar una alerta
			studentList.set(tblStudent.getSelectionModel().getSelectedIndex(), student);
			//JDK 8u>40
			Alert msg = new Alert(AlertType.INFORMATION);
			msg.setTitle("Registro actualizado");
			msg.setContentText("El registro ha sido actualizado exitosamente");
			msg.setHeaderText("Resultado: ");
			msg.show();
		}
	}
	
	@FXML
	public void targetEvent_btnDelete() {
		connection.createConnection(); //Instanciar y crear conexión de la Base de Datos
		int result = tblStudent.getSelectionModel().getSelectedItem().deleteRecord(connection.getConnection()); //Llamar al metodo deleteRecord de la clase Student
		connection.closeConnection(); //Cerrar conexión de la Base de Datos
		
		if(result == 1) { //Comprobar resultado y mandar una alerta
			studentList.remove(tblStudent.getSelectionModel().getSelectedIndex());
			//JDK 8u>40
			Alert msg = new Alert(AlertType.INFORMATION);
			msg.setTitle("Registro eliminado");
			msg.setContentText("El registro ha sido eliminado exitosamente");
			msg.setHeaderText("Resultado: ");
			msg.show();
		}
	}
	
	@FXML
	public void cleanComponents() {
		txtCode.setText(null);
		txtName.setText(null);
		txtLastname.setText(null);
		txtAge.setText(null);
		rbtFemale.setSelected(false);
		rbtMale.setSelected(false);
		dtpDate.setValue(null);
		cbDegree.setValue(null);
		cbStudyCenter.setValue(null);
		
		btnSave.setDisable(false);
		btnDelete.setDisable(true);
		btnUpdate.setDisable(true);
	}
}
