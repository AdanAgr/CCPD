import java.io.Serializable;

/**
 * Objeto serializable que contiene la porción (trozo) de la imagen
 * y la información necesaria para procesarla.
 */
public class ImageChunkMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public int startX, startY;   // Coordenadas de inicio del trozo
    public int width, height;    // Dimensiones del trozo
    public int[] pixels;         // Array con los píxeles del trozo
    public int operationCode;    // Código para indicar qué operación/filtro aplicar

    public ImageChunkMessage(int startX, int startY, int width, int height, int[] pixels, int operationCode) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.operationCode = operationCode;
    }
}
