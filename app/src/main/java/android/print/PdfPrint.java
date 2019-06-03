package android.print;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;

public class PdfPrint extends PrintDocumentAdapter {

    private static final String TAG = PdfPrint.class.getSimpleName();
    private PrintAttributes printAttributes;

    public PdfPrint(PrintAttributes printAttributes) {
        this.printAttributes = printAttributes;
    }


    public void print(final PrintDocumentAdapter printAdapter, final File path, final String fileName) {
        printAdapter.onLayout(null, printAttributes, null, new LayoutResultCallback() {
            @Override
            public void onLayoutFinished(PrintDocumentInfo info, boolean changed) {
                super.onLayoutFinished(info, changed);
                printAdapter.onWrite(new PageRange[]{PageRange.ALL_PAGES}, getOutputFile(path, fileName), new CancellationSignal(), new PrintDocumentAdapter.WriteResultCallback() {
                    @Override
                    public void onWriteFinished(PageRange[] pages) {
                        super.onWriteFinished(pages);

                    }
                });
            }

            @Override
            public void onLayoutFailed(CharSequence error) {
                super.onLayoutFailed(error);
            }

            @Override
            public void onLayoutCancelled() {
                super.onLayoutCancelled();
            }
        }, null);
    }

    private ParcelFileDescriptor getOutputFile(File path, String fileName) {
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, fileName);
        try {
            file.createNewFile();
            return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE);
        } catch (Exception e) {
            Log.e(TAG, "Failed to open ParcelFileDescriptor", e);
        }
        return null;

    }

    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {

    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {

    }
}
