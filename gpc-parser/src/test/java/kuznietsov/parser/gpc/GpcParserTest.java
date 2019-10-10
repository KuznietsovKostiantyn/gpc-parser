package kuznietsov.parser.gpc;

import kuznietsov.gpcparser.GpcParserApplication;
import kuznietsov.gpcparser.model.GpcModel;
import kuznietsov.gpcparser.parser.gpc.FileRecord;
import kuznietsov.gpcparser.parser.gpc.GpcRecord075;
import kuznietsov.gpcparser.service.GpcRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {GpcParserApplication.class})
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GpcParserTest {

    @Autowired
    GpcRepository gpcRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void cleanDb(){
        gpcRepository.deleteAll();
    }

    @Test
    public void testImportFile() throws Exception{

        cleanDb();
        FileInputStream fis = new FileInputStream("src/test/java/resources/Pohyby_na_uctu-1.gpc");
        MockMultipartFile file = new MockMultipartFile("file", fis);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/import").file(file).contentType(MediaType.ALL))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
        System.out.println(gpcRepository.findAll().size());
        Assert.assertEquals(1, gpcRepository.findAll().size());
    }
    @Test
    public void testFileElems() throws Exception{

        testImportFile();
        List<GpcModel> gpcModel = gpcRepository.findAll();
        Assert.assertEquals(4, gpcModel.get(0).getFileRecords().size());
        Assert.assertTrue(gpcModel.get(0).getFileRecords().get(0) instanceof GpcRecord075);
        Assert.assertTrue(gpcModel.get(0).getFileRecords().get(1) instanceof GpcRecord075);
        Assert.assertTrue(gpcModel.get(0).getFileRecords().get(2) instanceof GpcRecord075);
        Assert.assertTrue(gpcModel.get(0).getFileRecords().get(3) instanceof GpcRecord075);
    }
    @Test
    public void testParsedAttributes() throws Exception {

        testImportFile();
        List<GpcModel> gpcModels = gpcRepository.findAll();
        List<FileRecord> fileRecords = gpcModels.get(0).getFileRecords();

        GpcModel model = gpcModels.get(0);

        Assert.assertEquals("0000002600848022",model.getAccountNumber());
        Assert.assertEquals("Mgr. Yyyyyy, Xxxxx  ",model.getOwner());
        Assert.assertEquals( LocalDate.of(2019, 7, 25), model.getOpeningBalanceDate());
        Assert.assertEquals("+",model.getOpeningBalanceSign());
        Assert.assertEquals(0, model.getOpeningBalance().compareTo(new BigDecimal("35494.68")));
        Assert.assertEquals("+",model.getClosingBalanceSign());
        Assert.assertEquals(0, model.getClosingBalance().compareTo(new BigDecimal("17820.20")));
        Assert.assertEquals(0, model.getDebitSum().compareTo(new BigDecimal("115536.48")));
        Assert.assertEquals(0, model.getCreditSum().compareTo(new BigDecimal("97862.00")));
        Assert.assertEquals(Integer.valueOf(0), model.getSerialNumber());
        Assert.assertEquals( LocalDate.of(2019, 8, 25), model.getStatementDate());

        if (fileRecords.get(0) instanceof GpcRecord075){

            GpcRecord075 gpcRecord075 = ((GpcRecord075) fileRecords.get(0));
            Assert.assertEquals("0000002600848022", gpcRecord075.getAccountNumber());
            Assert.assertEquals("0000000000000000", gpcRecord075.getCounterpartyAccountNumber());
            Assert.assertEquals("0021382973418", gpcRecord075.getTransactionIdentifier());
            Assert.assertEquals(0, gpcRecord075.getTransactionAmount().compareTo(new BigDecimal("1596")));
            Assert.assertEquals(Integer.valueOf(1), gpcRecord075.getAccountingType());
            Assert.assertEquals("0000003101", gpcRecord075.getVariableCode());
            Assert.assertEquals("0000", gpcRecord075.getCounterpartyBankCode());
            Assert.assertEquals("0000", gpcRecord075.getConstantCode());
            Assert.assertEquals( LocalDate.of(2019, 7, 21), gpcRecord075.getValueDate());
            Assert.assertEquals( "Nákup: Misto1234-Obu", gpcRecord075.getTransactionDescription());
            Assert.assertEquals( "00203", gpcRecord075.getCurrencyCode());
            Assert.assertEquals( LocalDate.of(2019, 7, 25), gpcRecord075.getPostingDate());

        }
    }
}
