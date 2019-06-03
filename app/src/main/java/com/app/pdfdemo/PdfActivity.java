package com.app.pdfdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.print.PdfPrint;
import android.print.PrintAttributes;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

public class PdfActivity extends BaseActivity {

    private WebView myWebView;
    private String invoiceNo = "rajneesh patwal";
    private String invoiceDate = "";
    private String dueDate = "";
    private String senderInfo = "";
    private String recipientName = "";
    private String paymentMethod = "";
    private String total = "";
    private String totalAmount = "";
    private String imageUrl = "";


    @Override
    protected void initView() {

        myWebView = findViewById(R.id.myWebView);
        if (getIntent() != null) {
            invoiceDate = getIntent().getStringExtra("invoiceDate");
            dueDate = getIntent().getStringExtra("dueDate");
            senderInfo = getIntent().getStringExtra("senderInfo");
            recipientName = getIntent().getStringExtra("recipientName");
            paymentMethod = getIntent().getStringExtra("paymentMethod");
            total = getIntent().getStringExtra("total");
            totalAmount = getIntent().getStringExtra("totalAmount");
            imageUrl = getIntent().getStringExtra("imageUrl");
        }

        //add webview client to handle event of loading
        myWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //if page loaded successfully then show print button
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });

        //prepare your html content which will be show in webview
        String htmlDocument = "<html>\n" +
                "    <head>\n" +
                "        <meta content=\"text/html; charset=utf-8\" http-equiv=\"content-type\">\n" +
                "            <title>A simple, clean, and responsive HTML invoice template</title>\n" +
                "            <style>\n" +
                "                .invoice-box{\n" +
                "                    max-width:800px;\n" +
                "                    margin:auto;\n" +
                "                    padding:30px;\n" +
                "                    /*border:1px solid #eee;*/\n" +
                "                    /*box-shadow:0 0 10px rgba(0, 0, 0, .15); */\n" +
                "                    font-size:16px;\n" +
                "                    line-height:24px;\n" +
                "                    font-family:'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif;\n" +
                "                    color:#555;\n" +
                "                }\n" +
                "            \n" +
                "            .invoice-box table{\n" +
                "                width:100%;\n" +
                "                line-height:inherit;\n" +
                "                text-align:left;\n" +
                "            }\n" +
                "            \n" +
                "            .invoice-box table td{\n" +
                "                padding:5px;\n" +
                "                vertical-align:top;\n" +
                "            }\n" +
                "            \n" +
                "            .invoice-box table tr td:nth-child(2){\n" +
                "                text-align:right;\n" +
                "            }\n" +
                "            \n" +
                "            .invoice-box table tr.top table td{\n" +
                "                padding-bottom:20px;\n" +
                "            }\n" +
                "            \n" +
                "            .invoice-box table tr.top table td.title{\n" +
                "                font-size:45px;\n" +
                "                line-height:45px;\n" +
                "                color:#333;\n" +
                "            }\n" +
                "            \n" +
                "            .invoice-box table tr.information table td{\n" +
                "                padding-bottom:40px;\n" +
                "            }\n" +
                "            \n" +
                "            .invoice-box table tr.heading td{\n" +
                "                background:#eee;\n" +
                "                border-bottom:1px solid #ddd;\n" +
                "                font-weight:bold;\n" +
                "            }\n" +
                "            \n" +
                "            .invoice-box table tr.details td{\n" +
                "                padding-bottom:20px;\n" +
                "            }\n" +
                "            \n" +
                "            .invoice-box table tr.item td{\n" +
                "                border-bottom:1px solid #eee;\n" +
                "            }\n" +
                "            \n" +
                "            .invoice-box table tr.item.last td{\n" +
                "                border-bottom:none;\n" +
                "            }\n" +
                "            \n" +
                "            .invoice-box table tr.total td:nth-child(2){\n" +
                "                border-top:2px solid #eee;\n" +
                "                font-weight:bold;\n" +
                "            }\n" +
                "            \n" +
                "            @media only screen and (max-width: 600px) {\n" +
                "                .invoice-box table tr.top table td{\n" +
                "                    width:100%;\n" +
                "                    display:block;\n" +
                "                    text-align:center;\n" +
                "                }\n" +
                "                \n" +
                "                .invoice-box table tr.information table td{\n" +
                "                    width:100%;\n" +
                "                    display:block;\n" +
                "                    text-align:center;\n" +
                "                }\n" +
                "            }\n" +
                "            </style>\n" +
                "            </head>\n" +
                "    <body>\n" +
                "        <div class=\"invoice-box\">\n" +
                "            <table cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                <tbody>\n" +
                "                    <tr class=\"top\">\n" +
                "                        <td colspan=\"2\">\n" +
                "                            <table>\n" +
                "                                <tbody>\n" +
                "                                    <tr>\n" +
                "                                        <td class=\"title\"> <img src=" + imageUrl + " style=\"width:100%; max-width:300px; background-color: #cdcdcd\">\n" +
                "                                            </td>\n" +
                "                                        <td> Invoice : " + invoiceNo + " <br>\n" +
                "                                            #INVOICE_DATE : " + invoiceDate + "<br>\n" +
                "                                            #DUE_DATE# : " + dueDate + "</td>\n" +
                "                                    </tr>\n" +
                "                                </tbody>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr class=\"information\">\n" +
                "                        <td colspan=\"2\">\n" +
                "                            <table>\n" +
                "                                <tbody>\n" +
                "                                    <tr>\n" +
                "                                        <td> #SENDER_INFO# : " + senderInfo + " </td>\n" +
                "                                        <td> #RECIPIENT_INFO# :" + recipientName + "</td>\n" +
                "                                    </tr>\n" +
                "                                </tbody>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr class=\"heading\">\n" +
                "                        <td> Payment Method </td>\n" +
                "                        <td> <br>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr class=\"details\">\n" +
                "                        <td> #PAYMENT_METHOD# </td>\n" +
                "                        <td> <br>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr class=\"heading\">\n" +
                "                        <td> Item </td>\n" +
                "                        <td> Price </td>\n" +
                "                    </tr>\n" +
                "                    #ITEMS#\n" +
                "                    <tr class=\"total\">\n" +
                "                        <td><br>\n" +
                "                        </td>\n" +
                "                        <td> Total: #TOTAL_AMOUNT# </td>\n" +
                "                    </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "        \n" +
                "    </body>\n" +
                "</html>\n";

        //load your html to webview
        myWebView.loadData(htmlDocument, "text/HTML", "UTF-8");
//        myWebView.loadUrl("file:///android_asset/invoice.html");


    }

    private void createWebPrintJob(WebView webView) {

        //create object of print manager in your device
        String jobName = getString(R.string.app_name) + " Document";
        PrintAttributes attributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/PDFTest/");
        PdfPrint pdfPrint = new PdfPrint(attributes);
        pdfPrint.print(webView.createPrintDocumentAdapter(jobName), path, "output_" + System.currentTimeMillis() + ".pdf");
    }

    //perform click pdf creation operation on click of print button click
    public void printPDF(View view) {

        if (isStoragePermissionGranted()) {
            createWebPrintJob(myWebView);
            finish();
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //resume tasks needing this permission
            createWebPrintJob(myWebView);

        }
    }

    @Override
    protected int getLayoutById() {
        return R.layout.pdf_activity;
    }
}
