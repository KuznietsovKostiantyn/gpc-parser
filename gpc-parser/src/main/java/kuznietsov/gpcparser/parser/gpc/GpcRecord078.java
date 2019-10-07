package kuznietsov.gpcparser.parser.gpc;

import kuznietsov.gpcparser.parser.ReadTypes;
import lombok.Data;

import java.io.BufferedReader;

@Data
public class GpcRecord078 implements FileRecord {

    private static String mixedTransactionInfo1;

    private static String mixedTransactionInfo2;

    private static String mixedTransactionInfo3;

    @Override
    public void parse(BufferedReader br) throws Exception {

        mixedTransactionInfo1 = ReadTypes.readString(br,35);
        mixedTransactionInfo2 = ReadTypes.readString(br, 35);
        mixedTransactionInfo3 = ReadTypes.readString(br, 54);
        ReadTypes.readString(br, 2);
    }
}
