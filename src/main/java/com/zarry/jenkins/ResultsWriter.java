package com.zarry.jenkins;



import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
/**
 * Author: lzarou
 * Date: 6/23/13
 * Time: 11:41 PM
 */
public class ResultsWriter {
    private static final String NEWLINE = System.getProperty("line.separator");
    private boolean writeToFile = false;
    private String fileName;
    private BufferedWriter bw;
    private ArrayList<Integer> columnWidth;
    private LinkedHashMap<String,Integer> headerAndColumnWidth;
    private int autoSizeHeaderBuffer = 2;


    public ResultsWriter(){

    }

    public void setFileName(String fn){
        fileName = fn;
        try{
            writeToFile = true;
            bw = new BufferedWriter(new FileWriter(new File(fileName)));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getFileName(){return fileName;}
    public void flushFile(){
        try{
            bw.flush();
            bw.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateColumnWidthWhenAutoSized(){
        for(String key : headerAndColumnWidth.keySet()){
             if(headerAndColumnWidth.get(key) == 0){
                headerAndColumnWidth.put(key, calcColumnWidth(key, autoSizeHeaderBuffer));
             }
        }
    }

    public void writeGenericHeader(LinkedHashMap<String, Integer> columns){
        headerAndColumnWidth = columns;
        updateColumnWidthWhenAutoSized();

        StringBuilder headerColumnBuilder = new StringBuilder();
        StringBuilder line = new StringBuilder();

        for(String columnText : headerAndColumnWidth.keySet()){
            Integer columnBuffer = getColumnBuffer(headerAndColumnWidth.get(columnText), columnText);
            StringBuilder buffer = new StringBuilder();
            StringBuilder lineBuffer = new StringBuilder();

            for(int k = 0; k < columnBuffer; k++){
                buffer.append(" ");
                lineBuffer.append("-");
            }

            headerColumnBuilder
                    .append("|")
                    .append(buffer)
                    .append(columnText)
                    .append(buffer);

            line
                .append("+")
                .append(lineBuffer)
                .append(buildLineUnderColumnHeader(columnText))
                .append(lineBuffer);
        }

        System.out.format(NEWLINE);
        System.out.println(headerColumnBuilder.append("|").toString());
        System.out.println(line.append("+").toString());

    }

    private Integer getColumnBuffer(Integer width, String text){
        return (width - text.length()) / 2;

    }

    private String buildLineUnderColumnHeader(String columnText){
        String underLine = "";
        for(int i = 0; i < columnText.length(); i++){
            underLine += "-";
        }
        return underLine;
    }

    private ArrayList<Integer> getHeaderColumnWidths(ArrayList<String> column){
        return getHeaderColumnWidths(column, 2);
    }

    private ArrayList<Integer> getHeaderColumnWidths(ArrayList<String> column, int cushion){
        ArrayList<Integer> width = new ArrayList<Integer>(0);

        for(Integer j = 0; j < column.size(); j++){
            width.add(j, calcColumnWidth(column.get(j),cushion));
        }
        return width;
    }

    private Integer calcColumnWidth(String columnText, int cushion){
        int columnWidth = columnText.length();
        return columnWidth += cushion + cushion;
    }

    public void writerJobInfo(String ciServerUrl, String ciJob){
        Calendar now = GregorianCalendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH);
        int month = now.get(Calendar.MONTH) + 1;
        int year = now.get(Calendar.YEAR);
        int hour = now.get(Calendar.HOUR);
        int minute = now.get(Calendar.MINUTE);
        String am_pm;
        if(now.get(Calendar.AM_PM) == 0){
            am_pm = "AM";
        }
        else{
            am_pm = "PM";
        }

        System.out.format(NEWLINE);
        System.out.format("%-15s%s","Report Time: ", year + "/" + month + "/" + day + "  " + hour + ":" + minute + " " + am_pm + NEWLINE);
        System.out.format("%-15s%s","Jenkins CI: ", ciServerUrl + NEWLINE);
        System.out.format("%-15s%s","Job: ", ciJob + NEWLINE);
        if (writeToFile){
            try{
                bw.newLine();
                bw.write("Report Time:\t" + year + "/" + month + "/" + day + "  " + hour + ":" + minute + "\t" + am_pm + NEWLINE );
                bw.write("Jenkins CI:\t" + ciServerUrl);  bw.newLine();
                bw.write("Job:\t\t" + ciJob); bw.newLine();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void writeReportRow(String build, String failedTestCount, String totalTestCount, String duration, Date time){
        String durationReadable = convertMilliSecToReadable(duration);
        String t = "\t\t";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String s = " ";

        System.out.format("%1s%-9s%1s%-21s%1s%-20s%1s%-18s%1s%-16s%1s",
                s,StringUtils.center(build, 9),
                s,StringUtils.center(failedTestCount, 21),
                s,StringUtils.center(totalTestCount, 20),
                s,StringUtils.center(durationReadable, 18),
                s,StringUtils.center(dateFormat.format(time), 16),s
                + NEWLINE);
        if (writeToFile){
            try {
                bw.write(build + t + failedTestCount + t + "\t" + totalTestCount + t + durationReadable); bw.newLine();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String convertMilliSecToReadable(String milliseconds){
        return String.format("%d h %d m",
                TimeUnit.MILLISECONDS.toHours(Long.parseLong(milliseconds)),
                TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(milliseconds))-
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(Long.parseLong(milliseconds)))

        );
    }

    public Date convertEpoch(String epochTime){
        return new java.util.Date(Long.parseLong(epochTime));
    }


    public void setAutoSizeHeaderBuffer(int autoSizeHeaderBuffer) {
        this.autoSizeHeaderBuffer = autoSizeHeaderBuffer;
    }
}
