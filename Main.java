import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class Main {
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
        System.out.print("秘匿するファイル名を入力してください:");
        String selectedFileName = scanner.nextLine();
        String filePath = findFilePath(listOfFiles, selectedFileName);

        if (filePath.isEmpty()) {
            System.out.println("存在しません");
            return;
        }

        System.out.print("秘匿データを隠す先の画像ファイル名(.png)を入力してください:");
        String selectedImageName = scanner.nextLine();
        String imagePath = findFilePath(listOfFiles, selectedImageName);

        if (!imagePath.endsWith(".png")) {
            System.out.println("エラー: .png形式のファイルを選択してください。");
            return;
        } else if (imagePath.isEmpty()) {
            System.out.println("存在しません");
            return;
        }

        // 出力画像のパスを定義
        String outputImagePath = imagePath.replace(".png", "_stego.png");

        try {
            byte[] fileData = FileHandler.readFile(filePath);
            BufferedImage image = ImageProcessor.loadImage(imagePath);
            BufferedImage stegoImage = ImageProcessor.embedDataIntoImage(image, fileData);
            ImageProcessor.saveImage(stegoImage, outputImagePath);
            System.out.println("処理が完了しました。出力ファイル: " + outputImagePath);
        } catch (IOException e) {
            System.err.println("エラーが発生しました: " + e.getMessage());
        }
    }

    /**
     * 指定されたファイル名に一致するファイルの絶対パスをリストから検索して返す
     * 
     * @param listOfFiles 検索対象のファイルオブジェクトの配列
     * @param fileName 検索するファイル名
     * @return 一致するファイルの絶対パス。一致するファイルがない場合は空文字列を返す
     */
    private static String findFilePath(File[] listOfFiles, String fileName) {
        for (File file : listOfFiles) {
            if (file.getName().equals(fileName)) {
                return file.getAbsolutePath();
            }
        }
        return "";
    }
}