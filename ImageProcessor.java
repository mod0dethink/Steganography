import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {

    public static BufferedImage loadImage(String filePath) throws IOException {
        File file = new File(filePath);
        return ImageIO.read(file);
    }

    public static void saveImage(BufferedImage image, String filePath) throws IOException {
        File file = new File(filePath);
        ImageIO.write(image, "png", file);
    }

    public static BufferedImage embedDataIntoImage(BufferedImage image, byte[] data) {
        int width = image.getWidth();
        int height = image.getHeight();
        int dataIndex = 0;
        int dataLength = data.length;

        for (int y = 0; y < height && dataIndex < dataLength; y++) {
            for (int x = 0; x < width && dataIndex < dataLength; x++) {
                int color = image.getRGB(x, y);
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = color & 0xFF;

                // データを埋め込むために、青色成分の最下位ビットを変更
                blue = (blue & 0xFE) | ((data[dataIndex] >> (7 - (dataIndex % 8))) & 1);

                // 8ピクセルごとにデータの次のビットに移動
                if (x % 8 == 7) {
                    dataIndex++;
                }

                int newColor = (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, newColor);
            }
        }

        return image;
    }
}
