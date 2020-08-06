package model;

/* Java Bean
 * Class: StudyCenter */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class StudyCenter{
	private IntegerProperty id;
	private StringProperty nombre;

	public StudyCenter(int id, String nombre) { 
		this.id = new SimpleIntegerProperty(id);
		this.nombre = new SimpleStringProperty(nombre);
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
	
	public static void getData(Connection connection, ObservableList<StudyCenter> list) {
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(
					"SELECT id_centro_estudio, "
					+ "nombre "
					+ "FROM centro_estudio"
			);
			while (result.next()) {
				list.add(
						new StudyCenter(
							result.getInt("id_centro_estudio"),
							result.getString("nombre")
						)
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return nombre.get();
	}
}