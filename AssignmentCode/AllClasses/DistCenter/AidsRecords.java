package DistCenter;

/**
 * Represent the aid records in the distribution center
 */
public class AidsRecords {
    
    private String index;
    private String donorName;
    private String donorPhone;
    private String itemName;
    private String donorQuantityGiven;
    private String ngoQuantityNeeded;
    private String ngoName;
    private String ngoManpower;
    private String status;

    /**
     * Construct an AidsRecords object with all data field
     * setted to "-"
     */
    public AidsRecords(){
        
        this.index = "-";
        this.donorName = "-";
        this.donorPhone = "-";
        this.itemName = "-";
        this.donorQuantityGiven = "-";
        this.ngoQuantityNeeded = "-";
        this.ngoName = "-";
        this.ngoManpower = "-";
        this.status = "-";
    };

    /**
     * Construct an AidsRecords object with all data field
     * setted according to the parameters of data field
     * @param index 
     * @param donorName
     * @param donorPhone
     * @param itemName
     * @param donorQuantityGiven
     * @param ngoQuantityNeeded
     * @param ngoName
     * @param ngoManpower
     * @param status
     */
    public AidsRecords(String index, String donorName, String donorPhone, String itemName, 
                String donorQuantityGiven, String ngoQuantityNeeded, 
                String ngoName, String ngoManpower, String status){

        this.index = index;
        this.donorName = donorName;
        this.donorPhone = donorPhone;
        this.itemName = itemName;
        this.donorQuantityGiven = donorQuantityGiven;
        this.ngoQuantityNeeded = ngoQuantityNeeded;
        this.ngoName = ngoName;
        this.ngoManpower = ngoManpower;
        this.status = status;
    }

    /**
     * This method is used to set to all the 
     * data field of an AidsRecords object 
     * according to the String parameters
     * @param index
     * @param donorName
     * @param donorPhone
     * @param itemName
     * @param donorQuantityGiven
     * @param ngoQuantityNeeded
     * @param ngoName
     * @param ngoManpower
     * @param status
     */
    public void setAllAttAidsRecords(String index, String donorName, String donorPhone, String itemName, 
                                  String donorQuantityGiven, String ngoQuantityNeeded, 
                                  String ngoName, String ngoManpower, String status){

        this.index = index;
        this.donorName = donorName;
        this.donorPhone = donorPhone;
        this.itemName = itemName;
        this.donorQuantityGiven = donorQuantityGiven;
        this.ngoQuantityNeeded = ngoQuantityNeeded;
        this.ngoName = ngoName;
        this.ngoManpower = ngoManpower;
        this.status = status;
    }

    /**
     * This method is used to set to all the 
     * data field of an AidsRecords object 
     * according to the AidsRecords parameter
     * @param tempAidsRecords
     */
    public void setAllAttAidsRecords(AidsRecords tempAidsRecords){

        this.index = tempAidsRecords.index;
        this.donorName = tempAidsRecords.donorName;
        this.donorPhone = tempAidsRecords.donorPhone;
        this.itemName = tempAidsRecords.itemName;
        this.donorQuantityGiven = tempAidsRecords.donorQuantityGiven;
        this.ngoQuantityNeeded = tempAidsRecords.ngoQuantityNeeded;
        this.ngoName = tempAidsRecords.ngoName;
        this.ngoManpower = tempAidsRecords.ngoManpower;
        this.status = tempAidsRecords.status;
    }

    /**
     * This method sets the AidsRecords index
     * @param index
     */
    public void setIndex(String index){
        this.index = index;
    }

    /**
     * This method sets the AidsRecords donor's name
     * @param donorName Name of the donor
     */
    public void setDonorName(String donorName){
        this.donorName = donorName;
    }

    /**
     * This method sets the AidsRecords donor's phone number
     * @param donorPhone
     */
    public void setDonorPhone(String donorPhone){
        this.donorPhone = donorPhone;
    }

    /**
     * This method sets the AidsRecords item's name
     * @param itemName
     */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    /**
     * This method sets the AidsRecords quantity of item
     * given by the donor
     * @param donorQuantityGiven
     */
    public void setDonorQuantityGiven(String donorQuantityGiven){
        this.donorQuantityGiven = donorQuantityGiven;
    }

    /**
     * This method sets the AidsRecords quantity of item
     * needed by the NGO
     * @param ngoQuantityNeeded
     */
    public void setNgoQuantityNeeded(String ngoQuantityNeeded){
        this.ngoQuantityNeeded = ngoQuantityNeeded;
    }

    /**
     * This method sets the AidsRecords NGO's name
     * @param ngoName
     */
    public void setNgoName(String ngoName){
        this.ngoName = ngoName;
    }

    /**
     * This method sets the AidsRecords NGO's manpower
     * @param ngoManpower
     */
    public void setNgoManPower(String ngoManpower){
        this.ngoManpower = ngoManpower;
    }

    /**
     * This method sets the AidsRecords matching status
     * @param status
     */
    public void setStatus(String status){
        this.status = status;
    }

    /**
     * This method returns the AidsRecords index
     * @return 
     */
    public String getIndex(){
        return index;
    }

    /**
     * This method returns the AidsRecords donor's name
     * @return
     */
    public String getDonorName(){
        return donorName;
    }

    /**
     * This method returns the AidsRecords donor's phone number
     * @return donor phone number
     */
    public String getDonorPhone(){
        return donorPhone;
    }

    /**
     * This method returns the AidsRecords item's name
     * @return item name
     */
    public String getItemName(){
        return itemName;
    }

    /**
     * This method returns the AidsRecords quantity
     * given by the donor
     * @return quantity given by donor
     */
    public String getDonorQuantityGiven(){
        return donorQuantityGiven;
    }

    /**
     * This method returns the AidsRecords quantity
     * needed by the NGO
     * @return Quantity needed by NGO
     */
    public String getNgoQuantityNeeded(){
        return ngoQuantityNeeded;
    }

    /**
     * This method returns the AidsRecords NGO's name
     * @return NGO name
     */
    public String getNgoName(){
        return ngoName;
    }

    /**
     * This method returns the AidsRecords NGO's manpower
     * @return NGO manpower
     */
    public String getNgoManpower(){
        return ngoManpower;
    }

    /**
     * This method returns the AidsRecords matching status
     * @return status
     */
    public String getStatus(){
        return status;
    }


    /**
     * Returns all the data fields of AidsRecords
     * combined in a String
     * @param indexCounter index to be written in the String
     * @return a String of data fields combined
     */
    public String csvFormatToWrite(int indexCounter){

        String recordsCombined;
        String strIndex = String.valueOf(indexCounter);

        recordsCombined = strIndex + "," + donorName + "," + donorPhone + "," + itemName + "," +  donorQuantityGiven + "," + ngoQuantityNeeded + "," + ngoName + "," + ngoManpower + "," + status;
        return recordsCombined;
    }

}