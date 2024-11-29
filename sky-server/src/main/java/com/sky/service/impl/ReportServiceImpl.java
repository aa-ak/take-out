package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UserMapper  userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private WorkspaceService workspaceService;


    /**
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public TurnoverReportVO getTurnOverStart(LocalDate startDate, LocalDate endDate) {

        List<LocalDate> datelist = new ArrayList<>();
        List<Double> turnOverList = new ArrayList<>();
        datelist.add(startDate);
        while (!startDate.equals(endDate)) {


            startDate = startDate.plusDays(1);
            datelist.add(startDate);
        }
        String join = StringUtils.join(datelist, ",");
        for (LocalDate localDate : datelist) {


            LocalDateTime localDate1 = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime localDate2 = LocalDateTime.of(localDate, LocalTime.MAX);
            Integer status=Orders.COMPLETED;
            Double sum = orderMapper.getTurnOver(localDate1, localDate2,status );
            sum=sum==null?0.0:sum;
            turnOverList.add(sum);

            //localdate只有年月日而没有时分秒
//            select sum(amount) from orders where order_time>localDate1 and <localdate2 and status=#{status}
        }

        return TurnoverReportVO.builder()
                .dateList(join)
                .turnoverList(StringUtils.join(turnOverList, ","))
                .build();


    }

    /**
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserReport(LocalDate begin, LocalDate end) {
        List<LocalDate>dateList=new ArrayList<>();
        List<Integer>newUserList=new ArrayList<>();
        List<Integer>totalUserList=new ArrayList<>();
        while(!begin.equals(end))
        {
            dateList.add(begin);
            begin=begin.plusDays(1);
        }
        String join = StringUtils.join(dateList, ",");
        dateList.forEach(
                date->{
                    LocalDateTime dateTime1=LocalDateTime.of(date,LocalTime.MIN);
                    LocalDateTime dateTime2=LocalDateTime.of(date,LocalTime.MAX);
                    Integer Newuser=userMapper.getUser(dateTime1,dateTime2);
                    newUserList.add(Newuser);
                    Integer totalUser=userMapper.getUser1();
                    totalUserList.add(totalUser);
                }
        );
        return UserReportVO.builder()
                .dateList(join)
                .newUserList(String.join(",",newUserList.toString()))
                .totalUserList(String.join(",",totalUserList.toString()))
                .build();

    }

    /**
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrderReport(LocalDate begin, LocalDate end) {
        List<LocalDate>dateList=new ArrayList<>();
        AtomicInteger totalOrder = new AtomicInteger();
        List<Integer> validOrderCountList= new ArrayList<>();
        List<Integer> OrderCountList= new ArrayList<>();
        AtomicReference<Integer> totalvalidcount= new AtomicReference<>(0);
        while(!begin.equals(end))
        {
            dateList.add(begin);
            begin=begin.plusDays(1);
        }
        String join = StringUtils.join(dateList, ",");
        dateList.forEach(
                date->{
                    LocalDateTime dateTime1=LocalDateTime.of(date,LocalTime.MIN);
                    LocalDateTime dateTime2=LocalDateTime.of(date,LocalTime.MAX);
                    Integer totalOrderCount=orderMapper.getOrderCount(dateTime1,dateTime2);
                    totalOrder.addAndGet(totalOrderCount);
                    Integer validOrderCount=orderMapper.getValidOrderCount(dateTime1,dateTime2,Orders.COMPLETED);
                    OrderCountList.add(validOrderCount);

                    validOrderCountList.add(validOrderCount);

                }
        );
        Integer totalValidcount=validOrderCountList.stream().reduce(Integer::sum).get();

        Double orderCompletionRate=0.0;
        if(totalOrder.get()!=0)
        {
            orderCompletionRate=totalValidcount.doubleValue()/totalOrder.get();
        }
       return OrderReportVO.builder()
                .dateList(join)
                .orderCompletionRate(orderCompletionRate)
               .orderCountList(StringUtils.join(OrderCountList, ","))
                .validOrderCount(totalValidcount)
                .totalOrderCount(totalOrder.get())
                .validOrderCountList(String.join(",",validOrderCountList.toString()))
                .build();

    }

    /**
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
//        log.info("热销商品10:{},{}",begin,end);
        List<String> nameList = new ArrayList<>();
        List<Integer>numberlist=new ArrayList<>();

        LocalDateTime dateTime1=LocalDateTime.of(begin ,LocalTime.MIN);
        LocalDateTime dateTime2=LocalDateTime.of(end,LocalTime.MAX);
        List<GoodsSalesDTO> salesTop10=orderMapper.getSalesTop10(dateTime1,dateTime2,Orders.COMPLETED);
        salesTop10.forEach(
          goodsSalesDTO -> {
              nameList.add(goodsSalesDTO.getName());
              numberlist.add(goodsSalesDTO.getNumber());

                            }
                    );
//        numberlist=salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String join2 = StringUtils.join(numberlist, ",");
        return SalesTop10ReportVO.builder()
                .nameList(String.join(",",nameList))
                .numberList(join2)
                .build();
    }

    /**
     * @param response
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) throws FileNotFoundException {

        //查询数据库，获取营业数据
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end=LocalDate.now().minusDays(1);

        //查询概览数据
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        FileInputStream fileInputStream=new FileInputStream("sky-server/src/main/resources/template/运营数据报表模板.xlsx");

        try{
            XSSFWorkbook excel = new XSSFWorkbook(fileInputStream);
            //填充时间
            XSSFSheet sheet1 = excel.getSheet("Sheet1");
            sheet1.getRow(1).getCell(1).setCellValue("时间:"+begin+"至"+end);
            sheet1.getRow(3).getCell(2).setCellValue(businessData.getTurnover());
            sheet1.getRow(3).getCell(4).setCellValue(businessData.getOrderCompletionRate());
            sheet1.getRow(3).getCell(6).setCellValue(businessData.getNewUsers());
            sheet1.getRow(4).getCell(2).setCellValue(businessData.getValidOrderCount());
            sheet1.getRow(4).getCell(4).setCellValue(businessData.getUnitPrice());

            for(int i=0;i<30;i++)
            {
                LocalDate date=begin.plusDays(i);
                BusinessDataVO businessData1 = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                XSSFRow row = sheet1.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData1.getTurnover());
                row.getCell(3).setCellValue(businessData1.getValidOrderCount());
                row.getCell(4).setCellValue(businessData1.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData1.getUnitPrice());
                row.getCell(6).setCellValue(businessData1.getNewUsers());
            }
           //
            ServletOutputStream outputStream = response.getOutputStream();
           //
            excel.write(outputStream);
            outputStream.close();


        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }
}
