package kuznietsov.gpcparser.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.InputStream;

@Data
public abstract class FileModel {

    @Id
    private String id;

    public abstract void parse(InputStream in);



}
