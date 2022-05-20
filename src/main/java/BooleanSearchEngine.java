import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private final Map<String, List<PageEntry>> mapForAllPages = new TreeMap<>();
    //  private  List<PageEntry> list = new ArrayList<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        File[] pdfsList = pdfsDir.listFiles();
        if (pdfsList == null) {
            throw new IOException("Файлы отсутствуют");
        } else {
            for (File pdfFile : pdfsList) {
                var doc = new PdfDocument(new PdfReader(pdfFile));
                Map<String, Integer> wordsToSearch = new HashMap<>();
                for (int page = 1; page < doc.getNumberOfPages(); page++) {
                    var text = PdfTextExtractor.getTextFromPage(doc.getPage(page));
                    var words = text.split("\\P{IsAlphabetic}+");
                    for (var word : words) {
                        if (word.isEmpty()) {
                            continue;
                        }
                        wordsToSearch.put(word.toLowerCase(), wordsToSearch.getOrDefault(word, 0) + 1);
                    }

                    for (Map.Entry<String, Integer> entry : wordsToSearch.entrySet()) {
                        List<PageEntry> list = new ArrayList<>();
                        if (mapForAllPages.containsKey(entry.getKey())) {
                            list = mapForAllPages.get(entry.getKey());
                        }
                        list.add(new PageEntry(doc.getDocumentInfo().getTitle(), page, entry.getValue()));
                        mapForAllPages.put(entry.getKey(), list);
                    }
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        if (mapForAllPages.containsKey(word.toLowerCase())) {
            return mapForAllPages.get(word.toLowerCase());
        }
        return Collections.emptyList();
    }
}
