package kuznietsov.gpcparser.parser.gpc;

import java.io.BufferedReader;

public interface FileRecord {
     void parse(BufferedReader br) throws Exception;
}
