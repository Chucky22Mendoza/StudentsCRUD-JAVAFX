package model;

/* Java Bean
 * Class: Student */

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Student{
	private IntegerProperty id;
	private StringProperty nombre;
	private StringProperty apellido;
	private IntegerProperty edad;
	private StringProperty genero;
	private Date fechaIngreso;
	private Degree idDegree;
	private StudyCenter idStudyCenter;

	public Student(int id, String nombre, String apellido, int edad, String genero, Date fechaIngreso, Degree idDegree, StudyCenter idStudyCenter) { 
		this.id = new SimpleIntegerProperty(id);
		this.nombre = new SimpleStringProperty(nombre);
		this.apellido = new SimpleStringProperty(apellido);
		this.edad = new SimpleIntegerProperty(edad);
		this.genero = new SimpleStringProperty(genero);
		this.fechaIngreso = fechaIngreso;
		this.idDegree = idDegree;
		this.idStudyCenter = idStudyCenter;
	}

	//Metodos atributo: id
	public int getId() {
		return id.get();
	}
	
	public void setId(int id) {
		this.id = new SimpleIntegerProperty(id);
	}
	
	public IntegerProperty IdProperty() {
		return id;
	}
	
	//Metodos atributo: nombre
	public String getNombre() {
		return nombre.get();
	}
	
	public void setNombre(String nombre) {
		this.nombre = new SimpleStringProperty(nombre);
	}
	
	public StringProperty NombreProperty() {
		return nombre;
	}
	
	//Metodos atributo: apellido
	public String getApellido() {
		return apellido.get();
	}
	
	public void setApellido(String apellido) {
		this.apellido = new SimpleStringProperty(apellido);
	}
	
	public StringProperty ApellidoProperty() {
		return apellido;
	}
	
	//Metodos atributo: edad
	public int getEdad() {
		return edad.get();
	}
	
	public void setEdad(int edad) {
		this.edad = new SimpleIntegerProperty(edad);
	}
	
	public IntegerProperty EdadProperty() {
		return edad;
	}
	
	//Metodos atributo: genero
	public String getGenero() {
		return genero.get();
	}
	
	public void setGenero(String genero) {
		this.genero = new SimpleStringProperty(genero);
	}
	
	public StringProperty GeneroProperty() {
		return genero;
	}
	
	//Metodos atributo: fechaIngreso
	public Date getFechaIngreso() {
		return fechaIngreso;
	}
	
	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	
	//Metodos atributo: idDegree
	public Degree getIdDegree() {
		return idDegree;
	}
	
	public void setIdDegree(Degree idDegree) {
		this.idDegree = idDegree;
	}
	
	//Metodos atributo: idStudyCenter
	public StudyCenter getIdStudyCenter() {
		return idStudyCenter;
	}
	
	public void setIdStudyCenter(StudyCenter idStudyCenter) {
		this.idStudyCenter = idStudyCenter;
	}
	
	public int saveRecord(Connection connection) {
		try {
			String SQLQuery = "INSERT INTO alumno(nombre, apellido, edad, genero, fecha_ingreso, id_carrera, id_centro_estudio) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery); //Evitar inyeccion SQL
			preparedStatement.setString(1, nombre.get());
			preparedStatement.setString(2, apellido.get());
			preparedStatement.setInt(3, edad.get());
			preparedStatement.setString(4, genero.get());
			preparedStatement.setDate(5, fechaIngreso);
			preparedStatement.setInt(6, idDegree.getId());
			preparedStatement.setInt(7, idStudyCenter.getId());
			
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int updateRecord(Connection connection) {
		try {
			String SQLQuery = "UPDATE alumno "
									+ "SET nombre = ?, "
									+ "apellido = ?, "
									+ "edad = ?, "
									+ "genero = ?, "
									+ "fecha_ingreso = ?, "
									+ "id_carrera = ?, "
									+ "id_centro_estudio = ? "
							+ "WHERE id_alumno = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery); //Evitar inyeccion SQL
			preparedStatement.setString(1, nombre.get());
			preparedStatement.setString(2, apellido.get());
			preparedStatement.setInt(3, edad.get());
			preparedStatement.setString(4, genero.get());
			preparedStatement.setDate(5, fechaIngreso);
			preparedStatement.setInt(6, idDegree.getId());
			preparedStatement.setInt(7, idStudyCenter.getId());
			preparedStatement.setInt(8, id.get());
			
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int deleteRecord(Connection connection) {
		try {
			String SQLQuery = "DELETE FROM alumno "
								+ "WHERE id_alumno = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery); //Evitar inyeccion SQL
			preparedStatement.setInt(1, id.get());
			
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static void getData(Connection connection, ObservableList<Student> list) {
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(
					"SELECT " + 
						"aux.id_alumno, " + 
						"aux.nombre, " + 
						"aux.apellido, " + 
						"aux.edad, " + 
						"aux.genero, " +
						"aux.fecha_ingreso, " +
						"aux.id_carrera, " +
						"aux.carrera, " + 
						"aux.cantidad_asignaturas, " +
						"ce.id_centro_estudio, " +
						"ce.nombre AS centro_estudio " + 
					"FROM (" + 
						"SELECT " + 
							"al.*, " + 
							"car.nombre AS carrera, " + 
							"car.cantidad_asignaturas " + 
						"FROM alumno AS al " + 
							"INNER JOIN carrera AS car " + 
								"ON al.id_carrera = car.id_carrera" + 
					") AS aux " + 
						"INNER JOIN centro_estudio AS ce " + 
							"ON aux.id_centro_estudio = ce.id_centro_estudio"
			);
			while (result.next()) {
				list.add(
						new Student(
								result.getInt("id_alumno"),
								result.getString("nombre"),
								result.getString("apellido"),
								result.getInt("edad"),
								result.getString("genero"),
								result.getDate("fecha_ingreso"),
								new Degree(
										result.getInt("id_carrera"),
										result.getString("carrera"),
										result.getInt("cantidad_asignaturas")
								),
								new StudyCenter(
										result.getInt("id_centro_estudio"),
										result.getString("centro_estudio")
								)
						)
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
