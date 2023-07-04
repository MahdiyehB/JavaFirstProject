package domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import usecases.InsertAccountToDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class Account {

    private String recordNumber;
    private String accountNumber;
    private int accountType;
    private int accountCustomerID;
    private long accountLimit;
    private LocalDateTime accountOpenDate;
    private long accountBalance;


    public Account(String recordNumber, String accountNumber, int accountType, int accountCustomerID, long accountLimit, LocalDateTime accountOpenDate, long accountBalance) {
        this.recordNumber = recordNumber;
        this.accountNumber = StringUtils.leftPad(accountNumber, 22, "0");
        this.accountType = accountType;
        this.accountCustomerID =  accountCustomerID;
        this.accountLimit = accountLimit;
        this.accountOpenDate = accountOpenDate;
        this.accountBalance = accountBalance;
    }



    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getAccountCustomerID() {
        return accountCustomerID;
    }

    public void setAccountCustomerID(int accountCustomerID) {
        accountCustomerID = accountCustomerID;
    }

    public long getAccountLimit() {
        return accountLimit;
    }

    public void setAccountLimit(long accountLimit) {
        this.accountLimit = accountLimit;
    }

    public LocalDateTime getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(LocalDateTime accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
    }

    public long getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(long accountBalance) {
        this.accountBalance = accountBalance;
    }

}
