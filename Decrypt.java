import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Decrypt {

    public static void main(String[] args) {
        // 現在のフォルダーからファイル一覧を取得
        File[] listOfFiles = FileHandler.listFiles(".");

        // ファイル一覧の表示
        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("復号化する画像ファイル名を入力してください:");
        String selectedImageName = scanner.nextLine();
        String imagePath = findFilePath(listOfFiles, selectedImageName);

        if (!imagePath.endsWith(".png")) {
            System.out.println("エラー: .png形式のファイルを選択してください。");
            return;
        } else if (imagePath.isEmpty()) {
            System.out.println("存在しません");
            return;
        }

        // 出力ファイルのパスを定義（同一フォルダ内）
        String outputPath = imagePath.replace(".png", "_decrypted");

        try {
            BufferedImage image = ImageProcessor.loadImage(imagePath);
            byte[] extractedData = ImageProcessor.extractDataFromImage(image);
            saveData(outputPath, extractedData);
            System.out.println("データの復号化が完了しました。出力ファイル: " + outputPath);
        } catch (IOException e) {
            System.err.println("エラーが発生しました: " + e.getMessage());
        }
    }

    /**
     * 指定されたファイル名に一致するファイルの絶対パスを返す
     * 
     * @param listOfFiles 検索対象のファイル配列
     * @param fileName 検索するファイル名
     * @return 一致するファイルが見つかった場合はその絶対パス、見つからない場合は空文字列
     */
    private static String findFilePath(File[] listOfFiles, String fileName) {
        for (File file : listOfFiles) {
            if (file.getName().equals(fileName)) {
                return file.getAbsolutePath();
            }
        }
        return "";
    }

    /**
     * 指定されたパスにデータをバイト配列として保存
     * 
     * @param outputPath 出力ファイルのパス
     * @param data 保存するデータのバイト配列
     * @throws IOException ファイル書き込み時にIOエラーが発生した場合
     */
    private static void saveData(String outputPath, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(new File(outputPath))) {
            fos.write(data);
        }
    }
}