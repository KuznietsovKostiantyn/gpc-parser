package kuznietsov.gpcparser.parser;

import kuznietsov.gpcparser.parser.exceptions.EofException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ReadTypes {

    public static String readString (BufferedReader br, int length) throws Exception{

        char [] line = new char [length];
        int eof = br.read(line, 0, length);
        if(eof == -1) throw new EofException();
        return new String(line);
    }
    public static LocalDate readDate ( BufferedReader br) throws Exception {

        char [] line = new char [6];
        int eof = br.read(line,0,6);
        if(eof == -1) throw new EofException();
        return LocalDate.parse(new String(line), DateTimeFormatter.ofPattern("ddMMyy"));
    }
    public static BigDecimal readDecimal (BufferedReader br, int length) throws Exception {

        char [] line = new char [length];
        int eof = br.read(line,0,length);
        if(eof == -1) throw new EofException();
        return new BigDecimal(line).multiply(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static Integer readInteger (BufferedReader br, int length) throws Exception {

        char [] line = new char [length];
        int eof = br.read(line,0,length);
        if(eof == -1) throw new EofException();
        return Integer.parseInt(new String(line));
    }
}
