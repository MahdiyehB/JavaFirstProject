package utils.jsonUtil;

import java.time.LocalDateTime;

/**
 * @author Mahdiyeh Biglari
 */
public class JSONDetails {
    private String fileName;
    private int recordNumber;
    private String errorCode;
    private String errorClassificationCode;
    private String errorDescription;
    private LocalDateTime errorDate;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorClassificationCode() {
        return errorClassificationCode;
    }

    public void setErrorClassificationCode(String errorClassificationCode) {
        this.errorClassificationCode = errorClassificationCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public LocalDateTime getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(LocalDateTime errorDate) {
        this.errorDate = errorDate;
    }
}
