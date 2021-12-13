package BrianW.AKA.BigChan.Tools;

import java.util.ArrayList;
import java.util.List;

public class DnsData {
    public short transactionID;
    public short Flags;
    public short Questions;
    public short AnswersRRS;
    public short AuthorityRRS;
    public short AdditionalRRS;
    public List<String> Records;
    public short RecordType;
    public short RecordClass;
    public short Field;
    public short Type;
    public short FieldClass;
    public int TTL;
    public void test(){
        this.Records.contains("test");
    }

    public DnsData() {
        this.Records = new ArrayList<>();
    }
}
