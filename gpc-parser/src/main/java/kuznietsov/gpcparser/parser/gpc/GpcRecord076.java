package kuznietsov.gpcparser.parser.gpc;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;

@Slf4j
@Data
public class GpcRecord076 implements FileRecord {

    private String transactionIdentification;

    private String dateOfDebitingFromCounterparty;

    private String comment;

    @Override
    public void parse(BufferedReader br) throws Exception{

        char [] line = new char [26];
        br.read(line,0,26);
        transactionIdentification = new String(line);
        line = new char [6];
        br.read(line,0,6);
        dateOfDebitingFromCounterparty = new String(line);
        line = new char [92];
        br.read(line,0,92);
        comment = new String(line);
    }
}
