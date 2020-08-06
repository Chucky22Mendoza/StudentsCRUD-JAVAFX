package model;

/* Java Bean
 * Class: Degree */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Degree{
	private IntegerProperty id;
	private StringProperty nombre;
	private IntegerProperty cantidadAsignaturas;

	public Degree(int id, String nombre, int cantidadAsignaturas) { 
		this.id = new SimpleIntegerProperty(id);
		this.nombre = new SimpleStringProperty(nombre);
		this.cantidadAsignaturas = new SimpleIntegerProperty(cantidadAsignaturas);
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
	
	//Metodos atributo: cantidadAsignaturas
	public int getCantidadAsignaturas() {
		return cantidadAsignaturas.get();
	}
	
	public void setCantidadAsignaturas(int cantidadAsignaturas) {
		this.cantidadAsignaturas = new SimpleIntegerProperty(cantidadAsignaturas);
	}
	
	public IntegerProperty CantidadAsignaturasProperty() {
		return cantidadAsignaturas;
	}
	
	public static void getData(Connection connection, ObservableList<Degree> list) {
		try {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(
					"SELECT id_carrera, "
					+ "nombre, "
					+ "cantidad_asignaturas "
					+ "FROM carrera"
			);
			while (result.next()) {
				list.add(
						new Degree(
							result.getInt("id_carrera"),
							result.getString("nombre"),
							result.getInt("cantidad_asignaturas")
						)
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return nombre.get() + " (" + cantidadAsignaturas.get() + ")";
	}
}