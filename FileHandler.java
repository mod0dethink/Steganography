import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandler {

    /**
     * ファイルをバイト配列として読み込む
     * @param filePath 読み込むファイルのパス
     * @return ファイルの内容を含むバイト配列
     * @throws IOException ファイル読み込み時にエラーが発生した場合
     */
    public static byte[] readFile(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileContent = new byte[(int) file.length()];
        
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileContent);
        }
        
        return fileContent;
    }

    /**
     * バイト配列の内容をファイルに書き込む
     * @param filePath 書き込むファイルのパス
     * @param content 書き込む内容を含むバイト配列
     * @throws IOException ファイル書き込み時にエラーが発生した場合
     */
    public static void writeFile(String filePath, byte[] content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(content);
        }
    }

    /**
     * 指定されたディレクトリ内のファイル一覧を取得
     * @param directoryPath ディレクトリのパス
     * @return ディレクトリ内のファイルオブジェクトの配列
     */
    public static File[] listFiles(String directoryPath) {
        File folder = new File(directoryPath);
        return folder.listFiles();
    }
}