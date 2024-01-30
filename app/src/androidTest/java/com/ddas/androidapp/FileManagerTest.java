package com.ddas.androidapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.ddas.androidapp.util.FileManager;
import com.ddas.androidapp.util.FileManagerConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(AndroidJUnit4.class)
public class FileManagerTest {

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void testSaveBitmapToPdf() {
        Bitmap mockBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        String testName = "testName";
        String testDescription = "testDescription";
        String testTags = "testTags";

        FileManager.saveBitmapToPdf(context, mockBitmap, testName, testDescription, testTags);

        String expectedFilePath = context.getFilesDir() + "/" + FileManagerConstants.PDFS_FOLDER + "/" + testName + ".pdf";
        File pdfFile = new File(expectedFilePath);

        assertTrue(pdfFile.exists());

        if (pdfFile.exists()) {
            pdfFile.delete();
        }
    }

    @Test
    public void testConvertBitmapToPdf() {
        Bitmap testBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        PdfDocument pdfDocument = FileManager.convertBitmapToPdf(testBitmap);

        assertNotNull(pdfDocument);
        assertEquals(1, pdfDocument.getPages().size());

        PdfDocument.PageInfo page = pdfDocument.getPages().get(0);
        assertNotNull(page);

        pdfDocument.close();
    }
}