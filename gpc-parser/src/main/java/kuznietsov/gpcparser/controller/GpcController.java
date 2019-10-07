package kuznietsov.gpcparser.controller;

import kuznietsov.gpcparser.model.GpcModel;
import kuznietsov.gpcparser.parser.gpc.*;
import kuznietsov.gpcparser.service.GpcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GpcController {

    private final GpcRepository repository;

    @PostMapping("/import")
    public String importFile(@RequestParam("file") MultipartFile file){
        GpcModel model = new GpcModel();
        try {
            if(!file.isEmpty()) {
                model.parse(file.getInputStream());
                repository.save(model);
            }

        } catch (Exception e) {
            log.error("Cannot upload file", e);
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String getModels(Model model){
        model.addAttribute("gpcModels", repository.findAll());
        return "index";
    }
    @GetMapping("/details/{id}")
    public String getDetails(@PathVariable("id") String id, Model model){

        List<FileRecord> fileRecords = repository.findById(id).get().getFileRecords();
        model.addAttribute("gpc075", getListByObject(fileRecords, e -> e instanceof GpcRecord075));
        model.addAttribute("gpc076", getListByObject(fileRecords, e -> e instanceof GpcRecord076));
        model.addAttribute("gpc078", getListByObject(fileRecords, e -> e instanceof GpcRecord078));
        model.addAttribute("gpc079", getListByObject(fileRecords, e -> e instanceof GpcRecord079));
        return "file-details";
    }
    private List<FileRecord> getListByObject(List<FileRecord> records, Predicate<? super FileRecord> object){
        return records.stream()
                .filter(object)
                .collect(Collectors.toList());
    }
}
