/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udanalyser;

/**
 *
 * @author darwin
 */
public class ThreadDataAnalyser {
    private final String dataSeparator;
    private final int DATASIZE = 2;
    
    private String[] dataArray;
    
    public ThreadDataAnalyser(String dataSeparator){
        this.dataSeparator = dataSeparator;
        
    }
    
    public void setData(String data){
        String[] temp = dataSplit(data);
        if(temp.length == DATASIZE){
            dataArray = temp;
        }else{
            dataArray[0] = "Error";
            dataArray[1] = "Error";
        }
        
    }
    
    private String[] dataSplit(String data){
        return data.split(dataSeparator);
        
    }
    
    public String getID(){
        return dataArray[0];
    }
    
    public int getdata(){
        return Integer.parseInt(dataArray[1]);
    }
    
}
