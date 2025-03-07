package com.ptit.asset.external;

import com.ptit.asset.domain.Category;
import com.ptit.asset.domain.Group;
import com.ptit.asset.domain.enumeration.MaterialStatus;
import com.ptit.asset.domain.enumeration.TimeAllocationType;
import com.ptit.asset.repository.*;
import com.ptit.asset.repository.data.Statistical;
import com.ptit.asset.util.TimeProviderUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class StatisticalExcelExporterProviderServiceImpl implements StatisticalExcelExporterProviderService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TimeProviderUtil timeProviderUtil;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private static final String [] markGroup = new String[] {
            "A","B","C","D","E","F","G","H"
    };
    private static final String [] markCategory = new String[] {
            "I","II","III","IV","V","VI","VII","VIII","IX","X"
    };

    private final StatisticalRepository statisticalRepository;
    private final GroupRepository groupRepository;
    private final CategoryRepository categoryRepository;

    public StatisticalExcelExporterProviderServiceImpl(StatisticalRepository statisticalRepository,
                                            GroupRepository groupRepository,
                                            CategoryRepository categoryRepository){

        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("Inventory");

        this.statisticalRepository = statisticalRepository;
        this.groupRepository = groupRepository;
        this.categoryRepository = categoryRepository;
    }

    // ------------------------------------------------------
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }
        if (value instanceof Integer){
            cell.setCellValue((Integer) value);
        }
        else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }
        else if (value instanceof Double){
            cell.setCellValue((Double) value);
        }
        else if (value instanceof Instant){
            cell.setCellValue(Date.from((Instant) value));
        }
        else if (value instanceof TimeAllocationType){
            cell.setCellValue(String.valueOf(value));
        }
        else if (value instanceof MaterialStatus){
            cell.setCellValue(String.valueOf(value));
        }
        else {
            assert value instanceof String;
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeHeaderLine() {
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        style.setFont(font);
//        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.FILL);

        createCell(row, 0, "STT", style);
        createCell(row, 1, "Tên Tài Sản", style);
        createCell(row, 2, "Mã Số", style);
        createCell(row, 3, "Nơi Sử Dụng", style);
        createCell(row, 4, "Nguyên Giá", style);
        createCell(row, 5, "Giá Trị Còn Lại", style);
        createCell(row, 6, "Hiện Trạng", style);
        createCell(row, 7, "Kiểm Tra Mã QR", style);

        // add here
        createCell(row, 8, "Số lượng theo sổ kế toán", style);
        createCell(row, 9, "Số lượng theo kiểm kê", style);
        createCell(row, 10, "Chênh lệch", style);
        // add here

    }

    private void writeSingleLine(int rowCount, String nameElement, int index,Object object, double fontHeight){

        Row row = sheet.createRow(rowCount);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(fontHeight);
        style.setFont(font);

        if (index != -1){
            if (object instanceof Group){
                createCell(row, 0, markGroup[index], style);
            }
            if (object instanceof Category){
                createCell(row, 0, markCategory[index], style);
            }
        }
        createCell(row, 1, nameElement, style);
    }

    private void writeDataLines(int year) throws SQLException {

//        Instant now = Instant.now();
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        style.setFont(font);

        // todo: start write data
        List<Group> groups = groupRepository.findAll();

        int indexOfGroup = 0;
        for (Group group : groups){
            writeSingleLine(rowCount, group.getDescription(),indexOfGroup, group,14);
            System.out.println("Group =====> : " + group.toString());
            rowCount += 1;

            int indexOfCategory = 0;
            List<Category> categories = categoryRepository.findAllByGroupId(group.getId());
            for (Category category : categories){
                writeSingleLine(rowCount, category.getDescription(),indexOfCategory, category,12);
                System.out.println("Category =====> : " + category.getDescription());

                // do logic --------------------------------------------------------------------------------------------
                List<Statistical.Inventory> inventoryList =
                        statisticalRepository.fetchDataStatisticalInventory(category.getId(), year);

                if (inventoryList.size() > 0){
                    rowCount += 1;
                }

                int stt = 1;
                for (Statistical.Inventory e : inventoryList){
                    System.out.println("<<=====-------------- Output Data --------------=====>>");
                    System.out.println(
                            e.getName() + "-" + e.getCode() + "-" + e.getPlace() + "-" + e.getPrice() + "-"
                            + e.getTimeAllocationType() + "-" + e.getAllocationDuration() + "-"
                            + e.getDepreciationRate() + "-" + e.getTimeStartDepreciation() + "-"
                            + e.getMaterialStatus() + "-" + e.getMaterialCheck());

                    Row row = sheet.createRow(rowCount++);
                    int columnCount = 0;
                    createCell(row,columnCount++,stt++,style); // STT
                    createCell(row,columnCount++,e.getName(),style); // Ten Tai San
                    createCell(row,columnCount++,e.getCode(),style);// Ma So Tai San
                    createCell(row,columnCount++,e.getPlace(),style);// Noi Su Dung
                    createCell(row,columnCount++,e.getPrice(),style);// Nguyen Gia

                    // logic to calculated depreciation
                    LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Ho_Chi_Minh"));
                    LocalDateTime old = LocalDateTime.ofInstant(e.getTimeStartDepreciation(), ZoneId.of("Asia/Ho_Chi_Minh"));
                    Double priceRemain = 0.0;
                    if (e.getTimeAllocationType().equals(TimeAllocationType.YEAR)){
                        long numberDiffYear = ChronoUnit.YEARS.between(old,now);
                        System.out.println(">>> DIFF YEAR : " + numberDiffYear);
                        priceRemain = e.getPrice() -
                                ((e.getPrice()*(e.getDepreciationRate()/100))*numberDiffYear);
                    }
                    if (e.getTimeAllocationType().equals(TimeAllocationType.MONTH)){
                        long numberDiffMonth = ChronoUnit.MONTHS.between(old,now);
                        System.out.println(">>> DIFF MONTH : " + numberDiffMonth);
                        priceRemain = e.getPrice() -
                                (e.getPrice()*(e.getDepreciationRate()/100)*numberDiffMonth);
                    }
                    // logic to calculated depreciation
                    createCell(row,columnCount++,priceRemain,style);// Gia tri con lai

                    createCell(row,columnCount++,e.getMaterialStatus(),style);// Hien trang
                    createCell(row,columnCount++,e.getMaterialCheck(),style);// Kiem tra QRcode

                    // add here
                    createCell(row,columnCount++,1,style);// So luong theo ke toan
                    createCell(row,columnCount++,subMethod(e.getMaterialCheck()),style);// So luong theo kiem ke
                    createCell(row,columnCount,1 - subMethod(e.getMaterialCheck()),style);// Chenh lech
                    // add here

                }
                // do logic --------------------------------------------------------------------------------------------

                rowCount += 1;
                indexOfCategory += 1;
            }
            indexOfGroup += 1;
        }

        rowCount += 1;
        Row row = sheet.createRow(rowCount++);
        createCell(row, 1, "MEMBERS CHECKING", style);
        for (String e : statisticalRepository.getMember(year)){
            row = sheet.createRow(rowCount++);
            createCell(row,1,e,style); // member data
        }

    }

    int subMethod(String materialCheck){
        if (materialCheck.equalsIgnoreCase("Checked")){
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void export(HttpServletResponse response, int year) throws IOException, SQLException {
        writeHeaderLine();
        writeDataLines(year);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
//        workbook.close();
        outputStream.close();// close to test export many times
    }
}

// ---------------------------------------------------------------------------------------------------------------------
// way 2
//                Query query = entityManager
//                        .createNativeQuery("{EXEC STATISTICAL_INVENTORY(?)}", "StatisticalResult")
//                        .setParameter(1, category.getId());
// way 3
//                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("STATISTICAL_INVENTORY");
//                query.registerStoredProcedureParameter("categoryId", Long.class, ParameterMode.IN);
//                query.setParameter("categoryId",category.getId());
//                query.execute();
//                List resultSet = query.getResultList();
//                for (Object x : resultSet){
//                    ResultSet y = (ResultSet)x;
//                    System.out.println(y.getString("name"));
//                }
// way 4
//                List<StatisticalResult> list = entityManager.createNativeQuery("EXEC STATISTICAL_INVENTORY (?)")
//                        .unwrap(Query.class)
//                        .setResultTransformer(Transformers.aliasToBean(StatisticalResult.class)).getResultList();
// way 5
//                JsonNode properties = (JsonNode) entityManager.createStoredProcedureQuery("STATISTICAL_INVENTORY")
//                        .registerStoredProcedureParameter("categoryId", Long.class, ParameterMode.IN)
//                        .setParameter("categoryId", category.getId())
//                        .unwrap(org.hibernate.query.NativeQuery.class)
//                        .addScalar("properties", JsonNodeBinaryType.INSTANCE)
//                        .getResultList();
//                Iterator<JsonNode> iterator = properties.iterator();
// way 6
//                PreparedStatement ps = con.prepareStatement(sql);
//                ps.setLong(1, category.getId());
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()){
//                    for (int i = 0 ; i < 9 ; i++){
//                        System.out.println(rs.getString("name"));
//                    }
//                }
