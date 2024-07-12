package com.ar.api.Persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ar.api.Entidades.Pelicula;


public class PeliculaDao {
    
    public Long cargarPelicula(Pelicula pelicula){
        String insertPelicucalSQl = "INSERT INTO pelicula (titulo, duracion, genero, imagen) VALUES (?, ?, ?, ?)"; 

        try(Connection conexion = dbConection.getConnection();
        PreparedStatement preparedStatement = conexion.prepareStatement(insertPelicucalSQl, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, pelicula.getTitulo());
            preparedStatement.setString(2, pelicula.getDuracion());
            preparedStatement.setString(3, pelicula.getGenero());
            preparedStatement.setString(4, pelicula.getImagen());

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    System.out.println("Pelicula Insertada correctamente " + id);
                    return id;
                }else{
                    System.out.println("Error al obtener el Id de la  pelicula");
                    return  null;
                }
            }else{
                System.out.println("Error al insertar pelicula");
                return  null;
            }

        } catch (Exception e) {
           System.err.println("error al insertar la pelicula" + e.getMessage());
           return  null;
        }
    }

    public List<Pelicula> listarPeliculas (){
        String selectPeliculasSql = "SELECT * FROM pelicula";
        List<Pelicula> peliculas = new ArrayList<>();
        
        try(Connection conexion = dbConection.getConnection();
        PreparedStatement preparedStatement = conexion.prepareStatement(selectPeliculasSql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Pelicula pelicula = new Pelicula();
                pelicula.setId(resultSet.getLong("id"));
                pelicula.setTitulo(resultSet.getString("titulo"));
                pelicula.setDuracion(resultSet.getString("duracion"));
                pelicula.setGenero(resultSet.getString("genero"));
                pelicula.setImagen(resultSet.getString("imagen"));
                peliculas.add(pelicula);
    }
    } catch (SQLException e) {
    System.err.println("Error al listar peliculas" + e.getMessage());
    }
    return peliculas;
}

public Pelicula listarPorId(long id){
    Pelicula pelicula = null;
    
    try(Connection connection = dbConection.getConnection(); // Obtiene una conexión a la base de datos.
    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM pelicula WHERE id = ?")) {
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            pelicula = new Pelicula();

            pelicula.setId(resultSet.getLong("id"));
            pelicula.setTitulo(resultSet.getString("titulo"));
            pelicula.setDuracion(resultSet.getString("duracion"));
            pelicula.setGenero(resultSet.getString("genero"));
            pelicula.setImagen(resultSet.getString("imagen"));

        } 
    }catch (SQLException e) {
        System.err.println("Error al listar pelicula por id" + e.getMessage());
    }
    return pelicula;
}

public void updatePeliculas(Pelicula pelicula) throws IllegalAccessException{
    if (pelicula.getId() == null) {
        throw new IllegalAccessException("El Id de la pelicula no puede ser nulo");
    }

    String actulizarPeliculaSQL = "UPDATE pelicula SET titulo = ?, duracion = ?, genero = ?, imagen= ? WHERE id = ?"; // SQL para actualizar pelicula.
    try(Connection connection = dbConection.getConnection();
    PreparedStatement preparedStatement = connection.prepareStatement(actulizarPeliculaSQL)) {
        
        preparedStatement.setString(1, pelicula.getTitulo());
        preparedStatement.setString(2, pelicula.getDuracion());
        preparedStatement.setString(3, pelicula.getGenero());
        preparedStatement.setString(4, pelicula.getImagen());
        preparedStatement.setLong(5, pelicula.getId());

        int result = preparedStatement.executeUpdate();
        if (result > 0) {
            System.out.println("Pelicula actualizada con exito");
            } else {
                System.out.println("No se pudo actualizar la pelicula");
                }
        
        } catch (SQLException e) {
            System.err.println("error al actualizar pelicula" + e.getMessage());
        }
}

// metodo para eliminar una pelicula
public void deletePelicula(Long id) {
    
    String eliminarPeliculaSQL = "DELETE FROM pelicula WHERE id = ?";
    
    try (Connection connection = dbConection.getConnection(); 
         PreparedStatement preparedStatement = connection.prepareStatement(eliminarPeliculaSQL)) { 
        preparedStatement.setLong(1, id); 

        int result = preparedStatement.executeUpdate(); // Ejecuta la consulta y devuelve el número de filas afectadas.
        if (result > 0) {
            System.out.println("La Pelicula ha sido eliminada exitosamente."); 
        } else {
            System.out.println("Error al eliminar la pelicula."); 
        }
    } catch (SQLException e) {
        System.err.println("Error al eliminar la pelicula: " + e.getMessage()); 
    }
}


public List<Pelicula> buscarPeliculaPorTitulo(String titulo) {
    String buscarPeliculaSQL = "SELECT * FROM pelicula WHERE titulo LIKE ?";
    
    List<Pelicula> peliculas = new ArrayList<>(); // Crea una lista para almacenar las películas.
   
    
    try (Connection connection = dbConection.getConnection(); 
    
         PreparedStatement preparedStatement = connection.prepareStatement(buscarPeliculaSQL)) { 
            
        preparedStatement.setString(1, "%" + titulo + "%"); 
        ResultSet resultSet = preparedStatement.executeQuery(); 
        
        while (resultSet.next()) { // Itera sobre los resultados de la consulta.
            
            Pelicula pelicula = new Pelicula();
            pelicula.setId(resultSet.getLong("id"));
            pelicula.setTitulo(resultSet.getString("titulo"));
            pelicula.setDuracion(resultSet.getString("duracion"));
            pelicula.setGenero(resultSet.getString("genero"));
            pelicula.setImagen(resultSet.getString("imagen"));
            peliculas.add(pelicula); // Añade la película a la lista.
        }
    } catch (SQLException e) {
        System.err.println("Error al buscar las peliculas: " + e.getMessage()); 
    }
    return peliculas; 
}


}

