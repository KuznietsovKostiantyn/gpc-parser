package kuznietsov.gpcparser.parser.gpc;

import kuznietsov.gpcparser.parser.ReadTypes;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Data
public class GpcRecord075 implements FileRecord {

    private String accountNumber;

    private String counterpartyAccountNumber;

    private String transactionIdentifier;

    private BigDecimal transactionAmount;

    private Integer accountingType;

    private String variableCode;

    private String counterpartyBankCode;

    private String constantCode;

    private String specificCode;

    private LocalDate valueDate;

    private String transactionDescription;

    private String currencyCode;

    private LocalDate postingDate;



    @Override
    public void parse(BufferedReader br) throws Exception{

        accountNumber = ReadTypes.readString(br, 16);
        counterpartyAccountNumber = ReadTypes.readString(br, 16);
        transactionIdentifier = ReadTypes.readString(br, 13);
        transactionAmount = ReadTypes.readDecimal(br, 12);
        accountingType = ReadTypes.readInteger(br, 1);
        variableCode = ReadTypes.readString(br, 10);
        ReadTypes.readString(br, 2);
        counterpartyBankCode = ReadTypes.readString(br, 4);
        constantCode = ReadTypes.readString(br, 4);
        specificCode = ReadTypes.readString(br, 10);
        valueDate = ReadTypes.readDate(br);
        transactionDescription = ReadTypes.readString(br, 20);
        currencyCode = ReadTypes.readString(br, 5);
        postingDate = ReadTypes.readDate(br);
        ReadTypes.readString(br, 2);
        log.info(this.toString());

    }
}
