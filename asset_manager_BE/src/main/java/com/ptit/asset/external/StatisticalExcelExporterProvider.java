package com.ptit.asset.external;

import com.ptit.asset.domain.Category;
import com.ptit.asset.domain.Group;
import com.ptit.asset.repository.*;
import com.ptit.asset.repository.data.Statistical;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class StatisticalExcelExporterProvider {

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
    private final ProductRepository productRepository;
    private final MaterialRepository materialRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public StatisticalExcelExporterProvider(StatisticalRepository statisticalRepository,
                                            GroupRepository groupRepository,
                                            CategoryRepository categoryRepository,
                                            ProductRepository productRepository,
                                            MaterialRepository materialRepository){
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("Inventory");

        this.statisticalRepository = statisticalRepository;
        this.groupRepository = groupRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.materialRepository = materialRepository;
    }

    // ------------------------------------------------------
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }
        else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }
        else {
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
        style.setWrapText(true);

        createCell(row, 0, "STT", style);
        createCell(row, 1, "Tên Tài Sản", style);
        createCell(row, 2, "Mã Số", style);
        createCell(row, 3, "Nơi Sử Dụng", style);

        createCell(row, 4, "Số Lượng", style);
        createCell(row, 5, "Nguyên Giá", style);
        createCell(row, 6, "Giá Trị Còn Lại", style);

        createCell(row, 7, "Hiện Trạng", style);
        createCell(row, 8, "Kiểm Tra Mã QR", style);
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


    private void writeDataLines(int year) {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        style.setFont(font);

        // todo: start write data
        List<Group> groups = groupRepository.findAll();

        int indexOfGroup = 0;
        for (Group group : groups){
            writeSingleLine(rowCount, group.getDescription(),indexOfGroup, group, 14);
            System.out.println("Group : " + group.toString());
            // do something logic
            rowCount += 1;

            int indexOfCategory = 0;
            List<Category> categories = categoryRepository.findAllByGroupId(group.getId());
            for (Category category : categories){
                writeSingleLine(rowCount, category.getDescription(),indexOfCategory,category, 12);
                System.out.println("Category : " + category.toString());

                // do something logic ----------------------------------------------------------------------------------

                // test 1//
//                statisticalRepository.fetchDataStatisticalInventory(category.getId());

                // test 2//
//                List<Statistical.Inventory> inventoryList = statisticalRepository.fetchDataStatisticalInventory(category.getId());
//                for (Statistical.Inventory e : inventoryList){
//                    System.out.println("===== Data =====");
//                    System.out.println(
//                            e.getName() + "-"
//                            + e.getCode() + "-"
//                            + e.getPlace() + "-"
//                            + e.getPrice() + "-"
//                            + e.getTimeAllocationType() + "-"
//                            + e.getAllocationDuration() + "-"
//                            + e.getDepreciationRate() + "-"
////                            + e.getTimeStartDepreciation() + "-"
//                            + e.getMaterialStatus() + "-"
//                            + e.getMaterialCheck()
//                    );
//                }

                // test 3 //
//                List<Statistical.Material> materialList = statisticalRepository.fetchDataMaterial();
//                for (Statistical.Material e : materialList){
//                    System.out.println(e.getName() + "-" + e.getQuantity());
//                }


                // test 4 //
                Query query = entityManager
                        .createNativeQuery("{EXEC STATISTICAL_INVENTORY(?)}",Statistical.Inventory.class)
                        .setParameter(1, category.getId());

                List<Statistical.Inventory> resultSet = query.getResultList();

                // do something logic ----------------------------------------------------------------------------------

                rowCount += 1;
                indexOfCategory += 1;
            }
            indexOfGroup += 1;
        }
    }





    public void export(HttpServletResponse response, int year) throws IOException {
        writeHeaderLine();
        writeDataLines(year);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

} // end class
