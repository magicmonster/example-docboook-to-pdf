package com.magicmonster.docbook.example;

import net.sf.saxon.Transform;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by jurn on 07/02/2015.
 */
public class RunSaxon {
    /**
     *  mvn exec:java -Dexec.mainClass=com.magicmonster.docbook.example.RunSaxon
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Transform.main(new String[]{"-s:src/main/docbook/article.xml", "-xsl:docbook-xsl/docbook/fo/docbook.xsl", "-o:target/output.fo"});

        // and then run FOP
//        http://xmlgraphics.apache.org/fop/1.1/embedding.html

        FopFactory fopFactory = FopFactory.newInstance();
        OutputStream out = new BufferedOutputStream(new FileOutputStream(new File("target/output.pdf")));
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(); // identity transformer from saxon
        System.out.println(transformer);
        Source src = new StreamSource(new File("target/output.fo"));

        // Resulting SAX events (the generated FO) must be piped through to FOP
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
        Result res = new SAXResult(fop.getDefaultHandler());

        // Step 6: Start XSLT transformation and FOP processing
        transformer.transform(src, res);
        out.flush();

        out.close();
    }
}
