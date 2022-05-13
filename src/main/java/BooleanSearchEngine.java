import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private List<PageEntry> entryList = new ArrayList<>();
    private Map<String, PageEntry> mapForAllPages = new TreeMap<>();
    private List<Map<String, PageEntry>> mapList = new ArrayList<>();

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
                        mapForAllPages.put(entry.getKey(), new PageEntry(doc.getDocumentInfo().getTitle(), page, entry.getValue()));
                    }
                    mapList.add(mapForAllPages);
                    mapForAllPages = new TreeMap<>();
                    wordsToSearch.clear();
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        for (Map<String, PageEntry> entryMap : mapList) {
            for (Map.Entry<String, PageEntry> entry : entryMap.entrySet()) {
                if (word.equals(entry.getKey())) {
                    entryList.add(entry.getValue());
                }
            }
        }
        Collections.sort(entryList);
        return entryList;
    }
}
