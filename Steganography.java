import java.awt.image.BufferedImage;
import java.io.IOException;

public class Steganography {

    public static void embedFileIntoImage(String imagePath, String filePath, String outputImagePath) throws IOException {
        // 画像を読み込む
        BufferedImage image = ImageProcessor.loadImage(imagePath);

        // 埋め込むファイルを読み込む
        byte[] fileContent = FileHandler.readFile(filePath);

        // ファイルの内容を画像に埋め込む
        BufferedImage resultImage = ImageProcessor.embedDataIntoImage(image, fileContent);

        // 結果の画像を保存する
        ImageProcessor.saveImage(resultImage, outputImagePath);
    }

}
