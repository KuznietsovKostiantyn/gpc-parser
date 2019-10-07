package kuznietsov.gpcparser.model;

import kuznietsov.gpcparser.parser.ReadTypes;
import kuznietsov.gpcparser.parser.exceptions.EofException;
import kuznietsov.gpcparser.parser.exceptions.ValidationException;
import kuznietsov.gpcparser.parser.gpc.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class GpcModel extends FileModel {


    private String accountNumber;

    private String owner;

    private LocalDate openingBalanceDate;

    private BigDecimal openingBalance;

    private String openingBalanceSign;

    private BigDecimal closingBalance;

    private String closingBalanceSign;

    private BigDecimal debitSum;

    private String debitSign;

    private BigDecimal creditSum;

    private String creditSign;

    private Integer serialNumber;

    private LocalDate statementDate;

    private List<FileRecord> fileRecords = new ArrayList<>();

    @Override
    public void parse(InputStream in) {
        try{

            InputStreamReader reader = new InputStreamReader(in, "Windows-1250");
            BufferedReader br = new BufferedReader(reader);
            String fileType;
            parseHead(br);

            while(true){

                try {
                    fileType = ReadTypes.readString(br, 3);

                    if (fileType.isEmpty())
                        break;

                    switch (fileType) {
                        case ("075"):
                            addTransactionInfo(br);
                            break;
                        case ("076"):
                            addSupplementaryDetail(br);
                            break;
                        case ("078"):
                            addMessageToBeneficiary1(br);
                            break;
                        case ("079"):
                            addMessageToBeneficiary2(br);
                            break;
                    }
                } catch (EofException e){

                    boolean includeRecord = fileRecords.stream().anyMatch(record -> record instanceof GpcRecord075);
                    if (!includeRecord)
                        throw new ValidationException("File should include transaction information");
                    break;
                }
            }
        }
        catch (Exception ex){
           log.error("Cannot import file", ex);
        }

    }

    private void addMessageToBeneficiary2(BufferedReader br) throws Exception {

        FileRecord fileRecord = new GpcRecord079();
        fileRecord.parse(br);
        fileRecords.add(fileRecord);
    }

    private void addMessageToBeneficiary1(BufferedReader br) throws Exception {

        FileRecord fileRecord = new GpcRecord078();
        fileRecord.parse(br);
        fileRecords.add(fileRecord);
    }

    private void addSupplementaryDetail(BufferedReader br) throws Exception {

        FileRecord fileRecord = new GpcRecord076();
        fileRecord.parse(br);
        fileRecords.add(fileRecord);
    }

    private void addTransactionInfo(BufferedReader br) throws Exception {

        FileRecord fileRecord = new GpcRecord075();
        fileRecord.parse(br);
        fileRecords.add(fileRecord);
    }

    private void parseHead(BufferedReader br) throws Exception {

        if (!ReadTypes.readString(br,3).equals("074"))
            throw new ValidationException("Missing statement header");

        accountNumber = ReadTypes.readString(br,16);
        owner = ReadTypes.readString(br,20);
        openingBalanceDate = ReadTypes.readDate(br);
        openingBalance = ReadTypes.readDecimal(br, 14);
        openingBalanceSign = ReadTypes.readString(br,1);
        closingBalance = ReadTypes.readDecimal(br, 14);
        closingBalanceSign = ReadTypes.readString(br,1);
        debitSum = ReadTypes.readDecimal(br, 14);
        debitSign = ReadTypes.readString(br,1);
        creditSum = ReadTypes.readDecimal(br, 14);
        creditSign = ReadTypes.readString(br,1);
        serialNumber = ReadTypes.readInteger(br,3);
        statementDate = ReadTypes.readDate(br);
        ReadTypes.readString(br,16);

        log.info(this.toString());
    }

    public String returnOpeningBalance(){ return openingBalanceSign+openingBalance;}

    public String returnClosingBalance(){return closingBalanceSign+closingBalance; }
}

