package kuznietsov.gpcparser.parser.gpc;

import kuznietsov.gpcparser.parser.ReadTypes;
import lombok.Data;

import java.io.BufferedReader;

@Data
public class GpcRecord079 implements FileRecord {

    private String mixedTransactionInfo1;

    private String mixedTransactionInfo2;

    private String mixedTransactionInfo3;

    @Override
    public void parse(BufferedReader br) throws Exception {

        mixedTransactionInfo1 = ReadTypes.readString(br,35);
        mixedTransactionInfo2 = ReadTypes.readString(br, 35);
        mixedTransactionInfo3 = ReadTypes.readString(br, 35);
        ReadTypes.readString(br, 18);
        ReadTypes.readString(br, 2);
    }
}
