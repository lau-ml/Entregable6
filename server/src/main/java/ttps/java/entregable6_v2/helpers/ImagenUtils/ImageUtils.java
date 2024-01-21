package ttps.java.entregable6_v2.helpers.ImagenUtils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public  class ImageUtils {
    private static final String RUTA_ALMACENAMIENTO_IMAGENES = "C:/Users/lauta/OneDrive/Escritorio/Entregable6/cliente/src/assets";

    public static String guardarImagen(MultipartFile imagen) throws IOException
    {
        String nombreArchivo = imagen.getOriginalFilename();
        Path rutaImagen = Paths.get(RUTA_ALMACENAMIENTO_IMAGENES, nombreArchivo);
        Files.write(rutaImagen, imagen.getBytes());
        return nombreArchivo;
    }
}
