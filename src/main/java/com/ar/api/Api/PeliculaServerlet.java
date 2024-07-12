package com.ar.api.Api;

import java.io.IOException;
import java.util.List;

import com.ar.api.Entidades.Pelicula;
import com.ar.api.Persistencia.PeliculaDao;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/pelicula")
public class PeliculaServerlet extends HttpServlet {
    
    private PeliculaDao peliculaDao = new PeliculaDao();
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Configurar cabeceras CORS
          resp.setHeader("Access-Control-Allow-Origin", "*"); 
          resp.setHeader("Access-Control-Allow-Methods", "*"); 
          resp.setHeader("Access-Control-Allow-Headers", "Content-Type"); 
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
    
        
        String idParam = req.getParameter("id");
        String searchParam = req.getParameter("buscar");
    
        try {
            if (idParam != null) {
                // Buscar película por ID
                Long id = Long.parseLong(idParam);
                Pelicula pelicula = peliculaDao.listarPorId(id);
                if (pelicula != null) {
                    String jsonResponse = objectMapper.writeValueAsString(pelicula);
                    resp.setContentType("application/json");
                    resp.getWriter().write(jsonResponse);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Pelicula no encontrada");
                }
            } else if (searchParam != null && !searchParam.isEmpty()) {
                // Buscar películas por título
                List<Pelicula> peliculas = peliculaDao.buscarPeliculaPorTitulo(searchParam);
                String jsonResponse = objectMapper.writeValueAsString(peliculas);
                resp.setContentType("application/json");
                resp.getWriter().write(jsonResponse);
            } else {
                // Obtener todas las películas
                List<Pelicula> peliculas = peliculaDao.listarPeliculas();
                String jsonResponse = objectMapper.writeValueAsString(peliculas);
                resp.setContentType("application/json");
                resp.getWriter().write(jsonResponse);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Configurar cabeceras CORS
        resp.setHeader("Access-Control-Allow-Origin", "*"); // Permitir acceso desde cualquier origen
        resp.setHeader("Access-Control-Allow-Methods", "*"); // Métodos permitidos
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type"); // Cabeceras permitidas
       // Establecer la codificación de caracteres
       req.setCharacterEncoding("UTF-8");
       resp.setCharacterEncoding("UTF-8");
       
       // Leer JSON del cuerpo de la solicitud y convertirlo en un objeto Pelicula
       Pelicula pelicula = objectMapper.readValue(req.getInputStream(), Pelicula.class);
       
       // Insertar la película en la base de datos
       Long id = peliculaDao.cargarPelicula(pelicula);
       
       // Convertir el id a json
       String jsonResponse = objectMapper.writeValueAsString(id);
       
       // Establecer el tipo de contenido de la respuesta a JSON
       resp.setContentType("application/json");
       
       // Escribir la respuesta JSON
       resp.getWriter().write(jsonResponse);
       
       // Establecer el estado de la respuesta a 201 (Creado)
       resp.setStatus(HttpServletResponse.SC_CREATED);
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Configurar cabeceras CORS
        resp.setHeader("Access-Control-Allow-Origin", "*"); 
        resp.setHeader("Access-Control-Allow-Methods", "*"); 
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type"); 
      // Establecer la codificación de caracteres
      req.setCharacterEncoding("UTF-8");
      resp.setCharacterEncoding("UTF-8");
  
      try {
          // Leer JSON del cuerpo de la solicitud y convertirlo en un objeto Pelicula
          Pelicula pelicula = objectMapper.readValue(req.getInputStream(), Pelicula.class);
          // levantar el id del queryParams e insertarlo en el objeto pelicula
          String idParam = req.getParameter("id");
          if (idParam != null) {
              Long id = Long.parseLong(idParam);
              pelicula.setId(id);
          }
  
          // Verificar que la película tenga un ID
          if (pelicula.getId() == null) {
              resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "El ID de la película es necesario para actualizar.");
              return;
          }
  
          // Actualizar la película en la base de datos
          peliculaDao.updatePeliculas(pelicula);
  
          // Establecer el tipo de contenido de la respuesta a JSON
          resp.setContentType("application/json");
  
          // Escribir la respuesta JSON
          resp.getWriter().write("{\"message\": \"Pelicula actualizada exitosamente\"}");
  
          // Establecer el estado de la respuesta a 200 (OK)
          resp.setStatus(HttpServletResponse.SC_OK);
      } catch (IllegalArgumentException e) {
          resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
      } catch (Exception e) {
          resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al actualizar la película");
          e.printStackTrace();
      }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         // Configurar cabeceras CORS
         resp.setHeader("Access-Control-Allow-Origin", "*"); 
         resp.setHeader("Access-Control-Allow-Methods", "*"); 
         resp.setHeader("Access-Control-Allow-Headers", "Content-Type"); 
       // Establecer la codificación de caracteres
       req.setCharacterEncoding("UTF-8");
       resp.setCharacterEncoding("UTF-8");

       // Obtener el parámetro 'id' de la URL
       String idParam = req.getParameter("id");
       if (idParam != null) {
           try {
               Long id = Long.parseLong(idParam);
               peliculaDao.deletePelicula(id);
               
               // Establecer el tipo de contenido de la respuesta a JSON
               resp.setContentType("application/json");
               
               // Escribir la respuesta JSON
               resp.getWriter().write("{\"message\": \"Pelicula eliminada exitosamente\"}");
               
               // Establecer el estado de la respuesta a 200 (OK)
               resp.setStatus(HttpServletResponse.SC_OK);
           } catch (NumberFormatException e) {
               resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
           }
       } else {
           resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado");
    }
}

}