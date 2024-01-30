import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // 埋め込むデータのファイルパス
        String filePath = "";
        // 埋め込む先の画像のパス
        String imagePath = "";
        // 埋め込まれた画像の保存先のパス
        String outputImagePath = "";

        try {
            // ファイルを画像に埋め込む
            Steganography.embedFileIntoImage(imagePath, filePath, outputImagePath);
            System.out.println("指定されたファイルを指定された画像に埋め込みました");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
