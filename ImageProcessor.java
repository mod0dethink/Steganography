import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageProcessor {

    /**
     * 指定されたファイルパスから画像を読み込み、BufferedImageオブジェクトとして返す
     * 
     * @param filePath 読み込む画像ファイルのパス
     * @return 読み込まれた画像データを含むBufferedImageオブジェクト
     * @throws IOException 画像の読み込みに失敗した場合
     */
    public static BufferedImage loadImage(String filePath) throws IOException {
        File file = new File(filePath);
        return ImageIO.read(file);
    }

    /**
     * 指定されたBufferedImageオブジェクトを画像ファイルとして保存
     * 
     * @param image 保存する画像データを含むBufferedImageオブジェクト
     * @param filePath 画像を保存するファイルのパス
     * @throws IOException 画像の保存に失敗した場合
     */
    public static void saveImage(BufferedImage image, String filePath) throws IOException {
        File file = new File(filePath);
        ImageIO.write(image, "png", file);
    }

    /**
     * 画像にデータを埋め込み、変更された画像をBufferedImageオブジェクトとして返す
     * 
     * @param image データを埋め込む元の画像データを含むBufferedImageオブジェクト
     * @param data 埋め込むデータのバイト配列
     * @return データが埋め込まれた後の画像データを含むBufferedImageオブジェクト
     */
    public static BufferedImage embedDataIntoImage(BufferedImage image, byte[] data) {
        // データサイズをバイト配列に変換（4バイト）
        byte[] dataSize = ByteBuffer.allocate(4).putInt(data.length).array();
        // データサイズ情報と実データを結合
        byte[] combinedData = new byte[dataSize.length + data.length];
        System.arraycopy(dataSize, 0, combinedData, 0, dataSize.length);
        System.arraycopy(data, 0, combinedData, dataSize.length, data.length);

        int width = image.getWidth();
        int height = image.getHeight();
        int dataIndex = 0;
        int dataLength = combinedData.length;

        for (int y = 0; y < height && dataIndex < dataLength; y++) {
            for (int x = 0; x < width && dataIndex < dataLength; x++) {
                int color = image.getRGB(x, y);
                int red = (color >> 16) & 0xFF;
                int green = (color >> 8) & 0xFF;
                int blue = color & 0xFF;

                // データを埋め込むために、青色成分の最下位ビットを変更
                blue = (blue & 0xFE) | ((combinedData[dataIndex] >> (7 - (dataIndex % 8))) & 1);

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

    /**
     * 画像から埋め込まれたデータを抽出し、バイト配列として返す
     * 
     * @param image データを抽出する画像データを含むBufferedImageオブジェクト
     * @return 抽出されたデータのバイト配列
     * @throws IOException 抽出プロセス中にIOエラーが発生した場合
     */
    public static byte[] extractDataFromImage(BufferedImage image) throws IOException {
        // 最初の4バイトからデータサイズを抽出するための準備
        byte[] sizeInfo = new byte[4];
        extractBitsFromImage(image, sizeInfo, 0, 32);
        int dataSize = ByteBuffer.wrap(sizeInfo).getInt();

        // 実際のデータを抽出する
        byte[] data = new byte[dataSize];
        extractBitsFromImage(image, data, 32, dataSize * 8);
        return data;
    }

    private static void extractBitsFromImage(BufferedImage image, byte[] data, int startBit, int bitsToExtract) {
        int width = image.getWidth();
        int height = image.getHeight();
        int bitIndex = startBit;
        for (int y = 0; y < height && bitIndex < startBit + bitsToExtract; y++) {
            for (int x = 0; x < width && bitIndex < startBit + bitsToExtract; x++) {
                int color = image.getRGB(x, y);
                int blue = color & 0xFF;
                int bit = (blue & 1);
                int dataIndex = (bitIndex - startBit) / 8;
                int bitPosition = 7 - (bitIndex - startBit) % 8;
                data[dataIndex] = (byte) ((data[dataIndex] & ~(1 << bitPosition)) | (bit << bitPosition));
                bitIndex++;
            }
        }
    }
}
